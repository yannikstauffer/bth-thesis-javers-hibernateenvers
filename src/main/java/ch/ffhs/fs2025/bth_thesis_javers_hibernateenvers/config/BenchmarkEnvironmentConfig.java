package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class BenchmarkEnvironmentConfig {

    private JmhConfig jmhConfig;
    private RunConfig runConfig;
    private String environment;

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
}
