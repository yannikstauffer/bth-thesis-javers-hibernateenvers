package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.update;


import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.ThreadBenchmarkBase;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config.Scenario;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config.SetupRoutine;
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
    protected Scenario getScenario() {
        return Scenario.UPDATE;
    }

    @Override
    protected SetupRoutine<T> getSetupRoutine() {
        return SetupRoutine.<T>builder()
                .preSaveSetupRoutine(() -> {
                    var thread = getRepository().save(getTestObject());
                    addTestObject(thread);
                })
                .saveSetup(true)
                .postSaveSetupRoutine(thread -> dataUpdater.update(thread, getPayloadType()))
                .build();
    }

    @Benchmark
    public void update(Blackhole blackhole) {
        var updated = getRepository().save(nextTestObject());

        blackhole.consume(updated);
    }
}
