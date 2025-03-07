package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.config;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class BenchmarkConfigManagerTest {

    static final String BENCHMARK_CONFIG_FILE = "benchmarkconfigmanagertest.yaml";
    static final LocalDateTime MOCKED_NOW_TIMESTAMP = LocalDateTime.of(2000, 1, 1, 0, 0);
    static final String BENCHMARK_CONFIG_FILE_COPY = "benchmarkconfigmanagertest." + MOCKED_NOW_TIMESTAMP.format(BenchmarkConfigManager.FORMATTER) + ".yaml";

    MockedStatic<LocalDateTime> localDateTimeMockedStatic = mockStatic(LocalDateTime.class);

    @BeforeEach
    void setUp() {
        localDateTimeMockedStatic.when(LocalDateTime::now).thenReturn(MOCKED_NOW_TIMESTAMP);
        ResourceUtils.delete(BENCHMARK_CONFIG_FILE);
        ResourceUtils.delete(BENCHMARK_CONFIG_FILE_COPY);

        ResourceUtils.copy(ResourceUtilsTest.TESTING_FILE, BENCHMARK_CONFIG_FILE);
    }

    @AfterEach
    void tearDown() {
        ResourceUtils.delete(BENCHMARK_CONFIG_FILE);
        ResourceUtils.delete(BENCHMARK_CONFIG_FILE_COPY);
        localDateTimeMockedStatic.close();
        if(!ResourceUtils.fileExists(ResourceUtilsTest.TESTING_FILE)) {
            fail("You have deleted the testing file. That was not supposed to happen.");
        }
    }

    @Test
    void constructor_createsBackup() {
        new BenchmarkConfigManager(BENCHMARK_CONFIG_FILE);

        assertThat(new ClassPathResource((BENCHMARK_CONFIG_FILE_COPY)).exists()).isTrue();
    }

    @Test
    void save() {
        BenchmarkConfigManager benchmarkConfigManager = new BenchmarkConfigManager(BENCHMARK_CONFIG_FILE);
        ResourceUtils.delete(BENCHMARK_CONFIG_FILE);

        benchmarkConfigManager.save();

        assertThat(ResourceUtils.fileExists(BENCHMARK_CONFIG_FILE)).isTrue();
    }

    @Test
    void upsertEntry_addsNew() {
        BenchmarkConfigElement benchmarkConfigElement = getNewBenchmarkConfigElement();

        BenchmarkConfigManager benchmarkConfigManager = new BenchmarkConfigManager(BENCHMARK_CONFIG_FILE);
        benchmarkConfigManager.upsertEntry(benchmarkConfigElement);

        benchmarkConfigManager.save();

        Map<String, Object> yamlData = YamlUtils.ofFile(BENCHMARK_CONFIG_FILE);

        assertYamlContains(yamlData, benchmarkConfigElement);
    }

    @Test
    void upsertEntry_updatesExisting() {
        BenchmarkConfigElement benchmarkConfigElement = getNewBenchmarkConfigElement();

        BenchmarkConfigManager benchmarkConfigManager = new BenchmarkConfigManager(BENCHMARK_CONFIG_FILE);
        benchmarkConfigManager.upsertEntry(benchmarkConfigElement);
        benchmarkConfigManager.save();

        BenchmarkConfigElement updatedBenchmarkConfigElement = getUpdatedBenchmarkConfigElement();
        benchmarkConfigManager.upsertEntry(updatedBenchmarkConfigElement);
        benchmarkConfigManager.save();

        Map<String, Object> yamlData = YamlUtils.ofFile(BENCHMARK_CONFIG_FILE);

        assertYamlContains(yamlData, updatedBenchmarkConfigElement);
    }

    private void assertYamlContains(Map<String, Object> yamlData, BenchmarkConfigElement benchmarkConfigElement) {
        Map<String, Object> currentMap = yamlData;

        StringBuilder keyPath = new StringBuilder();
        for (String key : benchmarkConfigElement.getYamlKeyPath()) {
            keyPath.append(".").append(key);

            assertThat(currentMap)
                    .as("Key " + keyPath + " not found in YAML.")
                    .containsKey(key);

            currentMap = (Map<String, Object>) currentMap.get(key);
        }

        for(String key : benchmarkConfigElement.getYamlValues().keySet()) {
            String valuePath = keyPath + "." + key;
            String value = benchmarkConfigElement.getYamlValues().get(key).toString();
            assertThat(currentMap)
                    .as("Expecting " + valuePath + " to contain " + value + " in YAML.")
                    .containsEntry(key, benchmarkConfigElement.getYamlValues().get(key));
        }
    }

    private BenchmarkConfigElement getNewBenchmarkConfigElement() {
        return BenchmarkConfigElement.builder()
                .scenario("scenario")
                .benchmark("benchmark")
                .versioning("versioning")
                .complexity("complexity")
                .payloadType("payloadType")
                .currentAmount(1)
                .targetAmount(2)
                .timestamp(MOCKED_NOW_TIMESTAMP)
                .build();
    }

    private BenchmarkConfigElement getUpdatedBenchmarkConfigElement() {
        BenchmarkConfigElement benchmarkConfigElement = getNewBenchmarkConfigElement();

        benchmarkConfigElement.setCurrentAmount(3);
        benchmarkConfigElement.setTargetAmount(4);
        benchmarkConfigElement.setTimestamp(MOCKED_NOW_TIMESTAMP.plusDays(1));

        return benchmarkConfigElement;
    }

}
