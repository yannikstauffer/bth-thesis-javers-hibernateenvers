package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.model.JaversThread;

public interface JaversVersioning extends Versioned<JaversThread>{
    default VersioningDefinition<JaversThread> getVersioningDefinition() {
        return VersioningDefinition.of(JaversThread.class);
    }
}
