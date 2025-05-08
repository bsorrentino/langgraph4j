package org.bsc.langgraph4j;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.content.DefaultContent;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.rag.query.Query;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.Result;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.*;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

public class RAGPostgresTest {
    enum AiModel {

/*
        OPENAI_GPT_4O_MINI( OpenAiChatModel.builder()
                .apiKey( System.getenv("OPENAI_API_KEY") )
                .modelName( "gpt-4o-mini" )
                .supportedCapabilities(Set.of(Capability.RESPONSE_FORMAT_JSON_SCHEMA))
                .logResponses(true)
                .maxRetries(2)
                .temperature(0.0)
                .build() ),
*/
        OLLAMA_QWEN3_14B( OllamaChatModel.builder()
                .modelName( "qwen3:14b" )
                .baseUrl("http://localhost:11434")
                .supportedCapabilities(Capability.RESPONSE_FORMAT_JSON_SCHEMA)
                .logRequests(true)
                .logResponses(true)
                .maxRetries(2)
                .temperature(0.0)
                .build() ),
        OLLAMA_QWEN2_5_7B( OllamaChatModel.builder()
                .modelName( "qwen2.5:7b" )
                .baseUrl("http://localhost:11434")
                .supportedCapabilities(Capability.RESPONSE_FORMAT_JSON_SCHEMA)
                .logRequests(true)
                .logResponses(true)
                .maxRetries(2)
                .temperature(0.0)
                .build() )
        ;

        public final ChatModel model;

        AiModel(  ChatModel model ) {
            this.model = model;
        }
    }

    interface Assistant {

        Result<String> chat(String userMessage);
    }

    private final static Logger log = LoggerFactory.getLogger(RAGPostgresTest.class);

    private void log$(Document document) {
        log.info("{}: {} ...",
                document.metadata().getString("file_name"),
                document.text().trim().substring(0, 50));
    }

    @Test
    public void loadDocumentPages() throws IOException {
        //EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
        EmbeddingModel embeddingModel = OllamaEmbeddingModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("nomic-embed-text:latest")
                .build();

        log.info( "embeddingModel.dimension: {}", embeddingModel.dimension() );

        EmbeddingStore<TextSegment> embeddingStore = PgVectorEmbeddingStore.builder()
                .host("localhost")
                .port(5432)
                .database("pg-demo")
                .user("bsorrentino")
                .password("demo")
                .table("Drug_Leaflet")
                .dimension(embeddingModel.dimension())
                .dropTableFirst(true)
                .createTable(true)
                .build();

        Path documentPath = toPath("ASPIRINA.pdf");

        var pages = PdfPageSplitter.splitPdfByPages(documentPath);

        for (Document page : pages) {

            var textSegment = page.toTextSegment();

            Embedding embedding = embeddingModel.embed(textSegment)
                                        .content();
            embeddingStore.add(embedding, textSegment);

        }

        Embedding queryEmbedding = embeddingModel.embed("cosa contiene l'ASPIRINA?")
                                                .content();
        EmbeddingSearchRequest embeddingSearchRequest1 = EmbeddingSearchRequest.builder()
                .queryEmbedding(queryEmbedding)
                .maxResults(3)
                .minScore(0.75)
                .build();

        EmbeddingSearchResult<TextSegment> embeddingSearchResult1 = embeddingStore.search(embeddingSearchRequest1);
        var embeddingMatches = embeddingSearchResult1.matches();

        embeddingMatches.stream()
                .sorted(Comparator
                        .comparing(EmbeddingMatch<TextSegment>::score, Comparator.reverseOrder()) // Sort by score descending
                        .thenComparing(match -> match.embedded().metadata().getInteger("page")) // Then by page number ascending
                )
                .forEach(match -> {
                        log.info( "page: {}, Score: {}",match.embedded().metadata().getInteger("page"), match.score());
                });

        var contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(3)
                .minScore(0.75)
                .build();

        var documents = contentRetriever.retrieve( Query.from("Quali sono le controindicazioni per l'ASPIRINA?"));
        assertFalse(  documents.isEmpty(), "No documents found" );


        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(AiModel.OLLAMA_QWEN2_5_7B.model)
                .contentRetriever(contentRetriever)
                .build();

        var result = assistant.chat("Quale è la posologia suggerita per l'ASPIRINA?");

        var answer = result.content();
        var surces = result.sources();

        log.info( "ANSWER:\n{}\n\nSOURCES: {}", answer,
                result.sources().stream()
                        .map( s -> s.textSegment().metadata().toString() )
                        .collect( Collectors.joining(",") ));


    }

    private Path toPath(String fileName) {
        try {
            URL fileUrl = getClass().getResource(format("/%s",fileName));
            return Paths.get(fileUrl.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}

class PdfPageSplitter {
    public static List<Document> splitPdfByPages(Path pdfFilePath) throws IOException {
        List<Document> documents = new ArrayList<>();

        // Load the PDF file
        try (var pdfDocument = PDDocument.load(pdfFilePath.toFile())) {
            // Ensure the PDF is not encrypted
            if (!pdfDocument.isEncrypted()) {
                var textStripper = new PDFTextStripper();
                int numberOfPages = pdfDocument.getNumberOfPages();

                // Iterate through each page
                for (int page = 1; page <= numberOfPages; page++) {
                    // Set the page range to extract text for the current page
                    textStripper.setStartPage(page);
                    textStripper.setEndPage(page);

                    // Extract text
                    String pageText = textStripper.getText(pdfDocument);

                    // Create metadata with page number
                    Metadata metadata = new Metadata();
                    metadata.put("source", Objects.toString(pdfFilePath));
                    metadata.put("page", String.valueOf(page));

                    // Create a LangChain4j Document
                    Document document = Document.from(pageText, metadata);
                    documents.add(document);
                }
            } else {
                throw new IOException("PDF is encrypted and cannot be processed.");
            }
        }

        return documents;
    }
}