package org.bsc.langgraph4j.checkpoint;

import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static java.lang.String.format;
import static java.util.Objects.requireNonNull;

public class PostgresSaver {
    private static final Logger log = LoggerFactory.getLogger(PostgresSaver.class);
    /**
     * Datasource used to create the store
     */
    protected final DataSource datasource;
    protected final String table;

    protected PostgresSaver( Builder builder ) {
        this.datasource = builder.datasource;
        this.table = builder.table;
        initTable( builder.dropTableFirst,
                builder.createTable,
                builder.useIndex,
                builder.indexListSize);
    }

    public static Builder builder() {
        return new Builder();
    }

    protected void initTable(boolean dropTableFirst, boolean createTable, boolean useIndex, Integer indexListSize) {

        String query = "init";
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            if (dropTableFirst) {
                statement.executeUpdate(format("DROP TABLE IF EXISTS %s", table));
            }
            if (createTable) {
                query = format("CREATE TABLE IF NOT EXISTS %s (embedding_id UUID PRIMARY KEY, " +
                                " text TEXT NULL, %s )",
                        table, "dimension");
                statement.executeUpdate(query);
            }
            if (useIndex) {
                final String indexName = table + "_ivfflat_index";
                query = format(
                        "CREATE INDEX IF NOT EXISTS %s ON %s " +
                                "USING ivfflat (embedding vector_cosine_ops) " +
                                "WITH (lists = %s)", indexName, table, indexListSize);
                statement.executeUpdate(query);
            }
        } catch (SQLException e) {
            throw new RuntimeException(format("Failed to execute '%s'", query), e);
        }
    }

    public void removeAll(Collection<String> ids) {
        requireNonNull(ids, "ids");
        String sql = format("DELETE FROM %s WHERE embedding_id = ANY (?)", table);
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            Array array = connection.createArrayOf("uuid", ids.stream().map(UUID::fromString).toArray());
            statement.setArray(1, array);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeAll() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(format("TRUNCATE TABLE %s", table));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Datasource connection
     * Creates the vector extension and add the vector type if it does not exist.
     * Could be overridden in case extension creation and adding type is done at datasource initialization step.
     *
     * @return Datasource connection
     * @throws SQLException exception
     */
    protected Connection getConnection() throws SQLException {
        return datasource.getConnection();
    }

    public static class Builder {
        private String host;
        private Integer port;
        private String user;
        private String password;
        private String database;
        private String table;
        private boolean useIndex;
        private Integer indexListSize;
        private boolean createTable;
        private boolean dropTableFirst;
        private DataSource datasource;

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(Integer port) {
            this.port = port;
            return this;
        }

        public Builder user(String user) {
            this.user = user;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder database(String database) {
            this.database = database;
            return this;
        }

        public Builder table(String table) {
            this.table = table;
            return this;
        }

        public Builder useIndex(Boolean useIndex) {
            this.useIndex = useIndex;
            return this;
        }

        public Builder indexListSize(int indexListSize) {
            this.indexListSize = indexListSize;
            return this;
        }

        public Builder createTable(boolean createTable) {
            this.createTable = createTable;
            return this;
        }

        public Builder dropTableFirst(boolean dropTableFirst) {
            this.dropTableFirst = dropTableFirst;
            return this;
        }

        private String requireNotBlank( String value, String name ) {
            if( requireNonNull(value, format("'%s' cannot be null", name) ).isBlank() ) {
                throw new IllegalArgumentException(format("'%s' cannot be blank", name));
            }
            return value;
        }
        public PostgresSaver build() {
            if( port <=0 ) {
                throw new IllegalArgumentException("port must be greater than 0");
            }
            if( useIndex ) {
                requireNonNull(indexListSize, "indexListSize cannot be null if useIndex is true");
            }
            var ds = new PGSimpleDataSource();
            ds.setDatabaseName( requireNotBlank(database, "database"));
            ds.setUser(requireNotBlank(user, "user"));
            ds.setPassword(requireNonNull(password, "password cannot be null"));
            ds.setPortNumbers( new int[] {port} );
            ds.setServerNames( new String[] { requireNotBlank(host, "host") } );

            if( table ==  null || table.isBlank() ) {
                table = "Checkpoints";
            }
            return new PostgresSaver( this );
        }
    }
}

/*
    public EmbeddingSearchResult<TextSegment> search(EmbeddingSearchRequest request) {
        Embedding referenceEmbedding = request.queryEmbedding();
        int maxResults = request.maxResults();
        double minScore = request.minScore();
        Filter filter = request.filter();

        List<EmbeddingMatch<TextSegment>> result = new ArrayList<>();
        try (Connection connection = getConnection()) {
            String referenceVector = Arrays.toString(referenceEmbedding.vector());
            String whereClause = (filter == null) ? "" : metadataHandler.whereClause(filter);
            whereClause = (whereClause.isEmpty()) ? "" : "AND " + whereClause;
            String query = String.format(
                    "SELECT (2 - (embedding <=> '%s')) / 2 AS score, embedding_id, embedding, text, %s FROM %s " +
                            "WHERE round(cast(float8 (embedding <=> '%s') as numeric), 8) <= round(2 - 2 * %s, 8) %s " + "ORDER BY embedding <=> '%s' LIMIT %s;",
                    referenceVector, join(",", metadataHandler.columnsNames()), table, referenceVector,
                    minScore, whereClause, referenceVector, maxResults
            );
            try (PreparedStatement selectStmt = connection.prepareStatement(query)) {
                try (ResultSet resultSet = selectStmt.executeQuery()) {
                    while (resultSet.next()) {
                        double score = resultSet.getDouble("score");
                        String embeddingId = resultSet.getString("embedding_id");

                        PGvector vector = (PGvector) resultSet.getObject("embedding");
                        Embedding embedding = new Embedding(vector.toArray());

                        String text = resultSet.getString("text");
                        TextSegment textSegment = null;
                        if (isNotNullOrBlank(text)) {
                            Metadata metadata = metadataHandler.fromResultSet(resultSet);
                            textSegment = TextSegment.from(text, metadata);
                        }
                        result.add(new EmbeddingMatch<>(score, embeddingId, embedding, textSegment));
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new EmbeddingSearchResult<>(result);
    }

    private void addInternal(String id, Embedding embedding, TextSegment embedded) {
        addAll(
                singletonList(id),
                singletonList(embedding),
                embedded == null ? null : singletonList(embedded));
    }

    public void addAll(
            List<String> ids, List<Embedding> embeddings, List<TextSegment> embedded) {
        if (isNullOrEmpty(ids) || isNullOrEmpty(embeddings)) {
            log.info("Empty embeddings - no ops");
            return;
        }
        ensureTrue(ids.size() == embeddings.size(), "ids size is not equal to embeddings size");
        ensureTrue(embedded == null || embeddings.size() == embedded.size(),
                "embeddings size is not equal to embedded size");

        try (Connection connection = getConnection()) {
            String query = String.format(
                    "INSERT INTO %s (embedding_id, embedding, text, %s) VALUES (?, ?, ?, %s)" +
                            "ON CONFLICT (embedding_id) DO UPDATE SET " +
                            "embedding = EXCLUDED.embedding," +
                            "text = EXCLUDED.text," +
                            "%s;",
                    table, join(",", metadataHandler.columnsNames()),
                    join(",", nCopies(metadataHandler.columnsNames().size(), "?")),
                    metadataHandler.insertClause());
            try (PreparedStatement upsertStmt = connection.prepareStatement(query)) {
                for (int i = 0; i < ids.size(); ++i) {
                    upsertStmt.setObject(1, UUID.fromString(ids.get(i)));
                    upsertStmt.setObject(2, new PGvector(embeddings.get(i).vector()));

                    if (embedded != null && embedded.get(i) != null) {
                        upsertStmt.setObject(3, embedded.get(i).text());
                        metadataHandler.setMetadata(upsertStmt, 4, embedded.get(i).metadata());
                    } else {
                        upsertStmt.setNull(3, Types.VARCHAR);
                        IntStream.range(4, 4 + metadataHandler.columnsNames().size()).forEach(
                                j -> {
                                    try {
                                        upsertStmt.setNull(j, Types.OTHER);
                                    } catch (SQLException e) {
                                        throw new RuntimeException(e);
                                    }
                                });
                    }
                    upsertStmt.addBatch();
                }
                upsertStmt.executeBatch();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
*/

