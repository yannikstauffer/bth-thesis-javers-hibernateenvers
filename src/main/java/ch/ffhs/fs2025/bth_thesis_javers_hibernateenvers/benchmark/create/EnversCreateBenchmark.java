package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.create;


import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config.EnversVersioning;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.model.EnversThread;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
public class EnversCreateBenchmark extends AbstractCreateBenchmark<EnversThread> implements EnversVersioning {

}
