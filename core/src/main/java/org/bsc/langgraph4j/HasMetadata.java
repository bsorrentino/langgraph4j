package org.bsc.langgraph4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public interface HasMetadata<B extends HasMetadata.Builder<B>> {

    /**
     * return metadata value for key
     *
     * @param key given metadata key
     * @return metadata value for key if any
     */
    Optional<Object> getMetadata(String key );

    class Builder<B extends Builder<B>> {
        protected Map<String,Object> metadata;

        @SuppressWarnings("unchecked")
        public B addMetadata( String key, Object value ) {
            if( metadata == null ) {
                // Lazy initialization of metadata map
                metadata = new HashMap<>();
            }

            metadata.put( key, value);

            return (B)this;
        };
    }
}

