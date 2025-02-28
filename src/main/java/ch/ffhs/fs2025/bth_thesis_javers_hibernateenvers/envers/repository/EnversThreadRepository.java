package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.repository;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.model.EnversThread;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface EnversThreadRepository extends CrudRepository<EnversThread, Integer> {

    @Override
    @NonNull
    @Query("SELECT t FROM envers_threads t LEFT JOIN FETCH t.posts p LEFT JOIN FETCH p.comments WHERE t.id = :id")
    Optional<EnversThread> findById(@NonNull @Param("id") Integer id);

}
