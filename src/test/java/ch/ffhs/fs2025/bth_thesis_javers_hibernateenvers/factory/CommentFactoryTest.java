package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Comment;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.model.EnversComment;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.model.JaversComment;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.model.NoversComment;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CommentFactoryTest {

    @Mock
    private PayloadService payloadService;

    @InjectMocks
    private CommentFactory commentFactory;

    static Stream<Class<? extends Comment<?, ?>>> commentTypes() {
        return Stream.of(NoversComment.class, JaversComment.class, EnversComment.class);
    }

    @ParameterizedTest
    @MethodSource("commentTypes")
    void create(Class<Comment<?, ?>> commentType) {
        Comment<?, ?> comment = commentFactory.create(commentType);

        assertThat(comment)
                .isNotNull()
                .isInstanceOf(commentType);
    }
}