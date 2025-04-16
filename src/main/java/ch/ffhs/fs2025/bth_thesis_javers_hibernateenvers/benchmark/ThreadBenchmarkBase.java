package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.BthThesisJaversHibernateenversApplication;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config.Scenario;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config.SetupRoutine;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config.Versioned;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Thread;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.config.BenchmarkConfigManager;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.config.BenchmarkOptimizationDto;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.DataFactory;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.ObjectGraphComplexity;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.PayloadType;
import jakarta.persistence.EntityManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.TearDown;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;

import java.lang.reflect.Array;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
public abstract class ThreadBenchmarkBase<T extends Thread<?>> implements Versioned<T> {

    @Getter
    private CrudRepository<T, Integer> repository;
    @Getter
    private DataFactory dataFactory;
    @Getter
    private ObjectGraphComplexity objectGraphComplexity;
    @Getter
    private PayloadType payloadType;

    private T[] testObjects;
    private int pointer = 0;
    private ApplicationContext springContext;

    private BenchmarkConfigManager benchmarkConfigManager;

    private int testObjectCount;

    @Setup
    public void setup() {
        String benchmarkEnvironment = System.getProperty("spring.profiles.active", "dev");

        springContext = new SpringApplicationBuilder(BthThesisJaversHibernateenversApplication.class)
                .profiles(benchmarkEnvironment)
                .run();
        repository = getBean(getVersioningDefinition().getRepositoryClass());
        dataFactory = getBean(DataFactory.class);
        objectGraphComplexity = this.fromEnvEnum(ObjectGraphComplexity.class, "benchmark.config.objectGraphComplexity");
        payloadType = this.fromEnvEnum(PayloadType.class, "benchmark.config.payloadType");

        testObjectCount = loadTestObjectCountFromEnv();
        testObjects = (T[]) Array.newInstance(getVersioningDefinition().getTestObjectClass(), testObjectCount);

        benchmarkConfigManager = new BenchmarkConfigManager("application-" + benchmarkEnvironment + ".yaml");

        if(objectGraphComplexity == null || payloadType == null) {
            throw new IllegalArgumentException("Object graph complexity and payload type must be set in environment.");
        }

        beforeSetupRoutine();
        logBenchmarkSetupStart();

        runSetupRoutine();

        afterSetupRoutine();
        logBenchmarkSetupFinished();
    }

    @TearDown
    public void tearDown() {
        BenchmarkOptimizationDto benchmarkOptimizationDto = BenchmarkOptimizationDto.builder()
                .scenario(getScenario().name().toLowerCase())
                .versioning(getVersioningDefinition().getVersioning().name().toLowerCase())
                .complexity(objectGraphComplexity.name().toLowerCase())
                .payloadType(payloadType.name().toLowerCase())
                .objectCount(testObjects.length)
                .build();

        logBenchmarkFinished();

        try {
            if (testObjectCount * .9 < pointer) {
                benchmarkOptimizationDto.setObjectCount(testObjectCount * 1.5);
                throw new IllegalStateException("Benchmark failed. There were not/barely enough test objects staged. Total test objects created: " + testObjectCount + ". Items processed: " + pointer);
            }

        } finally {
            benchmarkConfigManager.upsertEntry(benchmarkOptimizationDto);
            benchmarkConfigManager.save();
        }
    }

    protected T getTestObject() {
        return dataFactory.create(getVersioningDefinition().getTestObjectClass(), getPayloadType(), getObjectGraphComplexity());
    }

    protected final int loadTestObjectCountFromEnv() {
        String key = String.join(".",
                "benchmark",
                getScenario().name().toLowerCase(),
                getVersioningDefinition().getVersioning().name().toLowerCase(),
                getObjectGraphComplexity().name().toLowerCase(),
                "objects",
                getPayloadType().name().toLowerCase());
        return fromEnv(Integer.class, key);
    }

    protected abstract Scenario getScenario();

    protected abstract SetupRoutine<T> getSetupRoutine();

    protected <B> B getBean(Class<B> type) {
        return this.springContext.getBean(type);
    }

    protected int nextTestObjectPointer() {
        return ++pointer;
    }

    protected void addTestObject(T testObject) {
        this.testObjects[pointer++] = testObject;
    }

    protected T nextTestObject() {
        int nextPointer = nextTestObjectPointer();
        if(nextPointer == 1 || nextPointer > testObjectCount) {
            // ensure that benchmark finishes - error handling will be done in tearDown
            return this.testObjects[0];
        }

        T testObject = this.testObjects[nextPointer - 1];
        this.testObjects[nextPointer - 1] = null;
        return testObject;
    }

    protected void beforeSetupRoutine() {}

    protected void afterSetupRoutine() {
        EntityManager entityManager = springContext.getBean(EntityManager.class);
        entityManager.clear();
        this.pointer = 0;
    }

    private <E extends Enum<E>> E fromEnvEnum(Class<E> clazz, String key) {
        if (springContext.getEnvironment().getProperty(key) == null) {
            throw new IllegalArgumentException("Property " + key + " not found in environment.");
        }

        return Enum.valueOf(clazz, springContext.getEnvironment().getProperty(key));
    }

    private <E> E fromEnv(Class<E> clazz, String key) {
        var property = springContext.getEnvironment().getProperty(key);

        if (property == null) {
            throw new IllegalArgumentException("Property " + key + " not found in environment.");
        }

        if (clazz == Integer.class) {
            return clazz.cast(Integer.valueOf(property));
        }
        return clazz.cast(property);

    }

    private void runSetupRoutine() {
        SetupRoutine<T> setupRoutine = getSetupRoutine();

        IntStream.range(0, testObjectCount)
                .forEach(_ -> setupRoutine.getPreSaveSetupRoutine().run());

        if (setupRoutine.isSaveSetup()) {
            repository.saveAll(List.of(this.testObjects));
        }

        setupRoutine.getPostSaveSetupRoutine().ifPresent(routine -> {
            for (T testObject : testObjects) {
                routine.accept(testObject);
            }
        });
    }

    private void logBenchmarkSetupStart() {
        log.info("""
                        
                        *****************************************************************************************
                        Benchmark setup started.
                        \tCount of objects in repository: {}
                        \tObject graph complexity: {}
                        \tPayload type: {}
                        \tTotal test objects requested: {}
                        Staging test objects...
                        """,
                repository.count(), getObjectGraphComplexity(), getPayloadType(), testObjectCount);
    }

    private void logBenchmarkSetupFinished() {
        log.info("""
                        Benchmark setup finished.
                        \tCount of persisted objects in repository: {}
                        *****************************************************************************************
                        
                        """
                , repository.count());
    }

    private void logBenchmarkFinished() {
        log.info("""
                        
                        *****************************************************************************************
                        Benchmark finished.
                        \tTotal objects in repository: {}
                        \tTotal test objects staged: {}
                        \tTest objects processed: {}
                        *****************************************************************************************
                        
                        """
                , repository.count(), testObjectCount, pointer);
    }

}
