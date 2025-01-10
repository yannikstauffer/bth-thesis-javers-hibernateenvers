package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.repository;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.model.EnversComment;
import org.springframework.data.repository.CrudRepository;

public interface EnversCommentRepository extends CrudRepository<EnversComment, Integer> {
}