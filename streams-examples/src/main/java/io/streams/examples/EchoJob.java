package io.streams.examples;

import io.streams.api.JobGraph;
import io.streams.api.Sink;
import io.streams.api.Source;
import io.streams.core.config.StreamsConfig;
import io.streams.runtime.local.LocalRunner;
import java.util.List;

/**
 * Smallest possible job: a fixed list of records read by a source and printed
 * by a sink. Exists to prove the build, config, and runner all work together.
 */
public final class EchoJob {

    /**
     * Builds the echo job and runs it on the local runner.
     */
    public static void main(String[] args) throws Exception {
        List<String> input = List.of("alpha", "bravo", "charlie", "delta");

        Source<String> source = out -> input.forEach(out::collect);
        Sink<String> sink = record -> System.out.println("  -> " + record);

        JobGraph<String> job = new JobGraph<>("echo", source, sink);

        new LocalRunner(StreamsConfig.load()).execute(job);
    }
}
