package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.repository;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.model.JaversThread;
import org.javers.spring.annotation.JaversSpringDataAuditable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.Optional;

@JaversSpringDataAuditable
public interface JaversThreadRepository extends CrudRepository<JaversThread, Integer> {

    @Override
    @NonNull
    @Query("SELECT t FROM javers_threads t LEFT JOIN FETCH t.posts p LEFT JOIN FETCH p.comments WHERE t.id = :id")
    Optional<JaversThread> findById(@NonNull @Param("id") Integer id);
}
