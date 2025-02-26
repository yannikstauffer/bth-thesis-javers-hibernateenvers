package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.read;


import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.ThreadBenchmarkBase;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Thread;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;
import org.springframework.data.repository.CrudRepository;

public abstract class AbstractReadBenchmark<T extends Thread<?>, R extends CrudRepository<T, Integer>> extends ThreadBenchmarkBase<T, R> {

    @Override
    protected int initObjectCount() {
        return 1500000;
    }

    protected void repeatedSetupRoutine(int i) {
        var thread = getThread();
        thread.setContent("TestThread");
        var created = getRepository().save(thread);

        addTestObject(created);
    }

    @Benchmark
    public void find(Blackhole blackhole) {
        var found = getRepository().findById(nextTestObjectPointer());
        blackhole.consume(found);
    }
}
