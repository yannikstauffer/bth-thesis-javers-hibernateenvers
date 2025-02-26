package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.springframework.context.ApplicationContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class JmhBenchmarkBase {

    private static final Integer MEASUREMENT_ITERATIONS = 5;
    private static final Integer WARMUP_ITERATIONS = 5;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    protected ApplicationContext context;

    @Test
    public void executeJmhRunner() throws RunnerException {
        String benchmarkClassName = this.getClass().getSimpleName();
        String benchmarkFileName = LocalDateTime.now().format(DATE_TIME_FORMATTER) + "_" + benchmarkClassName + ".json";

        Options opt = new OptionsBuilder()
                .include("\\." + benchmarkClassName + "\\.")
                .warmupIterations(WARMUP_ITERATIONS) // CITE: traini_2023
                .measurementIterations(MEASUREMENT_ITERATIONS)
                .measurementTime(TimeValue.milliseconds(1000))
                .warmupTime(TimeValue.milliseconds(1000))
                .forks(1) // CITE: costa_2021
                .threads(1)
                .shouldDoGC(true)
                .shouldFailOnError(true)
                .resultFormat(ResultFormatType.JSON)
                .result("./benchmark-results/" + benchmarkFileName)
                .shouldFailOnError(true)
                .jvmArgs("-server", "-Xms6g", "-Xmx6g")
//                .jvmArgs("-server", "-Xms8g", "-Xmx8g", "-Dspring.config.location=classpath:/application-postgres.properties")
                .build();

        new Runner(opt).run();
    }

}
