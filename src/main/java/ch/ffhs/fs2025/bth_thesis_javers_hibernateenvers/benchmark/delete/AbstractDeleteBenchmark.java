package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.delete;


import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.ThreadBenchmarkBase;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config.CrudOperation;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config.RepeatedRunnable;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Thread;
import org.openjdk.jmh.annotations.Benchmark;

public abstract class AbstractDeleteBenchmark<T extends Thread<?>> extends ThreadBenchmarkBase<T> {

    @Override
    protected CrudOperation getCrudOperation() {
        return CrudOperation.DELETE;
    }

    @Override
    protected RepeatedRunnable getSetupRoutine() {
        return new RepeatedRunnable(() -> {
            var thread = getTestObject();
            addTestObject(thread);
            getRepository().save(thread);
        });
    }

    @Benchmark
    public void delete() {
        getRepository().delete(nextTestObject());
    }
}
