package io.streams.api;

/**
 * Produces the records that enter a job. Implementations run until they choose
 * to stop emitting.
 */
@FunctionalInterface
public interface Source<T> {

    /**
     * Emits every record this source produces to the given collector.
     */
    void run(Collector<T> out) throws Exception;
}
