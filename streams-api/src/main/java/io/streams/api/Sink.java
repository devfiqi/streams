package io.streams.api;

/**
 * Consumes the records that leave a job.
 */
@FunctionalInterface
public interface Sink<T> {

    /**
     * Handles a single record at the end of the dataflow.
     */
    void write(T record) throws Exception;
}
