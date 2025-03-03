package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.config;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Thread;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.model.EnversThread;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.repository.EnversThreadRepository;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.model.JaversThread;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.repository.JaversThreadRepository;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.model.NoversThread;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.repository.NoversThreadRepository;
import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;

@Data
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class VersioningDefinition<T extends Thread<?>> {

    private final Versioning versioning;
    private final Class<T> testObjectClass;
    private final Class<? extends CrudRepository<T, Integer>> repositoryClass;

    public static <T extends Thread<?>> VersioningDefinition<T> of(Class<T> threadType) {
        if(threadType.equals(NoversThread.class)) {
            return (VersioningDefinition<T>) new VersioningDefinition<>(Versioning.NOVERS, NoversThread.class, NoversThreadRepository.class);
        } else if(threadType.equals(EnversThread.class)) {
            return (VersioningDefinition<T>) new VersioningDefinition<>(Versioning.ENVERS, EnversThread.class, EnversThreadRepository.class);
        } else if(threadType.equals(JaversThread.class)) {
            return (VersioningDefinition<T>) new VersioningDefinition<>(Versioning.JAVERS, JaversThread.class, JaversThreadRepository.class);
        }
        throw new IllegalArgumentException("Unsupported thread type: " + threadType);
    }


}
