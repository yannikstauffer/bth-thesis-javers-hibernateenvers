package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Payload;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractPayloadFactoryTest {

    @Mock
    private PayloadService payloadService;

    @InjectMocks
    private PayloadFactoryImpl payloadFactory;

    @Test
    void create_withPayloadType_EXTENDED() {
        when(payloadService.content()).thenReturn("new content");
        when(payloadService.attachment()).thenReturn(new byte[1337]);

        PayloadImpl payload = payloadFactory.create(PayloadImpl.class, PayloadType.EXTENDED);

        assertThat(payload)
                .isNotNull()
                .isInstanceOf(PayloadImpl.class)
                .hasFieldOrPropertyWithValue("content", "new content")
                .hasFieldOrPropertyWithValue("attachment", new byte[1337]);
    }

    @Test
    void create_withPayloadType_BASIC() {
        when(payloadService.content()).thenReturn("new content");

        PayloadImpl payload = payloadFactory.create(PayloadImpl.class, PayloadType.BASIC);

        assertThat(payload)
                .isNotNull()
                .isInstanceOf(PayloadImpl.class)
                .hasFieldOrPropertyWithValue("content", "new content")
                .hasFieldOrPropertyWithValue("attachment", null);
    }

    @Test
    void create() {
        PayloadImpl payload = payloadFactory.create(PayloadImpl.class);

        assertThat(payload)
                .isNotNull()
                .isInstanceOf(PayloadImpl.class)
                .hasFieldOrPropertyWithValue("content", null)
                .hasFieldOrPropertyWithValue("attachment", null);
    }

}

class PayloadFactoryImpl extends AbstractPayloadFactory<PayloadImpl> {

    public PayloadFactoryImpl(PayloadService payloadService) {
        super(payloadService);
    }

    @Override
    PayloadImpl create(Class<PayloadImpl> type) {
        return new PayloadImpl();
    }
}

@Getter
@Setter
class PayloadImpl implements Payload {

    private String content;
    private byte[] attachment;

}