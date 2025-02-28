package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.update;


import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.model.NoversThread;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.repository.NoversThreadRepository;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class NoversUpdateBenchmark extends AbstractUpdateBenchmark<NoversThread, NoversThreadRepository> {

    @Override
    protected int getTestObjectCount() {
        int baseline = 350000;
        int divisor = switch (getObjectGraphComplexity()) {
            case SINGLE -> 1;
            case MEDIUM -> 2;
            case HIGH -> 10;
        };
        return baseline / divisor;
    }

    protected Class<NoversThread> getTestObjectClass() {
        return NoversThread.class;
    }

    protected Class<NoversThreadRepository> getRepositoryClass() {
        return NoversThreadRepository.class;
    }

}
