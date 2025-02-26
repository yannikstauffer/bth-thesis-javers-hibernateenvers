package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.update;


import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.ThreadBenchmarkBase;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Thread;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.infra.Blackhole;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public abstract class AbstractUpdateBenchmark<T extends Thread<?>, R extends CrudRepository<T, Integer>> extends ThreadBenchmarkBase<T, R> {

    @Override
    protected int initObjectCount() {
        return 500000;
    }

    protected void repeatedSetupRoutine(int i) {
        var thread = getThread();
        thread.setContent("TestThread");
        var updated = getRepository().save(thread);
        updated.setContent("UpdatedThread_" + UUID.randomUUID());
        updated.setAttachment(new byte[1000]);
        addTestObject(updated);
    }


    @Benchmark
    public void update(Blackhole blackhole) {
        var updated = getRepository().save(nextTestObject());

        blackhole.consume(updated);
    }
}
