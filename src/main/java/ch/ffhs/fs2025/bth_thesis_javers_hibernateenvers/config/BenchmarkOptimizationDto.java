package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.config;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BenchmarkOptimizationDto {
    private String crudOperation;
    private String versioning;
    private String objectGraphSize;
    private String payloadType;
    private int objectCount;
    private LocalDateTime timestamp;

    public void setObjectCount(double targetAmount) {
        this.objectCount = (int) targetAmount;
    }

    public List<String> getYamlKeyPath() {
        return List.of(
                BenchmarkEnvironmentConfig.USECASE_KEY,
                crudOperation,
                objectGraphSize,
                BenchmarkEnvironmentConfig.OBJECTS_KEY,
                payloadType);
    }

}
