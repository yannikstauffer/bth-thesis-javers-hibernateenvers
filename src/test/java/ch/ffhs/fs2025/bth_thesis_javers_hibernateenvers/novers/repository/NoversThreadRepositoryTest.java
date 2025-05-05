package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.repository;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.benchmark.PostgresBenchmarkContainer;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.DataFactory;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.ObjectGraphComplexity;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.PayloadType;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.model.NoversThread;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.ComponentScan;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ComponentScan(basePackageClasses = {DataFactory.class})
@Testcontainers
class NoversThreadRepositoryTest {

    @Autowired
    private DataFactory dataFactory;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private NoversThreadRepository repository;

    @Container
    static PostgresBenchmarkContainer container = new PostgresBenchmarkContainer();

    @Test
    void findById_existingId_returnsThreadWithPostsAndComments() {
        NoversThread thread = dataFactory.create(NoversThread.class, PayloadType.BASIC, ObjectGraphComplexity.SINGLE);

        // Add posts and comments to the thread
        entityManager.persistAndFlush(thread);

        Optional<NoversThread> foundThread = repository.findById(thread.getId());

        assertThat(foundThread).isPresent();
        assertThat(foundThread.get().getPosts()).isNotEmpty();
        assertThat(foundThread.get().getPosts().stream().findFirst().get().getComments()).isNotEmpty();
    }

    @Test
    void findById_nonExistingId_returnsEmpty() {
        Optional<NoversThread> foundThread = repository.findById(999);

        assertThat(foundThread).isNotPresent();
    }
}
