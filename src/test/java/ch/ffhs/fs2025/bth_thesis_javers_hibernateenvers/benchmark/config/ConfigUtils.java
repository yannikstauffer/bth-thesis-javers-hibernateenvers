package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.yaml.snakeyaml.Yaml;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigUtils {

    public BenchmarksConfig getBenchmarksConfig() {
        String benchmarkEnvironment = System.getProperty("spring.profiles.active", "dev");
        Yaml yaml = new Yaml();
        BenchmarksConfig config = yaml.loadAs(readBenchmarkEnvironmentProperties(benchmarkEnvironment), BenchmarksConfig.class);
        config.setEnvironment(benchmarkEnvironment);
        return config;
    }

    private static String readBenchmarkEnvironmentProperties(String environment) {
        return readFileContents("environment-" + environment + ".yaml");
    }

    @SneakyThrows
    private static String readFileContents(String path) {
        ClassPathResource resource = new ClassPathResource(path);
        return Files.readString(resource.getFile().toPath(), StandardCharsets.UTF_8);
    }

    @Test
    void getBenchmarksConfig_test() {
        var config = getBenchmarksConfig();
        assertThat(config).isNotNull();
    }

    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class JmhConfig {
        private int warmupIterations;
        private int warmupTime;
        private int measurementIterations;
        private int measurementTime;
    }

    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class RunConfig {
        private Set<String> scenarios;
        private Set<String> versionings;
    }

    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class BenchmarksConfig {
        private JmhConfig jmhConfig;
        private RunConfig runConfig;
        private String environment;
    }

}
