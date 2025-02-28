package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Thread;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.model.EnversThread;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.model.JaversThread;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.model.NoversThread;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ThreadFactoryTest {

    @Mock
    private PayloadService payloadService;

    @InjectMocks
    private ThreadFactory threadFactory;

    @ParameterizedTest
    @MethodSource("threadTypes")
    void create_withOptions_addsName(Class<Thread<?>> threadType) {
        when(payloadService.name(ObjectGraphComplexity.SINGLE, PayloadType.BASIC)).thenReturn("title");

        Thread<?> thread = threadFactory.create(threadType, PayloadType.BASIC, ObjectGraphComplexity.SINGLE);

        assertThat(thread)
                .isNotNull()
                .isInstanceOf(threadType);
        assertThat(thread.getTitle())
                .isNotBlank()
                .isEqualTo("title");
    }

    static Stream<Class<? extends Thread<?>>> threadTypes() {
        return Stream.of(NoversThread.class, JaversThread.class, EnversThread.class);
    }

    @ParameterizedTest
    @MethodSource("threadTypes")
    void create(Class<Thread<?>> threadType) {
        Thread<?> thread = threadFactory.create(threadType);

        assertThat(thread)
                .isNotNull()
                .isInstanceOf(threadType);
    }
}
