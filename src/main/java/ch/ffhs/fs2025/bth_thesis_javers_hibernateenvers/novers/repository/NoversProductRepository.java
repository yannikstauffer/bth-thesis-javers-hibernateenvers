package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.repository;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.model.NoversProduct;
import org.springframework.data.repository.CrudRepository;

public interface NoversProductRepository extends CrudRepository<NoversProduct, Integer> {
}