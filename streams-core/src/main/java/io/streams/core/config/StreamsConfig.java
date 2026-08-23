package io.streams.core.config;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

/**
 * Typed access to engine configuration. Values are read from a YAML file on the
 * classpath and fall back to defaults when absent.
 */
public final class StreamsConfig {

    private static final String DEFAULT_RESOURCE = "streams.yaml";

    private final Map<String, Object> values;

    private StreamsConfig(Map<String, Object> values) {
        this.values = values;
    }

    /**
     * Loads configuration from the default classpath resource, returning an
     * empty configuration if the resource is missing.
     */
    public static StreamsConfig load() {
        return load(DEFAULT_RESOURCE);
    }

    /**
     * Loads configuration from the given classpath resource.
     */
    public static StreamsConfig load(String resource) {
        try (InputStream in = StreamsConfig.class.getClassLoader().getResourceAsStream(resource)) {
            if (in == null) {
                return new StreamsConfig(Collections.emptyMap());
            }
            Map<String, Object> parsed = new Yaml().load(in);
            return new StreamsConfig(parsed == null ? Collections.emptyMap() : parsed);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load config: " + resource, e);
        }
    }

    /**
     * Builds a configuration directly from a map. Intended for tests.
     */
    public static StreamsConfig of(Map<String, Object> values) {
        return new StreamsConfig(Map.copyOf(values));
    }

    /**
     * Returns the number of parallel subtasks a job runs with by default.
     */
    public int parallelism() {
        return getInt("parallelism", 1);
    }

    /**
     * Returns the interval between checkpoints in milliseconds.
     */
    public long checkpointIntervalMs() {
        return getLong("checkpoint.interval.ms", 10_000L);
    }

    /**
     * Returns the size in bytes of a single network buffer.
     */
    public int networkBufferBytes() {
        return getInt("network.buffer.bytes", 32 * 1024);
    }

    /**
     * Reads an integer value, returning the default when the key is absent.
     */
    public int getInt(String key, int defaultValue) {
        Object v = resolve(key);
        return v == null ? defaultValue : ((Number) v).intValue();
    }

    /**
     * Reads a long value, returning the default when the key is absent.
     */
    public long getLong(String key, long defaultValue) {
        Object v = resolve(key);
        return v == null ? defaultValue : ((Number) v).longValue();
    }

    /**
     * Reads a string value, returning the default when the key is absent.
     */
    public String getString(String key, String defaultValue) {
        Object v = resolve(key);
        return v == null ? defaultValue : v.toString();
    }

    /**
     * Walks a dotted key such as "checkpoint.interval.ms" through the nested
     * maps snakeyaml produces.
     */
    @SuppressWarnings("unchecked")
    private Object resolve(String key) {
        Object current = values;
        for (String part : key.split("\\.")) {
            if (!(current instanceof Map)) {
                return null;
            }
            current = ((Map<String, Object>) current).get(part);
            if (current == null) {
                return null;
            }
        }
        return current;
    }
}
