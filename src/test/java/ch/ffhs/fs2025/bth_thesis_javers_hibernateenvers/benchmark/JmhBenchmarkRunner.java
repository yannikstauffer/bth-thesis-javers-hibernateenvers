package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.config.BenchmarkEnvironmentConfig;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.config.BenchmarkEnvironmentConfigUtils;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.config.BenchmarkRunConfigDto;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.config.ResourceUtils;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Fail.fail;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class JmhBenchmarkRunner {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

    private static final String BENCHMARK_DIRECTORY = "./benchmark-results/" + LocalDateTime.now().format(DATE_TIME_FORMATTER);

    private static BenchmarkEnvironmentConfig benchmarksConfig;

    @BeforeAll
    static void init() {
        File directory = new File(BENCHMARK_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        benchmarksConfig = BenchmarkEnvironmentConfigUtils.getBenchmarksSetupConfig();
        System.out.println("Benchmark directory: " + BENCHMARK_DIRECTORY);
    }

    @AfterAll
    @SneakyThrows
    static void cleanUp() {
        String applicationYaml = "application-" + benchmarksConfig.getEnvironment() + ".yaml";
        Path applicationYamlPath = ResourceUtils.getPath("application-" + benchmarksConfig.getEnvironment() + ".yaml");

        if (ResourceUtils.fileExists(applicationYaml)) {
            Files.copy(applicationYamlPath, Path.of(BENCHMARK_DIRECTORY + "/application-optimized.yaml"));
        } else {
            fail("Expected optimization file missing.");
        }
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void executeJmhRunner(BenchmarkRunConfigDto runConfigDto) {

        String benchmarkFileName = String.join("_",
                        runConfigDto.getBenchmarkClassName(),
                        runConfigDto.getComplexity().name(),
                        runConfigDto.getPayloadType().name())
                .concat(".json");

        Options opt = new OptionsBuilder()
                .include("\\." + runConfigDto.getBenchmarkClassName() + "\\.")
                .warmupIterations(benchmarksConfig.getJmhConfig().getWarmupIterations()) // CITE: traini_2023
                .measurementIterations(benchmarksConfig.getJmhConfig().getMeasurementIterations())
                .measurementTime(TimeValue.milliseconds(benchmarksConfig.getJmhConfig().getMeasurementTime()))
                .warmupTime(TimeValue.milliseconds(benchmarksConfig.getJmhConfig().getWarmupTime()))
                .timeout(TimeValue.minutes(5))
                .forks(1) // CITE: costa_2021
                .threads(1)
                .shouldDoGC(true)
                .shouldFailOnError(true)
                .resultFormat(ResultFormatType.JSON)
                .result(BENCHMARK_DIRECTORY + "/" + benchmarkFileName)
                .shouldFailOnError(true)
                .jvmArgs(getJvmOptions(runConfigDto))
                .build();

        Runner runner = new Runner(opt);

        assertDoesNotThrow(runner::run);
    }

    private String[] getJvmOptions(BenchmarkRunConfigDto runConfigDto) {
        Set<String> jvmOptions = new HashSet<>(benchmarksConfig.getJvmConfig().getOptions());
        jvmOptions.add("-server");
        jvmOptions.add("-Dbenchmark.config.objectGraphComplexity=" + runConfigDto.getComplexity().name());
        jvmOptions.add("-Dbenchmark.config.payloadType=" + runConfigDto.getPayloadType().name());
        jvmOptions.add("-Dspring.profiles.active=" + benchmarksConfig.getEnvironment());
        return jvmOptions.toArray(new String[0]);
    }

    private static Stream<Arguments> provideParameters() {
        return benchmarksConfig.getBenchmarkRunConfigs()
                .map(runConfigDto -> Arguments.of(runConfigDto, benchmarksConfig.getJmhConfig()));
    }

}
