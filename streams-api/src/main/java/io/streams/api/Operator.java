package io.streams.api;

/**
 * Transforms records as they pass through the dataflow. This is the single
 * extension point every transformation is built on; later milestones widen it
 * with watermark and timer callbacks.
 */
@FunctionalInterface
public interface Operator<IN, OUT> {

    /**
     * Handles one input record, emitting zero or more records downstream.
     */
    void processElement(IN record, Collector<OUT> out) throws Exception;
}
