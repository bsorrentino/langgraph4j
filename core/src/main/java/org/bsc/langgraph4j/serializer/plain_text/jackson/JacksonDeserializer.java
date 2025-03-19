package org.bsc.langgraph4j.serializer.plain_text.jackson;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

@FunctionalInterface
public interface JacksonDeserializer<T> {

    T deserialize( JsonNode node ) throws IOException, JacksonException;
}
