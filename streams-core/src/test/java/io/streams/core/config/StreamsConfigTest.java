package io.streams.core.config;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

class StreamsConfigTest {

    /**
     * A missing resource yields defaults rather than failing.
     */
    @Test
    void missingResourceFallsBackToDefaults() {
        StreamsConfig config = StreamsConfig.load("does-not-exist.yaml");

        assertThat(config.parallelism()).isEqualTo(1);
        assertThat(config.checkpointIntervalMs()).isEqualTo(10_000L);
    }

    /**
     * Dotted keys resolve through the nested maps snakeyaml produces.
     */
    @Test
    void dottedKeysResolveNestedValues() {
        StreamsConfig config = StreamsConfig.of(Map.of(
                "parallelism", 8,
                "checkpoint", Map.of("interval", Map.of("ms", 500))));

        assertThat(config.parallelism()).isEqualTo(8);
        assertThat(config.checkpointIntervalMs()).isEqualTo(500L);
        assertThat(config.networkBufferBytes()).isEqualTo(32 * 1024);
    }
}
