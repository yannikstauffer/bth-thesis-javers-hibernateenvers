package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.repository;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.model.EnversThread;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface EnversThreadRepository extends CrudRepository<EnversThread, Integer> {

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"posts", "posts.comments"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT t FROM envers_threads t WHERE t.id = :id")
    Optional<EnversThread> findById(@NonNull @Param("id") Integer id);

}
