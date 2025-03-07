package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.delete;


import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.ThreadBenchmarkBase;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config.Scenario;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Thread;
import org.openjdk.jmh.annotations.Benchmark;

public abstract class AbstractDeleteBenchmark<T extends Thread<?>> extends ThreadBenchmarkBase<T> {

    @Override
    protected Scenario getScenario() {
        return Scenario.DELETE;
    }

    protected void repeatedSetupRoutine(int i) {
        var thread = getTestObject();
        var created = getRepository().save(thread);
        addTestObject(created);
    }

    @Benchmark
    public void delete() {
        getRepository().delete(nextTestObject());
    }
}
