package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.model.NoversThread;

public interface NoversVersioning extends Versioned<NoversThread> {
    default VersioningDefinition<NoversThread> getVersioningDefinition() {
        return VersioningDefinition.of(NoversThread.class);
    }
}
