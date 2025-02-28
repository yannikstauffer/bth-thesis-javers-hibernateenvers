package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.read;


import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.model.EnversThread;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.repository.EnversThreadRepository;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class EnversReadBenchmark extends AbstractReadBenchmark<EnversThread, EnversThreadRepository> {

    @Override
    protected int getTestObjectCount() {
        int baseline = 1300000;
        int divisor = switch (getObjectGraphComplexity()) {
            case SINGLE, MEDIUM, HIGH -> 1;
        };
        return baseline / divisor;
    }

    protected Class<EnversThread> getTestObjectClass() {
        return EnversThread.class;
    }

    protected Class<EnversThreadRepository> getRepositoryClass() {
        return EnversThreadRepository.class;
    }

}
