package io.streams.api;

/**
 * Accepts records emitted by a source or operator and hands them to whatever
 * comes next in the dataflow.
 */
@FunctionalInterface
public interface Collector<T> {

    /**
     * Emits a single record downstream.
     */
    void collect(T record);
}
