package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.BthThesisJaversHibernateenversApplication;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Thread;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.DataFactory;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.ObjectGraphComplexity;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.PayloadType;
import jakarta.persistence.EntityManager;
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

    @Getter
    private R repository;
    @Getter
    private DataFactory dataFactory;
    @Getter
    private ObjectGraphComplexity objectGraphComplexity;
    @Getter
    private PayloadType payloadType;

    private List<T> testObjects;
    private int pointer = 0;
    private ApplicationContext springContext;

    @Setup
    public void setup() {
        testObjects = new ArrayList<>();
        springContext = new SpringApplication(BthThesisJaversHibernateenversApplication.class).run();
        repository = getBean(getRepositoryClass());
        dataFactory = getBean(DataFactory.class);
        objectGraphComplexity = fromEnv(ObjectGraphComplexity.class, "benchmark.config.objectGraphComplexity");
        payloadType = fromEnv(PayloadType.class, "benchmark.config.payloadType");

        beforeSetupRoutine();
        logBenchmarkSetupStart();

        for (int i = 0; i < getTestObjectCount(); i++) {
            repeatedSetupRoutine(i);
        }

        afterSetupRoutine();
        logBenchmarkSetupFinished();
    }

    @TearDown
    public void tearDown() {
        if (testObjects.size() < pointer) {
            throw new IllegalStateException("Benchmark failed. There were not enough test objects staged. Total test objects created: " + testObjects.size() + ". Items processed: " + pointer);
        }
        System.out.println();
        System.out.println("Benchmark finished. Total test objects staged: " + testObjects.size() + ". Test objects processed: " + pointer);
    }

    protected T getTestObject() {
        return dataFactory.create(getTestObjectClass(), getPayloadType(), getObjectGraphComplexity());
    }

    protected abstract Class<T> getTestObjectClass();

    protected abstract Class<R> getRepositoryClass();

    protected abstract void repeatedSetupRoutine(int i);

    protected abstract int getTestObjectCount();

    private <B> B getBean(Class<B> type) {
        return this.springContext.getBean(type);
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

    protected void beforeSetupRoutine() {}

    protected void afterSetupRoutine() {
        EntityManager entityManager = springContext.getBean(EntityManager.class);
        entityManager.clear();
    }

    private <E extends Enum<E>> E fromEnv(Class<E> clazz, String key) {
        return Enum.valueOf(clazz, springContext.getEnvironment().getProperty(key));
    }

    private void logBenchmarkSetupStart() {
        System.out.println();
        System.out.println("*****************************************************************************************");
        System.out.println("Benchmark setup started.");
        System.out.println("Object graph complexity: " + getObjectGraphComplexity());
        System.out.println("Payload type: " + getPayloadType());
        System.out.println("Total test objects requested: " + getTestObjectCount());
        System.out.println("Staging test objects...");
    }

    private void logBenchmarkSetupFinished() {
        System.out.println("Benchmark setup finished.");
        System.out.println("*****************************************************************************************");
    }

}
