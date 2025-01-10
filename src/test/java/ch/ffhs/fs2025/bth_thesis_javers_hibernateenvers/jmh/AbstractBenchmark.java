package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.jmh;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.context.ApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class AbstractBenchmark {

    private static final Integer MEASUREMENT_ITERATIONS = 3;
    private static final Integer WARMUP_ITERATIONS = 3;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    protected ApplicationContext context;

    @Test
    public void executeJmhRunner() throws RunnerException {
        String benchmarkFileName = benchmarkName() + "_" + LocalDateTime.now().format(DATE_TIME_FORMATTER) + ".json";

        Options opt = new OptionsBuilder()
                // set the class name regex for benchmarks to search for to the current class
                .include("\\." + this.getClass().getSimpleName() + "\\.")
                .warmupIterations(WARMUP_ITERATIONS)
                .measurementIterations(MEASUREMENT_ITERATIONS)
                .forks(1)
                .threads(1)
                .shouldDoGC(true)
                .shouldFailOnError(true)
                .resultFormat(ResultFormatType.JSON)
                .result("./benchmark-results/" + benchmarkFileName)
                .shouldFailOnError(true)
                // todo: remove postgres profile to run on h2
                .jvmArgs("-server", "-Dspring.config.location=classpath:/application-postgres.properties")
                .build();

        new Runner(opt).run();
    }

    protected abstract String benchmarkName();

}