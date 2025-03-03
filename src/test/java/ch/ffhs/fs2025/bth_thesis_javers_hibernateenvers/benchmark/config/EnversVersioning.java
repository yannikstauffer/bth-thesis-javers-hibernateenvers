package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.model.EnversThread;

public interface EnversVersioning extends Versioned<EnversThread> {
    default VersioningDefinition<EnversThread> getVersioningDefinition() {
        return VersioningDefinition.of(EnversThread.class);
    }
}
