package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.create;


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
public class JaversCreateBenchmark extends AbstractCreateBenchmark<JaversThread, JaversThreadRepository> {

    @Override
    protected int getTestObjectCount() {
        int baseline = 80000;

        boolean hasAttachment = getPayloadType().equals(PayloadType.EXTENDED);
        int divisor = switch (getObjectGraphComplexity()) { // finetuning for heap usage optimization
            case SINGLE -> hasAttachment ? 13: 1;
            case MEDIUM -> hasAttachment ? 50 : 3;
            case HIGH -> hasAttachment ? 400 : 20;
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
