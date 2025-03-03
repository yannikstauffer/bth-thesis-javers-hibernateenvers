package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Thread;

public interface Versioned<T extends Thread<?>> {
    VersioningDefinition<T> getVersioningDefinition();
}
