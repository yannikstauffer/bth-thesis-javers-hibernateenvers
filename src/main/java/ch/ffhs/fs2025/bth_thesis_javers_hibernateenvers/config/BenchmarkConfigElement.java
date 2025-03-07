package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.config;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class BenchmarkConfigElement {
    private String scenario;
    private String benchmark;
    private String versioning;
    private String complexity;
    private String payloadType;
    private int currentAmount;
    private int targetAmount;
    private LocalDateTime timestamp;

    public List<String> getYamlKeyPath() {
        return List.of("benchmark", scenario, versioning, complexity, "objects", payloadType);
    }

    public String getYamlKey() {
        return String.format("benchmark.%s.%s.%s.objects.%s", scenario, versioning, complexity, payloadType);
    }

    public Map<String, Object> getYamlValues() {
        return Map.of(
                "currentAmount", currentAmount,
                "targetAmount", targetAmount,
                "timestamp", timestamp.toString());
    }
}
