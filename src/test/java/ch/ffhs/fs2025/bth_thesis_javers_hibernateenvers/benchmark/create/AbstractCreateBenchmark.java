package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.create;


import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.ThreadBenchmarkBase;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config.Scenario;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Thread;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;

public abstract class AbstractCreateBenchmark<T extends Thread<?>> extends ThreadBenchmarkBase<T> {

    protected Scenario getScenario() {
        return Scenario.CREATE;
    }

    protected void repeatedSetupRoutine(int i) {
        var thread = getTestObject();
        addTestObject(thread);
    }

    @Benchmark
    public void persist(Blackhole blackhole) {
        var saved = getRepository().save(nextTestObject());

        blackhole.consume(saved);
    }
}
