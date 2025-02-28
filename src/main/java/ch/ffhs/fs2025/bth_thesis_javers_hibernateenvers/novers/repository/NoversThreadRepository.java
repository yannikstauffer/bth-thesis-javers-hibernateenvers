package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.repository;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.model.NoversThread;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface NoversThreadRepository extends CrudRepository<NoversThread, Integer> {

    @Override
    @NonNull
    @Query("SELECT t FROM novers_threads t LEFT JOIN FETCH t.posts p LEFT JOIN FETCH p.comments WHERE t.id = :id")
    Optional<NoversThread> findById(@NonNull @Param("id") Integer id);
}
