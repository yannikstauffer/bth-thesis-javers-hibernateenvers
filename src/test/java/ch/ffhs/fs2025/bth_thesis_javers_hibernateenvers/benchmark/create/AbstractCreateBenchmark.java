package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.create;


import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.ThreadBenchmarkBase;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Thread;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;
import org.springframework.data.repository.CrudRepository;

public abstract class AbstractCreateBenchmark<T extends Thread<?>, R extends CrudRepository<T, Integer>> extends ThreadBenchmarkBase<T, R> {

    protected void repeatedSetupRoutine(int i) {
        var thread = getTestObject();
        thread.setContent("TestThread");
        addTestObject(thread);
    }

    @Benchmark
    public void persist(Blackhole blackhole) {
        var saved = getRepository().save(nextTestObject());

        blackhole.consume(saved);
    }
}
