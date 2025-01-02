package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark;


import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.BthThesisJaversHibernateenversApplication;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.jmh.AbstractBenchmark;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.model.NoversProduct;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.repository.NoversProductRepository;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.springframework.boot.SpringApplication;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class NoVersBenchmark extends AbstractBenchmark {

    @Setup
    public void setup() {
        this.context = new SpringApplication(BthThesisJaversHibernateenversApplication.class).run();
        productRepository = this.context.getBean(NoversProductRepository.class); //UserService
    }

    NoversProductRepository productRepository;

    @Benchmark
    public void persist(Blackhole blackhole) {
        NoversProduct product = NoversProduct.builder()
                .name("TestProduct")
                .build();
        productRepository.save(product);

        blackhole.consume(product);
    }
}