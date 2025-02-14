package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.rest;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.TestDataFactory;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.model.JaversThread;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.repository.JaversThreadRepository;
import org.javers.core.Javers;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

//@RestController
@RequestMapping(value = "/javers", consumes = "application/json", produces = "application/json")
public class JaversController extends AbstractController<JaversThread> {

    private final Javers javers;

    @Autowired
    public JaversController(JaversThreadRepository threadRepository, TestDataFactory testDataFactory, Javers javers) {
        super(threadRepository, testDataFactory);
        this.javers = javers;
    }

    @GetMapping("/threads/snapshots")
    public String getThreadsSnapshots() {
        QueryBuilder jqlQuery = QueryBuilder.byClass(JaversThread.class);
        List<CdoSnapshot> snapshots = javers.findSnapshots(jqlQuery.build());
        return javers.getJsonConverter().toJson(snapshots);
    }

    @Override
    protected Class<JaversThread> getThreadClass() {
        return JaversThread.class;
    }
}
