package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.config;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config.Scenario;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config.Versioning;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.ObjectGraphComplexity;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.PayloadType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BenchmarkRunConfigDto {
    private Scenario scenario;
    private Versioning versioning;
    private ObjectGraphComplexity complexity;
    private PayloadType payloadType;

    public BenchmarkRunConfigDto copy() {
        return new BenchmarkRunConfigDto(scenario, versioning, complexity, payloadType);
    }

    public String getBenchmarkClassName() {
        return toUpperCamelCase(versioning.name(), scenario.name(), "Benchmark");
    }

    public String getBenchmarkYamlKey() {
        return Stream.of(scenario.name(), versioning.name(), complexity.name(), "objects", payloadType.name())
                .map(String::toLowerCase)
                .reduce((a, b) -> a + "." + b)
                .orElseThrow();
    }

    private static String toUpperCamelCase(String... parts) {
        return String.join("", Stream.of(parts)
                .map(part -> part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase())
                .toArray(String[]::new));
    }

}
