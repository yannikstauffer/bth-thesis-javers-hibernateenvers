package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.BthThesisJaversHibernateenversApplication;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Thread;
import org.openjdk.jmh.annotations.Setup;
import org.springframework.boot.SpringApplication;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;

public abstract class ThreadBenchmarkBase<T extends Thread<?>, R extends CrudRepository<T, Integer>> extends JmhBenchmarkBase {

    protected R repository;

    protected List<T> threads = new ArrayList<>();
    protected int pointer = 0;

    @Setup
    public void setup() {
        this.context = new SpringApplication(BthThesisJaversHibernateenversApplication.class).run();
        repository = this.context.getBean(getRepositoryClass());

        for (int i = 0; i < 1000000; i++) {
            repeatedSetupRoutine(i);
        }
    }

    protected abstract T getThread();

    protected abstract Class<R> getRepositoryClass();

    protected abstract void repeatedSetupRoutine(int i);

}
