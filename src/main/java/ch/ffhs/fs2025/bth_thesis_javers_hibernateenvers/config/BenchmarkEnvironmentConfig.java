package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.config;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config.CrudOperation;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config.Versioning;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.ObjectGraphComplexity;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.PayloadType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@FieldNameConstants(level = AccessLevel.PRIVATE)
public class BenchmarkEnvironmentConfig {

    private JmhConfig jmhConfig;
    private RunConfig runConfig;
    private JvmConfig jvmConfig;
    private String environment;
    private Map<String, Object> usecase;

    public static final String OBJECTS_KEY = "objects";
    public static final String USECASE_KEY = BenchmarkEnvironmentConfig.Fields.usecase;

    public Stream<BenchmarkRunConfigDto> getBenchmarkRunConfigs() {
        List<BenchmarkRunConfigDto> runConfigDtos = apply(List.of(new BenchmarkRunConfigDto()),
                runConfig.getVersionings(),
                BenchmarkRunConfigDto::setVersioning,
                Versioning.class);
        runConfigDtos = apply(runConfigDtos,
                runConfig.getCrudOperations(),
                BenchmarkRunConfigDto::setCrudOperation,
                CrudOperation.class);
        runConfigDtos = apply(runConfigDtos,
                runConfig.getObjectGraphComplexities(),
                BenchmarkRunConfigDto::setComplexity,
                ObjectGraphComplexity.class);
        runConfigDtos = apply(runConfigDtos,
                runConfig.getPayloadTypes(),
                BenchmarkRunConfigDto::setPayloadType,
                PayloadType.class);

        return runConfigDtos.stream().filter(optimizerFilter());
    }

    /**
     * Run benchmarks only for {@link Versioning#NOVERS} and not optimized
     */
    private Predicate<BenchmarkRunConfigDto> optimizerFilter() {
        return dto -> {
            if(!runConfig.isOptimizeOnly()) {
                return true;
            }
            boolean isNovers = dto.getVersioning() == Versioning.NOVERS;
            boolean isOptimized = Boolean.parseBoolean(YamlUtils.readKey(usecase, dto.getBenchmarkYamlKey() + "Optimized"));
            return isNovers && !isOptimized;
        };
    }

    private static <E extends Enum<E>> List<BenchmarkRunConfigDto> apply(List<BenchmarkRunConfigDto> benchmarkConfigDtos,
                                                                         Set<String> values,
                                                                         BiConsumer<BenchmarkRunConfigDto, E> setter,
                                                                         Class<E> enumClazz) {
        List<BenchmarkRunConfigDto> result = new ArrayList<>();
        for (BenchmarkRunConfigDto dto : benchmarkConfigDtos) {
            for (String value : values) {
                BenchmarkRunConfigDto copy = dto.copy();
                setter.accept(copy, Enum.valueOf(enumClazz, value.toUpperCase()));
                result.add(copy);
            }
        }
        return result;
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
        private boolean optimizeOnly;
        private boolean failOnTightResult;
        private Set<String> crudOperations;
        private Set<String> versionings;
        private Set<String> payloadTypes;
        private Set<String> objectGraphComplexities;
    }

    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class JvmConfig {
        private Set<String> options;
    }
}
