package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.read;


import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.ThreadBenchmarkBase;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config.Scenario;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config.SetupRoutine;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Thread;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

public abstract class AbstractReadBenchmark<T extends Thread<?>> extends ThreadBenchmarkBase<T> {

    @Override
    protected Scenario getScenario() {
        return Scenario.READ;
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
    public void find(Blackhole blackhole) {
        var found = getRepository().findById(nextTestObjectPointer());
        blackhole.consume(found);
    }
}
