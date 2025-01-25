package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Thread;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.model.EnversThread;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.model.JaversThread;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.model.NoversThread;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ThreadFactoryTest {

    @Mock
    private PayloadService payloadService;

    @InjectMocks
    private ThreadFactory threadFactory;

    static Stream<Class<? extends Thread<?, ?>>> threadTypes() {
        return Stream.of(NoversThread.class, JaversThread.class, EnversThread.class);
    }

    @ParameterizedTest
    @MethodSource("threadTypes")
    void create(Class<Thread<?, ?>> commentType) {
        Thread<?, ?> comment = threadFactory.create(commentType);

        assertThat(comment)
                .isNotNull()
                .isInstanceOf(commentType);
    }

    @Test
    void create_withComplexity() {
        //todo implement
    }
}