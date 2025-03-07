package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.config;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BenchmarkEnvironmentConfigUtilsTest {

    @Test
    void getBenchmarksConfig_test() {
        var config = BenchmarkEnvironmentConfigUtils.getBenchmarksSetupConfig();
        assertThat(config).isNotNull();
    }
}
