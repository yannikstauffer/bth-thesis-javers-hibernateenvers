package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.stream.IntStream;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RepeatedRunnable {

    private Runnable runnable;

    public void run(int count) {
        IntStream.range(0, count)
                .forEach(_ -> runnable.run());
    }
}
