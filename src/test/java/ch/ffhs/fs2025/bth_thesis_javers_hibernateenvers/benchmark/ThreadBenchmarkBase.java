package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.BthThesisJaversHibernateenversApplication;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Thread;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.ObjectGraphComplexity;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.PayloadType;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.TestDataFactory;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class ThreadBenchmarkBase<T extends Thread<?>, R extends CrudRepository<T, Integer>> extends JmhBenchmarkBase {

    @Getter(AccessLevel.PROTECTED)
    private R repository;
    private TestDataFactory testDataFactory;

    private final List<T> testObjects = new ArrayList<>();
    private int pointer = 0;
    private ApplicationContext context;

    @Setup
    public void setup() {
        this.context = new SpringApplication(BthThesisJaversHibernateenversApplication.class).run();
        repository = getBean(getRepositoryClass());

        beforeSetupRoutine();
        for (int i = 0; i < initObjectCount(); i++) {
            repeatedSetupRoutine(i);
        }
        afterSetupRoutine();
        System.out.println("Benchmark setup finished. Total test objects staged: " + testObjects.size());
    }

    protected T getTestObject() {
        return testDataFactory.create(getTestObjectClass(), PayloadType.BASIC, ObjectGraphComplexity.SINGLE);
    }

    protected abstract Class<T> getTestObjectClass();

    protected abstract Class<R> getRepositoryClass();

    protected abstract void repeatedSetupRoutine(int i);

    protected <B> B getBean(Class<B> type) {
        return this.context.getBean(type);
    }

    protected int initObjectCount() {
        return 1000000;
    }

    protected int nextTestObjectPointer() {
        return ++pointer;
    }

    protected void addTestObject(T testObject) {
        testObjects.add(testObject);
    }

    protected T nextTestObject() {
        return this.testObjects.get(nextTestObjectPointer() - 1);
    }

    protected void beforeSetupRoutine() {
        testDataFactory = getBean(TestDataFactory.class);
    }

    protected void afterSetupRoutine() {
        EntityManager entityManager = context.getBean(EntityManager.class);
        entityManager.clear();
    }

    @TearDown
    public void tearDown() {
        if (testObjects.size() < pointer) {
            throw new IllegalStateException("Benchmark failed. There were not enough test objects staged. Total test objects created: " + testObjects.size() + ". Items processed: " + pointer);
        }
        System.out.println("Benchmark finished. Total test objects staged: " + testObjects.size() + ". Test objects processed: " + pointer);
    }

}
