# LangGraph4j :: CockroachDB Saver

`CockroachDBSaver` persists LangGraph4j workflow checkpoints in CockroachDB using the
PostgreSQL JDBC driver. It implements the `BaseCheckpointSaver` contract through
`AbstractCheckpointSaver` and supports checkpoint history, updates, and thread release.

## Maven Dependency

```xml
<dependency>
    <groupId>org.bsc.langgraph4j</groupId>
    <artifactId>langgraph4j-cockroachdb-saver</artifactId>
    <version>${langgraph4j.version}</version>
</dependency>
```

The module includes PostgreSQL JDBC driver `42.7.7`. A separate CockroachDB JDBC driver
is not required. The integration tests use `cockroachdb/cockroach:latest-v25.2`.

## Usage

The following example connects to a local, insecure development instance on SQL port
26257, using the `root` user and `defaultdb` database. The Java statements can be placed
inside an application method that declares `throws Exception`.

```java
// Create the saver tables if absent; the database must already exist.
var saver = CockroachDBSaver.builder()
        .host("localhost")
        .port(26257)
        .database("defaultdb")
        .user("root")
        .password("")
        .stateSerializer(new ObjectStreamStateSerializer<>(AgentState::new))
        .createTables(true)
        .build();

// Return a state update from the node; the saver persists workflow checkpoints.
var graph = new StateGraph<>(AgentState::new)
        .addNode("greet", node_async(state -> Map.of("greeting", "Hello!")))
        .addEdge(StateGraph.START, "greet")
        .addEdge("greet", StateGraph.END);

// Retain the active thread so its checkpoints remain readable after completion.
var workflow = graph.compile(CompileConfig.builder()
        .checkpointSaver(saver)
        .releaseThread(false)
        .build());
var config = RunnableConfig.builder().threadId("conversation-1").build();

// Reuse the thread ID to access persisted state and checkpoint history.
workflow.invoke(GraphInput.args(Map.of("input", "Hi")), config);
var latest = workflow.lastStateOf(config).orElseThrow();
var history = workflow.getStateHistory(config);
```

The saver opens and closes JDBC connections for its operations. Its internally created
`PGSimpleDataSource` does not provide connection pooling. `CockroachDBSaver` does not
implement `AutoCloseable`; if you inject a connection pool, manage its lifecycle yourself.

## Injecting a DataSource

Use `datasource(...)` to supply a configured JDBC `DataSource`, including a connection pool
or a source with TLS and certificate settings. When supplied, it takes precedence over
the builder's host, port, database, user, and password settings.

For example, configure these environment variables for your database:

- `COCKROACH_JDBC_URL`: the PostgreSQL JDBC URL with the required TLS settings.
- `COCKROACH_USER`: the database user.
- `COCKROACH_PASSWORD`: the database password.

```java
// Supply connection and TLS settings through the JDBC URL for your deployment.
var datasource = new PGSimpleDataSource();
datasource.setURL(System.getenv("COCKROACH_JDBC_URL"));
datasource.setUser(System.getenv("COCKROACH_USER"));
datasource.setPassword(System.getenv("COCKROACH_PASSWORD"));

// Use existing tables provisioned by your deployment process.
var configuredSaver = CockroachDBSaver.builder()
        .datasource(datasource)
        .stateSerializer(new ObjectStreamStateSerializer<>(AgentState::new))
        .build();
```

The saver still obtains a connection during `build()`, even when table creation is disabled.

## Builder Options

| Option | Default | Description |
| --- | --- | --- |
| `stateSerializer(StateSerializer)` | Required | Serializer for the agent state. |
| `datasource(DataSource)` | Unset | Externally configured JDBC data source; overrides direct connection settings. |
| `host(String)` | Required without a data source | Nonblank server hostname. |
| `port(Integer)` | `26257` | SQL port; must be greater than zero when constructing the internal data source. |
| `database(String)` | Required without a data source | Nonblank name of an existing database. |
| `user(String)` | Required without a data source | Nonblank database user. |
| `password(String)` | Required without a data source | Non-null password; may be empty for a local insecure instance. |
| `createTables(boolean)` | `false` | Execute the bundled table and index creation statements during `build()`. |
| `dropTablesFirst(boolean)` | `false` | Drop both saver tables before recreating them. Implies `createTables(true)` and deletes all checkpoint data. |

