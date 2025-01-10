package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.repository;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.model.NoversComment;
import org.springframework.data.repository.CrudRepository;

public interface NoversCommentRepository extends CrudRepository<NoversComment, Integer> {
}