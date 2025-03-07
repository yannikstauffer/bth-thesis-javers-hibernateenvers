package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.config;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BenchmarkOptimizationDto {
    private String scenario;
    private String versioning;
    private String complexity;
    private String payloadType;
    private int objectCount;
    private LocalDateTime timestamp;

    public void setObjectCount(double targetAmount) {
        this.objectCount = (int) targetAmount;
    }

    public List<String> getYamlKeyPath() {
        return List.of("benchmark", scenario, versioning, complexity, "objects", payloadType);
    }

}
