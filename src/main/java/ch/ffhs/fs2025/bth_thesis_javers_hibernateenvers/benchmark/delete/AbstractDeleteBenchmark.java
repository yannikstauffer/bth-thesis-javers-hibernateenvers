package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.delete;


import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.ThreadBenchmarkBase;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config.Scenario;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config.SetupRoutine;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Thread;
import org.openjdk.jmh.annotations.Benchmark;

public abstract class AbstractDeleteBenchmark<T extends Thread<?>> extends ThreadBenchmarkBase<T> {

    @Override
    protected Scenario getScenario() {
        return Scenario.DELETE;
    }

    @Override
    protected SetupRoutine<T> getSetupRoutine() {
        return SetupRoutine.<T>builder()
                .preSaveSetupRoutine(() -> {
                    var thread = getTestObject();
                    addTestObject(thread);
                })
                .saveSetup(true)
                .build();
    }

    @Benchmark
    public void delete() {
        getRepository().delete(nextTestObject());
    }
}
