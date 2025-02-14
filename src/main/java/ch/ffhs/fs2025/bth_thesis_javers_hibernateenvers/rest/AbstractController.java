package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.rest;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.BaseEntity;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Thread;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.ObjectGraphComplexity;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.PayloadType;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.TestDataFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@RequiredArgsConstructor
public abstract class AbstractController<T extends BaseEntity & Thread<?>> {

    private final CrudRepository<T, Integer> threadRepository;
    private final TestDataFactory testDataFactory;

    @GetMapping("/threads/snapshots")
    @ResponseBody
    public abstract String getThreadsSnapshots();

    protected abstract Class<T> getThreadClass();

    @PostMapping("/threads")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Integer createThread() {
        T thread = testDataFactory.create(getThreadClass(), PayloadType.BASIC, ObjectGraphComplexity.SINGLE);
        return threadRepository.save(thread).getId();
    }

    @GetMapping("/threads")
    @ResponseBody
    public Iterable<T> getThreads() {
        return threadRepository.findAll();
    }

    @GetMapping("/threads/{id}")
    @ResponseBody
    public ResponseEntity<T> getThread(@PathVariable Integer id) {
        return ResponseEntity.of(threadRepository.findById(id));
    }

    @DeleteMapping("/threads")
    public void deleteThreads() {
        threadRepository.deleteAll();
    }

    @DeleteMapping("/threads/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteThread(@PathVariable Integer id) {
        threadRepository.deleteById(id);
    }

    @PostMapping("/threads/{id}") // PatchMapping does not work for some weird reason
    @ResponseBody
    public ResponseEntity<T> updateThread(@PathVariable Integer id,
                                          @RequestBody ThreadDto threadDto) {
        return ResponseEntity.of(
                threadRepository.findById(id)
                        .map(threadDto::update)
                        .map(threadRepository::save)
        );
    }

}