The builder has no `property(...)` or `properties(...)` methods. Configure additional JDBC
properties on an injected data source. Use one serializer compatible with the stored
state; configuring another serializer replaces the previous one.

## Schema and Serialization

The saver uses `db/migration/v1.1__init.sql` for schema initialization and
`db/v1.1__commands.sql` for persistence operations. These are classpath resources bundled
with the module. Initialization runs `CREATE TABLE IF NOT EXISTS` and
`CREATE INDEX IF NOT EXISTS`; it does not run a schema migration engine or alter existing columns.

Two tables store the thread and checkpoint records:

| Table | Main columns | Purpose |
| --- | --- | --- |
| `LG4JThread` | `thread_id` UUID, `thread_name`, `is_released` | One row per execution of a logical thread; a partial unique index permits only one unreleased row per thread name. |
| `LG4JCheckpoint` | `checkpoint_id` UUID, `thread_id`, node IDs, `state_data`, `state_content_type`, `saved_at` | Checkpoint state and execution position, linked to the owning thread by a cascading foreign key. |

The logical thread name is the `RunnableConfig.threadId`. Checkpoint IDs must be UUID
strings; the saver converts them with `UUID.fromString(...)` before writing them.

`state_data` is a **TEXT** column containing the result of
`StateSerializer.writeDataAsString(...)`. Binary serializers can encode their output as
Base64, while text serializers supply their own string representation. The saver stores
the serializer's content type alongside the payload and requires a matching type on read.

History is selected for the active thread and ordered by `saved_at DESC`. Each history
load reads and deserializes all checkpoints for that thread. Indexes cover the thread
foreign key and the thread plus descending timestamp.

## Thread Release and Retention

Use `releaseThread(false)` during graph compilation when checkpoints should remain
available after workflow completion, as in the usage example.

Calling `saver.release(config)` marks the active `LG4JThread` row as released and returns a
`Tag` containing its loaded checkpoints. The rows remain in the database, but regular
checkpoint reads only select unreleased threads, so they no longer return that history.
A subsequent execution using the same logical thread name can create a new active thread row.

There is no saver API for deleting an individual thread or configuring TTL. Manage
retention externally when needed. Deleting a thread row cascades to its checkpoint rows.
`dropTablesFirst(true)` drops the entire saver schema and is intended for disposable test data.

## Transactions and Current Limits

- Checkpoint inserts wrap the thread upsert and checkpoint insert in a JDBC transaction.
  Updates delete the previous checkpoint and insert the replacement in the same transaction.
- The saver does not implement transaction retry handling for CockroachDB serialization
  failures such as SQLSTATE `40001`. Account for retries in your application design;
  rerunning a graph can repeat node side effects.
- Thread release selects the active thread and updates its release flag in separate
  statements without an explicit transaction. Coordinate concurrent operations on the
  same logical thread as needed.
- The `parent_checkpoint_id` column exists, but checkpoint inserts currently write `NULL`.
  Checkpoint lineage is not persisted.
- `tag(config, version)` returns empty; persisted release-tag lookup is not implemented.
  Release messages and error details are not stored. `registerInterruption(...)` returns
  the supplied metadata without persisting it.
- `clearCheckpointsCache(...)` is deprecated and returns an empty collection. This saver
  has no in-memory checkpoint cache to clear.

## Tests

The integration tests extend the core checkpoint-saver test suite and start CockroachDB
with Testcontainers. Docker must be running. From the repository root:

```sh
# Build reactor dependencies, including the core test JAR, and run the tests.
./mvnw -pl langgraph4j-cockroachdb-saver -am test -DskipTests=false
```
