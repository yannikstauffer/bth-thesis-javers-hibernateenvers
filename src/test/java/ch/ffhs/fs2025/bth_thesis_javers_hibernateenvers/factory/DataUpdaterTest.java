package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Comment;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Post;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Thread;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.model.EnversComment;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.model.EnversPost;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.model.EnversThread;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.model.JaversComment;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.model.JaversPost;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.model.JaversThread;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.model.NoversComment;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.model.NoversPost;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.model.NoversThread;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class DataUpdaterTest {

    byte[] UPDATED_ATTACHMENT;
    static String UPDATED_CONTENT = "Updated Content";

    @Mock
    private PayloadService payloadService;

    @InjectMocks
    private DataUpdater dataUpdater;

    @BeforeEach
    void setUp() {
        lenient().when(payloadService.content()).thenReturn(UPDATED_CONTENT);
        lenient().when(payloadService.name(any(), any())).thenCallRealMethod();

        byte[] attachment = new byte[100];
        new Random().nextBytes(attachment);
        UPDATED_ATTACHMENT = attachment;
        lenient().when(payloadService.attachment()).thenReturn(UPDATED_ATTACHMENT);

    }

    @ParameterizedTest
    @MethodSource("thread_arguments")
    void update_thread(Thread<?> thread) {
        thread.setTitle("Original Title");
        thread.setContent("Original Content");

        Thread<?> updated = dataUpdater.update(thread, PayloadType.BASIC);

        assertThat(updated.getContent())
                .isNotBlank()
                .isEqualTo(UPDATED_CONTENT);
        assertThat(updated.getTitle())
                .isNotBlank()
                .isEqualTo("Original Title updated");
        assertThat(updated.getAttachment())
                .isNullOrEmpty();
    }

    @ParameterizedTest
    @MethodSource("thread_arguments")
    void update_thread_withAttachment(Thread<?> thread) {
        thread.setTitle("Original Title");
        thread.setContent("Original Content");

        Thread<?> updated = dataUpdater.update(thread, PayloadType.EXTENDED);

        assertThat(updated.getContent())
                .isNotBlank()
                .isEqualTo(UPDATED_CONTENT);
        assertThat(updated.getTitle())
                .isNotBlank()
                .isEqualTo("Original Title updated");
        assertThat(updated.getAttachment())
                .isEqualTo(UPDATED_ATTACHMENT);
    }

    static Stream<Arguments> thread_arguments() {
        return Stream.of(
                Arguments.of(new NoversThread()),
                Arguments.of(new EnversThread()),
                Arguments.of(new JaversThread())
        );
    }

    @ParameterizedTest
    @MethodSource("post_arguments")
    void update_post(Post<?,?> post) {
        post.setContent("Original Content");

        Post<?,?> updated = dataUpdater.update(post, PayloadType.BASIC);

        assertThat(updated.getContent())
                .isNotBlank()
                .isEqualTo(UPDATED_CONTENT);
        assertThat(updated.getAttachment())
                .isNullOrEmpty();
    }

    @ParameterizedTest
    @MethodSource("post_arguments")
    void update_post_withAttachment(Post<?,?> post) {
        post.setContent("Original Content");

        Post<?,?> updated = dataUpdater.update(post, PayloadType.EXTENDED);

        assertThat(updated.getContent())
                .isNotBlank()
                .isEqualTo(UPDATED_CONTENT);
        assertThat(updated.getAttachment())
                .isEqualTo(UPDATED_ATTACHMENT);
    }

    static Stream<Arguments> post_arguments() {
        return Stream.of(
                Arguments.of(new NoversPost()),
                Arguments.of(new EnversPost()),
                Arguments.of(new JaversPost())
        );
    }

    @ParameterizedTest
    @MethodSource("comment_arguments")
    void update_comment(Comment<?> comment) {
        comment.setContent("Original Content");

        Comment<?> updated = dataUpdater.update(comment, PayloadType.BASIC);

        assertThat(updated.getContent())
                .isNotBlank()
                .isEqualTo(UPDATED_CONTENT);
        assertThat(updated.getAttachment())
                .isNullOrEmpty();
    }

    @ParameterizedTest
    @MethodSource("comment_arguments")
    void update_comment_withAttachment(Comment<?> comment) {
        comment.setContent("Original Content");

        Comment<?> updated = dataUpdater.update(comment, PayloadType.EXTENDED);

        assertThat(updated.getContent())
                .isNotBlank()
                .isEqualTo(UPDATED_CONTENT);
        assertThat(updated.getAttachment())
                .isEqualTo(UPDATED_ATTACHMENT);
    }

    static Stream<Arguments> comment_arguments() {
        return Stream.of(
                Arguments.of(new NoversComment()),
                Arguments.of(new EnversComment()),
                Arguments.of(new JaversComment())
        );
    }
}
