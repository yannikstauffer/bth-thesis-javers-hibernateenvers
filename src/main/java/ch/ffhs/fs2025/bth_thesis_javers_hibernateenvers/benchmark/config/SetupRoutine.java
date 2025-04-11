package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Thread;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;
import java.util.function.Consumer;

@Getter
@Setter
@Builder
public class SetupRoutine<T extends Thread<?>> {

    private Runnable preSaveSetupRoutine;

    private Consumer<T> postSaveSetupRoutine;

    public Optional<Consumer<T>> getPostSaveSetupRoutine() {
        return Optional.ofNullable(postSaveSetupRoutine);
    }

    @Builder.Default
    private boolean saveSetup = false;
}
