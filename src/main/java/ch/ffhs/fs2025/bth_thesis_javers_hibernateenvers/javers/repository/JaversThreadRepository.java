package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.repository;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.model.JaversThread;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.repository.CrudRepository;

@JaversSpringDataAuditable
public interface JaversThreadRepository extends CrudRepository<JaversThread, Integer> {
}