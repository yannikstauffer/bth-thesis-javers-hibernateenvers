package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.rest;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.TestDataFactory;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.model.NoversThread;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.repository.NoversThreadRepository;
import jakarta.persistence.EntityManager;
import org.javers.core.Javers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//@RestController
@RequestMapping(value = "/novers", consumes = "application/json", produces = "application/json")
public class NoversController extends AbstractController<NoversThread> {


    @Autowired
    public NoversController(NoversThreadRepository threadRepository, TestDataFactory testDataFactory, Javers javers, EntityManager entityManager) {
        super(threadRepository, testDataFactory);
    }

    @GetMapping("/threads/snapshots")
    public String getThreadsSnapshots() {
        return "{ \"message\": \"Nothing here\" }";
    }

    @Override
    protected Class<NoversThread> getThreadClass() {
        return NoversThread.class;
    }

}
