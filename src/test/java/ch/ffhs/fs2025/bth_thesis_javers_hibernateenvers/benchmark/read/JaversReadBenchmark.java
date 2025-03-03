package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.read;


import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config.JaversVersioning;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.model.JaversThread;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class JaversReadBenchmark extends AbstractReadBenchmark<JaversThread> implements JaversVersioning {

}
