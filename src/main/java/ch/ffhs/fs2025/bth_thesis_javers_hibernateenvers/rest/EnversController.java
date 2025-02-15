package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.rest;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.model.EnversThread;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.repository.EnversThreadRepository;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.TestDataFactory;
import jakarta.persistence.EntityManager;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditQuery;
import org.javers.core.Javers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/envers", consumes = "application/json", produces = "application/json")
public class EnversController extends AbstractController<EnversThread> {

    private final Javers javers;
    private final EntityManager entityManager;

    @Autowired
    public EnversController(EnversThreadRepository threadRepository, TestDataFactory testDataFactory, Javers javers, EntityManager entityManager) {
        super(threadRepository, testDataFactory);
        this.javers = javers;
        this.entityManager = entityManager;
    }

    @GetMapping("/threads/snapshots")
    public String getThreadsSnapshots() {
        AuditReader auditReader = AuditReaderFactory.get(entityManager);

        AuditQuery q = auditReader.createQuery()
                .forRevisionsOfEntity(EnversThread.class, true, true);

        return javers.getJsonConverter().toJson(q.getResultList());
    }

    @Override
    protected Class<EnversThread> getThreadClass() {
        return EnversThread.class;
    }

}
