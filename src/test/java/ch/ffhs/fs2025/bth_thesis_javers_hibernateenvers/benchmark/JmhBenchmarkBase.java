package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.ObjectGraphComplexity;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.PayloadType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

public abstract class JmhBenchmarkBase {

    private static final Integer MEASUREMENT_ITERATIONS = 5;
    private static final Integer WARMUP_ITERATIONS = 5;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    @ParameterizedTest
    @MethodSource("provideParameters")
    public void executeJmhRunner(ObjectGraphComplexity objectGraphComplexity, PayloadType payloadType) throws RunnerException {

        String benchmarkClassName = this.getClass().getSimpleName();
        String benchmarkFileName = String.join("_",
                        LocalDateTime.now().format(DATE_TIME_FORMATTER),
                        benchmarkClassName,
                        objectGraphComplexity.name(),
                        payloadType.name())
                .concat(".json");

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
                .jvmArgs("-server", "-Xms6g", "-Xmx6g",
                        "-Dbenchmark.config.objectGraphComplexity=" + objectGraphComplexity.name(),
                        "-Dbenchmark.config.payloadType=" + payloadType.name())
//                .jvmArgs("-server", "-Xms8g", "-Xmx8g", "-Dspring.config.location=classpath:/application-postgres.properties")
                .build();

        Runner runner = new Runner(opt);
        runner.run();
    }

//    private static Stream<Arguments> provideParameters() {
//        return Stream.of(
////                Arguments.of(ObjectGraphComplexity.SINGLE, PayloadType.BASIC),
//                Arguments.of(ObjectGraphComplexity.SINGLE, PayloadType.EXTENDED),
////                Arguments.of(ObjectGraphComplexity.MEDIUM, PayloadType.BASIC),
//                Arguments.of(ObjectGraphComplexity.MEDIUM, PayloadType.EXTENDED),
////                Arguments.of(ObjectGraphComplexity.HIGH, PayloadType.BASIC),
//                Arguments.of(ObjectGraphComplexity.HIGH, PayloadType.EXTENDED)
//        );
//    }

    private static Stream<Arguments> provideParameters() {
        return Stream.of(ObjectGraphComplexity.values())
                .flatMap(objectGraphComplexity -> Stream.of(PayloadType.values())
                        .map(payloadType -> Arguments.of(objectGraphComplexity, payloadType)));
    }
}
