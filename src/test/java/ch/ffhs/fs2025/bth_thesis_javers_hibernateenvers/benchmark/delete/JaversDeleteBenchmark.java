package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.delete;


import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.PayloadType;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.model.JaversThread;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.repository.JaversThreadRepository;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class JaversDeleteBenchmark extends AbstractDeleteBenchmark<JaversThread, JaversThreadRepository> {

    @Override
    protected int getTestObjectCount() {
        int baseline = 45000;

        boolean hasAttachment = getPayloadType().equals(PayloadType.EXTENDED);
        int divisor = switch (getObjectGraphComplexity()) { // finetuning for heap usage optimization
            case SINGLE -> hasAttachment ? 2 : 1;
            case MEDIUM -> hasAttachment ? 3 : 1;
            case HIGH -> hasAttachment ? 12 : 4;
        };
        return baseline / divisor;
    }

    protected Class<JaversThread> getTestObjectClass() {
        return JaversThread.class;
    }

    protected Class<JaversThreadRepository> getRepositoryClass() {
        return JaversThreadRepository.class;
    }

}
