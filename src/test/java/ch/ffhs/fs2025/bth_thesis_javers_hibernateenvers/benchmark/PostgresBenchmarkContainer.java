package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark;

import com.github.dockerjava.api.model.Capability;
import com.github.dockerjava.api.model.Ulimit;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;
import java.util.UUID;

public class PostgresBenchmarkContainer extends PostgreSQLContainer<PostgresBenchmarkContainer> {

    private static final String IMAGE_VERSION = "postgres:17.4-alpine3.21";
    private static final long GIGABYTE = 1024 * 1024 * 1024L;

    public PostgresBenchmarkContainer() {
        super(IMAGE_VERSION);
        this.withDatabaseName("benchmark")
                .withUsername("benchmark")
                .withPassword("benchmark")
                .withCreateContainerCmdModifier(cmd ->
                        cmd.withName("benchmark-db-" + UUID.randomUUID())
                                .getHostConfig()
                                .withMemory(4 * GIGABYTE)
                                .withMemoryReservation(4 * GIGABYTE)
                                .withCpuCount(1L)
                                .withCpusetCpus("4,5") // ensure that these are not the same as benchmark-runner cpuset
                                .withCpuShares(2)
                                .withCapAdd(Capability.SYS_NICE)
                                .withUlimits(List.of(new Ulimit("rtprio", 99L, 99L)))
                );

    }

    public List<String> springBootEnvironmentProperties() {
        return List.of(
                "spring.datasource.url=" + getJdbcUrl(),
                "spring.datasource.username=" + getUsername(),
                "spring.datasource.password=" + getPassword(),
                "spring.datasource.driver-class-name=org.postgresql.Driver",
                "spring.jpa.hibernate.ddl-auto=create",
                "spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect"
        );
    }


}
