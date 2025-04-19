package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class PostgresBenchmarkContainerTest {
    private PostgresBenchmarkContainer container;

    @BeforeEach
    void setUp() {
        container = new PostgresBenchmarkContainer();
        container.start();
    }

    @AfterEach
    void tearDown() {
        container.stop();
    }

    @Test
    void start() {
        // container starts
    }
}
