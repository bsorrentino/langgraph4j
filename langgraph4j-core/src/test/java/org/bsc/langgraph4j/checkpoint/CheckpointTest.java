package org.bsc.langgraph4j.checkpoint;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the creation date support added to {@link Checkpoint} ( see issue #234 )
 */
public class CheckpointTest {

    private static final Instant FIXED_DATE = Instant.parse("2025-09-10T10:37:31Z");

    @Test
    void createdAtDefaultsToNowOnConstructor() {
        final var before = Instant.now();
        final var checkpoint = new Checkpoint("id", Map.of(), "node", "next");
        final var after = Instant.now();

        assertNotNull(checkpoint.createdAt());
        assertFalse(checkpoint.createdAt().isBefore(before), "createdAt should not be before the construction");
        assertFalse(checkpoint.createdAt().isAfter(after), "createdAt should not be after the construction");
    }

    @Test
    void createdAtDefaultsToNowOnBuilder() {
        final var before = Instant.now();
        final var checkpoint = Checkpoint.builder()
                .nodeId("node")
                .nextNodeId("next")
                .state(Map.of())
                .build();
        final var after = Instant.now();

        assertNotNull(checkpoint.createdAt());
        assertFalse(checkpoint.createdAt().isBefore(before), "createdAt should not be before the construction");
        assertFalse(checkpoint.createdAt().isAfter(after), "createdAt should not be after the construction");
    }

    @Test
    void createdAtIsMandatory() {
        assertThrows(NullPointerException.class,
                () -> new Checkpoint("id", Map.of(), "node", "next", null));
        assertThrows(NullPointerException.class,
                () -> Checkpoint.builder().createdAt(null));
    }

    @Test
    void updateStatePreservesCreatedAt() {
        final var checkpoint = Checkpoint.builder()
                .id("id")
                .nodeId("node")
                .nextNodeId("next")
                .state(Map.of("prop1", "value1"))
                .createdAt(FIXED_DATE)
                .build();

        final var updated = checkpoint.updateState(Map.of("prop2", "value2"), null);

        assertEquals(FIXED_DATE, updated.createdAt());
        assertEquals(checkpoint.id(), updated.id());
        assertEquals("next", updated.nextNodeId());
    }

    @Test
    void copyOfRefreshesCreatedAt() {
        final var checkpoint = Checkpoint.builder()
                .id("id")
                .nodeId("node")
                .nextNodeId("next")
                .state(Map.of("prop1", "value1"))
                .createdAt(FIXED_DATE)
                .build();

        final var copy = Checkpoint.copyOf(checkpoint);

        assertNotEquals(checkpoint.id(), copy.id());
        assertFalse(copy.createdAt().isBefore(FIXED_DATE), "copied checkpoint must carry a fresh creation date");
    }

}
