package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.repository;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.model.NoversThread;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
public interface NoversThreadRepository extends CrudRepository<NoversThread, Integer> {

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"posts", "posts.comments"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT t FROM novers_threads t WHERE t.id = :id")
    Optional<NoversThread> findById(@NonNull @Param("id") Integer id);

    // todo: test
    @NonNull
    @Query("SELECT t FROM novers_threads t WHERE t.id = :id")
    Optional<NoversThread> findByIdForDelete(@NonNull @Param("id") Integer id);
}
