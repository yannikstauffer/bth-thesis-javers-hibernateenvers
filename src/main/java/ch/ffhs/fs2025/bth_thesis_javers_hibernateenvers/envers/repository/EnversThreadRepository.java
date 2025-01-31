package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.repository;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.model.EnversThread;
import org.springframework.data.repository.CrudRepository;

public interface EnversThreadRepository extends CrudRepository<EnversThread, Integer> {
}