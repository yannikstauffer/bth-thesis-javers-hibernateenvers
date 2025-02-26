package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.delete;


import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.ThreadBenchmarkBase;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Thread;
import org.openjdk.jmh.annotations.Benchmark;
import org.springframework.data.repository.CrudRepository;

public abstract class AbstractDeleteBenchmark<T extends Thread<?>, R extends CrudRepository<T, Integer>> extends ThreadBenchmarkBase<T, R> {

    @Override
    protected int initObjectCount() {
        return 500000;
    }

    protected void repeatedSetupRoutine(int i) {
        var thread = getThread();
        thread.setContent("TestThread");
        var created = getRepository().save(thread);
        addTestObject(created);
    }

    @Benchmark
    public void delete() {
        getRepository().delete(nextTestObject());
    }
}
