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
    BenchmarkConfigManager benchmarkConfigManager;

    @BeforeEach
    void setUp() {
        localDateTimeMockedStatic.when(LocalDateTime::now).thenReturn(MOCKED_NOW_TIMESTAMP);
        ResourceUtils.delete(BENCHMARK_CONFIG_FILE);
        ResourceUtils.delete(BENCHMARK_CONFIG_FILE_COPY);

        ResourceUtils.copy(ResourceUtilsTest.TESTING_FILE, BENCHMARK_CONFIG_FILE);
        benchmarkConfigManager = new BenchmarkConfigManager(BENCHMARK_CONFIG_FILE);
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
        assertThat(new ClassPathResource((BENCHMARK_CONFIG_FILE_COPY)).exists()).isTrue();
    }

    @Test
    void save() {
        ResourceUtils.delete(BENCHMARK_CONFIG_FILE);

        benchmarkConfigManager.save();

        assertThat(ResourceUtils.fileExists(BENCHMARK_CONFIG_FILE)).isTrue();
    }

    @Test
    void upsertEntry_addsNew() {
        BenchmarkOptimizationDto optimizationDto = getNewOptimizationDto();

        benchmarkConfigManager.upsertEntry(optimizationDto);
        benchmarkConfigManager.save();

        Map<String, Object> yamlData = YamlUtils.ofFile(BENCHMARK_CONFIG_FILE);

        assertYamlContains(yamlData, optimizationDto);
    }

    @Test
    void upsertEntry_updatesExisting() {
        BenchmarkOptimizationDto optimizationDto = getExistingOptimizationDto();
        optimizationDto.setObjectCount(321);

        benchmarkConfigManager.upsertEntry(optimizationDto);
        benchmarkConfigManager.save();

        Map<String, Object> yamlData = YamlUtils.ofFile(BENCHMARK_CONFIG_FILE);

        assertYamlContains(yamlData, optimizationDto);
    }

    private void assertYamlContains(Map<String, Object> yamlData, BenchmarkOptimizationDto optimizationDto) {
        Map<String, Object> currentMap = yamlData;

        assertThat(currentMap)
                .as("Expecting YAML to contain root keys benchmark, jmhConfig, jvmConfig, runConfig")
                .containsOnlyKeys("benchmark", "jmhConfig", "jvmConfig", "runConfig");

        StringBuilder keyPath = new StringBuilder();
        for (int i = 0; i < optimizationDto.getYamlKeyPath().size(); i++) {
            String key = optimizationDto.getYamlKeyPath().get(i);
            keyPath.append(".").append(key);

            var value = currentMap.get(key);
            if(i == optimizationDto.getYamlKeyPath().size() -1) {
                assertThat(value)
                        .as("Expecting " + keyPath + " to contain value of " + optimizationDto.getObjectCount())
                        .isEqualTo(optimizationDto.getObjectCount());
            } else {
                assertThat(currentMap)
                        .as("Key " + keyPath + " not found in YAML.")
                        .containsKey(key);
                currentMap = (Map<String, Object>) currentMap.get(key);
            }
        }
    }

    private BenchmarkOptimizationDto getExistingOptimizationDto() {
        return BenchmarkOptimizationDto.builder()
                .scenario("create")
                .versioning("novers")
                .complexity("single")
                .payloadType("basic")
                .build();
    }

    private BenchmarkOptimizationDto getNewOptimizationDto() {
        return BenchmarkOptimizationDto.builder()
                .scenario("scenario")
                .versioning("versioning")
                .complexity("complexity")
                .payloadType("payloadType")
                .objectCount(2)
                .build();
    }
}
