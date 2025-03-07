package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.config;

public final class BenchmarkEnvironmentConfigUtils {

    private BenchmarkEnvironmentConfigUtils() {}

    public static BenchmarkEnvironmentConfig getBenchmarksSetupConfig() {
        String benchmarkEnvironment = System.getProperty("spring.profiles.active", "dev");

        BenchmarkEnvironmentConfig config = YamlUtils.loadAs(readApplicationProperties(benchmarkEnvironment), BenchmarkEnvironmentConfig.class);
        config.setEnvironment(benchmarkEnvironment);
        return config;
    }

    private static String readApplicationProperties(String environment) {
        return ResourceUtils.readFileContents("application-" + environment + ".yaml");
    }

}
