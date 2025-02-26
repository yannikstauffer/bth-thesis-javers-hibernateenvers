package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.BthThesisJaversHibernateenversApplication;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Thread;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.springframework.boot.SpringApplication;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class ThreadBenchmarkBase<T extends Thread<?>, R extends CrudRepository<T, Integer>> extends JmhBenchmarkBase {

    protected R repository;

    protected List<T> threads = new ArrayList<>();
    protected int pointer = 0;

    @Setup
    public void setup() {
        this.context = new SpringApplication(BthThesisJaversHibernateenversApplication.class).run();
        repository = this.context.getBean(getRepositoryClass());

        for (int i = 0; i < initObjectCount(); i++) {
            repeatedSetupRoutine(i);
        }
        afterSetup();
        System.out.println("Benchmark setup finished. Total items created: " + threads.size());
    }

    protected int initObjectCount() {
        return 1000000;
    }

    protected abstract T getThread();

    protected abstract Class<R> getRepositoryClass();

    protected abstract void repeatedSetupRoutine(int i);

    protected void afterSetup() {
        EntityManager entityManager = context.getBean(EntityManager.class);
        entityManager.clear();
    }

    @TearDown
    public void tearDown() {
        if (threads.size() < pointer) {
            throw new IllegalStateException("Benchmark failed. There were not enough items staged. Total items created: " + threads.size() + ". Items processed: " + pointer);
        }
        System.out.println("Benchmark finished. Total items created: " + threads.size() + ". Items processed: " + pointer);
    }

}
