package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.config;

import org.yaml.snakeyaml.Yaml;

public final class BenchmarkEnvironmentConfigUtils {

    private BenchmarkEnvironmentConfigUtils() {}

    public static BenchmarkEnvironmentConfig getBenchmarksSetupConfig() {
        String benchmarkEnvironment = System.getProperty("spring.profiles.active", "dev");
        Yaml yaml = new Yaml();
        BenchmarkEnvironmentConfig config = yaml.loadAs(readBenchmarkEnvironmentProperties(benchmarkEnvironment), BenchmarkEnvironmentConfig.class);
        config.setEnvironment(benchmarkEnvironment);
        return config;
    }

    private static String readBenchmarkEnvironmentProperties(String environment) {
        return ResourceUtils.readFileContents("environment-" + environment + ".yaml");
    }

}
