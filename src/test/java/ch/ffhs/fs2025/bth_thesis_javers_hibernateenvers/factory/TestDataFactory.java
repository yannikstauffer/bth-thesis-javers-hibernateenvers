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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestDataFactory {

    @Mock
    private PayloadService payloadService;

    private DataFactory dataFactory;

    @BeforeEach
    void setUp() {
        CommentFactory commentFactory = new CommentFactory(payloadService);
        PostFactory postFactory = new PostFactory(payloadService);
        ThreadFactory threadFactory = new ThreadFactory(payloadService);

        dataFactory = new DataFactory(commentFactory, postFactory, threadFactory);

    }

    static Stream<Types> types() {
        return Stream.of(Types.novers(), Types.javers(), Types.envers());
    }

    @ParameterizedTest
    @MethodSource("types")
    void create_assertTypes(Types types) {
        Class<? extends Thread<?>> threadType = types.getThread();
        Class<? extends Post<?, ?>> postType = types.getPost();
        Class<? extends Comment<?>> commentType = types.getComment();

        Thread<?> thread = dataFactory.create(threadType, PayloadType.BASIC, ObjectGraphSize.SINGLE);

        assertThat(thread)
                .isNotNull()
                .isInstanceOf(threadType);
        assertThat(thread.getPosts())
                .isNotNull()
                .hasSize(1)
                .first()
                .isInstanceOf(postType);
        assertThat(thread.getPosts().stream().findFirst().get().getComments())
                .isNotNull()
                .hasSize(1)
                .first()
                .isInstanceOf(commentType);
    }


    static Stream<ObjectGraphSize> complexities() {
        return Stream.of(ObjectGraphSize.values());
    }

    @ParameterizedTest
    @MethodSource("complexities")
    void create_assertComplexity(ObjectGraphSize complexity) {
        Thread<?> thread = dataFactory.create(Types.novers().getThread(), PayloadType.BASIC, complexity);

        assertThat(thread)
                .isNotNull();
        assertThat(thread.getPosts())
                .isNotNull()
                .hasSize(complexity.getEntityCount())
                .allSatisfy(post -> {
                    assertThat(post.getComments())
                            .isNotNull()
                            .hasSize(complexity.getEntityCount());
                });
    }

    @Test
    void create_withExtendedPayload() {
        byte[] mockAttachment = new byte[0];
        when(payloadService.attachment()).thenReturn(mockAttachment);

        Thread<?> thread = dataFactory.create(Types.novers().getThread(), PayloadType.EXTENDED, ObjectGraphSize.SINGLE);

        assertThat(thread.getAttachment())
                .isEqualTo(mockAttachment);

        assertThat(thread.getPosts().stream().findFirst().get().getAttachment())
                .isEqualTo(mockAttachment);

        assertThat(thread.getPosts().stream().findFirst().get().getComments().stream().findFirst().get().getAttachment())
                .isEqualTo(mockAttachment);
    }

    @Test
    void create_withBasicPayload() {
        Thread<?> thread = dataFactory.create(Types.novers().getThread(), PayloadType.BASIC, ObjectGraphSize.SINGLE);

        verify(payloadService, never()).attachment();
        assertThat(thread.getAttachment())
                .isEmpty();

        assertThat(thread.getPosts().stream().findFirst().get().getAttachment())
                .isEmpty();

        assertThat(thread.getPosts().stream().findFirst().get().getComments().stream().findFirst().get().getAttachment())
                .isEmpty();
    }

    @Test
    void create_addsContent() {
        when(payloadService.content())
                .thenReturn("content-1")
                .thenReturn("content-2")
                .thenReturn("content-3");

        Thread<?> thread = dataFactory.create(Types.novers().getThread(), PayloadType.BASIC, ObjectGraphSize.SINGLE);

        verify(payloadService, never()).attachment();
        assertThat(thread.getContent())
                .isEqualTo("content-1");

        assertThat(thread.getPosts().stream().findFirst().get().getContent())
                .isEqualTo("content-2");

        assertThat(thread.getPosts().stream().findFirst().get().getComments().stream().findFirst().get().getContent())
                .isEqualTo("content-3");
    }

    @Test
    void create_addsThreadTitle() {
        when(payloadService.name(any(), any())).thenReturn("title");

        Thread<?> thread = dataFactory.create(Types.novers().getThread(), PayloadType.BASIC, ObjectGraphSize.SINGLE);

        verify(payloadService, never()).attachment();
        assertThat(thread.getTitle())
                .contains("title");
    }

}

@RequiredArgsConstructor
@Getter
class Types {
    private final Class<? extends Thread<?>> thread;
    private final Class<? extends Post<?, ?>> post;
    private final Class<? extends Comment<?>> comment;

    public static Types envers() {
        return new Types(EnversThread.class, EnversPost.class, EnversComment.class);
    }

    public static Types javers() {
        return new Types(JaversThread.class, JaversPost.class, JaversComment.class);
    }

    public static Types novers() {
        return new Types(NoversThread.class, NoversPost.class, NoversComment.class);
    }

}
