package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.create;


import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.ThreadBenchmarkBase;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config.CrudOperation;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config.RepeatedRunnable;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Thread;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

public abstract class AbstractCreateBenchmark<T extends Thread<?>> extends ThreadBenchmarkBase<T> {

    @Override
    protected CrudOperation getCrudOperation() {
        return CrudOperation.CREATE;
    }

    @Override
    protected RepeatedRunnable getSetupRoutine() {
        return new RepeatedRunnable(() -> {
            var thread = getTestObject();
            addTestObject(thread);
        });
    }

    @Benchmark
    public void persist(Blackhole blackhole) {
        var saved = getRepository().save(nextTestObject());

        blackhole.consume(saved);
    }
}
