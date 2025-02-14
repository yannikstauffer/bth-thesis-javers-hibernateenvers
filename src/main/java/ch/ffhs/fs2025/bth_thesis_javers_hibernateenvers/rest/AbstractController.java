package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.rest;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.BaseEntity;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Thread;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.ObjectGraphComplexity;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.PayloadType;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory.TestDataFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public T createThread() {
        T thread = testDataFactory.create(getThreadClass(), PayloadType.BASIC, ObjectGraphComplexity.SINGLE);
        return threadRepository.save(thread);
    }

    @GetMapping("/threads")
    @ResponseBody
    public Iterable<T> getThreads() {
        return threadRepository.findAll();
    }

    @GetMapping("/threads/{id}")
    @ResponseBody
    public T getThread(@PathVariable Integer id) {
        return threadRepository.findById(id).orElse(null);
    }

    @DeleteMapping("/threads")
    public void deleteThreads() {
        threadRepository.deleteAll();
    }

    @DeleteMapping("/threads/{id}")
    public void deleteThread(@PathVariable Integer id) {
        threadRepository.deleteById(id);
    }

    @PatchMapping("/threads/{id}")
    @ResponseBody
    @Transactional
    public ResponseEntity<T> updateThread(@PathVariable Integer id,
                                          @RequestBody ThreadDto threadDto) {
        T thread = threadRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Thread with id " + id + "not found"));

        T updated = threadRepository.save(threadDto.update(thread));

        return ResponseEntity.ok(updated);
    }

}
