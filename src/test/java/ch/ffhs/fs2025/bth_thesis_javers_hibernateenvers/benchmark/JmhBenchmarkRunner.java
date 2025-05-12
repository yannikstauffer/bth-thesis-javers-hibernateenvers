package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.config.BenchmarkEnvironmentConfig;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.config.BenchmarkEnvironmentConfigUtils;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.config.BenchmarkRunConfigDto;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.config.ResourceUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import static org.assertj.core.api.Fail.fail;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JmhBenchmarkRunner {

    private PostgresBenchmarkContainer postgres;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
    private static final String BENCHMARK_DIRECTORY = "./benchmark-results/" + LocalDateTime.now().format(DATE_TIME_FORMATTER);
    private static BenchmarkEnvironmentConfig benchmarksConfig;

    private static final AtomicBoolean hasFailed = new AtomicBoolean(false);

    @BeforeAll
    static void init() {
        File directory = new File(BENCHMARK_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        benchmarksConfig = BenchmarkEnvironmentConfigUtils.getBenchmarksSetupConfig();

        if (benchmarksConfig.getRunConfig().isOptimizeOnly()) {
            printOptimizeOnlyMessage();
        }

        System.out.println("Benchmark directory: " + BENCHMARK_DIRECTORY);
    }

    static void printOptimizeOnlyMessage() {
        System.out.println();
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("!!! Running in optimizeOnly mode. Only Novers benchmarks will be executed. !!!");
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println();
    }

    @BeforeEach
    void setUp() {
        postgres = new PostgresBenchmarkContainer();
        postgres.start();
    }

    @AfterEach
    void stopContainer() {
        postgres.stop();
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
        failIfPreviousFailed();

        Options opt = getRunnerOptions(runConfigDto);

        Runner runner = new Runner(opt);

        try {
            var results = runner.run();

            write95CItoJson(results, getJsonFilePath(runConfigDto));

        } catch (Exception e) {
            if (benchmarksConfig.getRunConfig().isOptimizeOnly()) {
                fail("Failing parameterized test to trigger optimization: " + runConfigDto.getBenchmarkIdentifier());
            } else {
                System.err.println("Aborting. Benchmark failed: " + runConfigDto.getBenchmarkIdentifier());
                hasFailed.set(true);
            }
        }
    }

    private void write95CItoJson(Collection<RunResult> results, String jsonFilePath) {
        if (results.size() != 1) {
            fail("Expecting exactly one benchmark result. Cannot calculate 95% CI.");
        }

        results.forEach(result -> {
            double[] ci = result.getPrimaryResult().getStatistics().getConfidenceIntervalAt(0.95);
            System.out.println("95% CI: " + Arrays.toString(ci));
            System.out.println();

            Path jsonPath = Paths.get(jsonFilePath);

            try {
                ObjectMapper mapper = new ObjectMapper();
                ArrayNode root = (ArrayNode) mapper.readTree(jsonPath.toFile());

                for (JsonNode benchmarkResult : root) {
                    JsonNode primaryMetric = benchmarkResult.get("primaryMetric");
                    if (primaryMetric != null && primaryMetric.isObject()) {
                        ((ObjectNode) primaryMetric).putArray("CI_95")
                                .add(ci[0])
                                .add(ci[1]);
                    }
                }

                mapper.writerWithDefaultPrettyPrinter().writeValue(jsonPath.toFile(), root);

            } catch (IOException e) {
                throw new RuntimeException("Fehler beim Schreiben des CI ins JSON", e);
            }
        });
    }

    private String getJsonFilePath(BenchmarkRunConfigDto runConfigDto) {
        String benchmarkFileName = runConfigDto.getBenchmarkIdentifier()
                .concat(".json");

        return BENCHMARK_DIRECTORY + "/" + benchmarkFileName;
    }

    private Options getRunnerOptions(BenchmarkRunConfigDto runConfigDto) {
        return new OptionsBuilder()
                .include("\\." + runConfigDto.getBenchmarkClassName() + "\\.")
                .warmupIterations(benchmarksConfig.getJmhConfig().getWarmupIterations()) // CITE: traini_2023
                .measurementIterations(benchmarksConfig.getJmhConfig().getMeasurementIterations())
                .measurementTime(TimeValue.milliseconds(benchmarksConfig.getJmhConfig().getMeasurementTime()))
                .warmupTime(TimeValue.milliseconds(benchmarksConfig.getJmhConfig().getWarmupTime()))
                .timeout(TimeValue.minutes(10))
                .forks(benchmarksConfig.getRunConfig().isOptimizeOnly() ? 1 : 5) // CITE: costa_2021
                .threads(1)
                .shouldDoGC(true)
                .shouldFailOnError(true)
                .resultFormat(ResultFormatType.JSON)
                .result(getJsonFilePath(runConfigDto))
                .jvmArgs(getJvmOptions(runConfigDto))
                .build();
    }

    private void failIfPreviousFailed() {
        Assumptions.assumeFalse(hasFailed.get(), "Aborting due to previous failure");
    }

    private String[] getJvmOptions(BenchmarkRunConfigDto runConfigDto) {
        Set<String> jvmOptions = new HashSet<>(benchmarksConfig.getJvmConfig().getOptions());
        jvmOptions.add("-server");
        jvmOptions.add("-Dbenchmark.config.objectGraphSize=" + runConfigDto.getComplexity().name());
        jvmOptions.add("-Dbenchmark.config.payloadType=" + runConfigDto.getPayloadType().name());
        jvmOptions.add("-Dspring.profiles.active=" + benchmarksConfig.getEnvironment());
        jvmOptions.add("-Dbenchmark.config.failOnTightResult=" + benchmarksConfig.getRunConfig().isFailOnTightResult());

        postgres.springBootEnvironmentProperties().forEach(prop -> jvmOptions.add("-D" + prop));

        if (benchmarksConfig.getRunConfig().isOptimizeOnly()) {
            jvmOptions.add("-Dbenchmark.config.optimizeOnly=true");
        }


        return jvmOptions.toArray(new String[0]);
    }

    private static Stream<Arguments> provideParameters() {
        return benchmarksConfig.getBenchmarkRunConfigs()
                .map(runConfigDto -> Arguments.of(runConfigDto, benchmarksConfig.getJmhConfig()));
    }

}
