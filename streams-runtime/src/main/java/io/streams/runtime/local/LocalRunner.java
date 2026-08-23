package io.streams.runtime.local;

import io.streams.api.JobGraph;
import io.streams.core.config.StreamsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Executes a job in the current process. This is the simplest possible runner
 * and exists to establish the submit, execute, terminate lifecycle that the
 * distributed runner will later follow.
 */
public final class LocalRunner {

    private static final Logger LOG = LoggerFactory.getLogger(LocalRunner.class);

    private final StreamsConfig config;

    public LocalRunner(StreamsConfig config) {
        this.config = config;
    }

    /**
     * Runs the job to completion, pumping every record the source produces into
     * the sink, and returns the number of records processed.
     */
    public <T> long execute(JobGraph<T> job) throws Exception {
        LOG.info("Starting job '{}' with parallelism {}", job.name(), config.parallelism());
        long start = System.nanoTime();
        long[] count = {0};

        job.source().run(record -> {
            try {
                job.sink().write(record);
                count[0]++;
            } catch (Exception e) {
                throw new RuntimeException("Sink failed for record: " + record, e);
            }
        });

        long elapsedMs = (System.nanoTime() - start) / 1_000_000;
        LOG.info("Job '{}' finished: {} records in {} ms", job.name(), count[0], elapsedMs);
        return count[0];
    }
}
