package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.delete;


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
public class EnversDeleteBenchmark extends AbstractDeleteBenchmark<EnversThread, EnversThreadRepository> {

    @Override
    protected int getTestObjectCount() {
        int baseline = 300000;
        int divisor = switch (getObjectGraphComplexity()) {
            case SINGLE -> 1;
            case MEDIUM -> 3;
            case HIGH -> 15;
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
