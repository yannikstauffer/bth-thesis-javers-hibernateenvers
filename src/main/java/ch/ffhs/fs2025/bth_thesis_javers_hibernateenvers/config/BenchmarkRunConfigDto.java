package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.config;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config.CrudOperation;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config.Versioning;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.ObjectGraphSize;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.PayloadType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BenchmarkRunConfigDto {
    private CrudOperation crudOperation;
    private Versioning versioning;
    private ObjectGraphSize complexity;
    private PayloadType payloadType;

    public BenchmarkRunConfigDto copy() {
        return new BenchmarkRunConfigDto(crudOperation, versioning, complexity, payloadType);
    }

    public String getBenchmarkClassName() {
        return toUpperCamelCase(versioning.name(), crudOperation.name(), "Benchmark");
    }

    public String getBenchmarkYamlKey() {
        return Stream.of(
                        crudOperation.name(),
                        complexity.name(),
                        BenchmarkEnvironmentConfig.OBJECTS_KEY,
                        payloadType.name())
                .map(String::toLowerCase)
                .reduce((a, b) -> a + "." + b)
                .orElseThrow();
    }

    public String getBenchmarkIdentifier() {
        return String.join("_",
                getBenchmarkClassName(),
                getComplexity().name(),
                getPayloadType().name());
    }

    private static String toUpperCamelCase(String... parts) {
        return String.join("", Stream.of(parts)
                .map(part -> part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase())
                .toArray(String[]::new));
    }

}
