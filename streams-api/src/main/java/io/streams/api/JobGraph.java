package io.streams.api;

/**
 * The compiled, runtime-facing description of a job. For now a job is just a
 * source feeding a sink; operator chaining arrives with the dataflow milestone.
 */
public record JobGraph<T>(String name, Source<T> source, Sink<T> sink) {}
