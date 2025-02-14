package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Post;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.model.EnversPost;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.model.JaversPost;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.model.NoversPost;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class PostFactoryTest {

    @Mock
    private PayloadService payloadService;

    @InjectMocks
    private PostFactory postFactory;

    static Stream<Class<? extends Post<?, ?>>> postTypes() {
        return Stream.of(NoversPost.class, JaversPost.class, EnversPost.class);
    }

    @ParameterizedTest
    @MethodSource("postTypes")
    void create(Class<Post<?, ?>> commentType) {
        Post<?, ?> comment = postFactory.create(commentType);

        assertThat(comment)
                .isNotNull()
                .isInstanceOf(commentType);
    }
}
