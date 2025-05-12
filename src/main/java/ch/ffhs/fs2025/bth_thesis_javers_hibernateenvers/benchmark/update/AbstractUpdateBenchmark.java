package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.update;


import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.ThreadBenchmarkBase;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config.CrudOperation;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config.RepeatedRunnable;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Thread;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.DataUpdater;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;


public abstract class AbstractUpdateBenchmark<T extends Thread<?>> extends ThreadBenchmarkBase<T> {

    private DataUpdater dataUpdater;

    @Override
    protected void beforeSetupRoutine() {
        dataUpdater = getBean(DataUpdater.class);
    }

    @Override
    protected CrudOperation getCrudOperation() {
        return CrudOperation.UPDATE;
    }

    @Override
    protected RepeatedRunnable getSetupRoutine() {
        return new RepeatedRunnable(() -> {
            var thread = getTestObject();
            addTestObject(thread);
            getRepository().save(thread);
            dataUpdater.update(thread, getPayloadType());
        });
    }

    @Benchmark
    public void update(Blackhole blackhole) {
        var updated = getRepository().save(nextTestObject());

        blackhole.consume(updated);
    }
}
