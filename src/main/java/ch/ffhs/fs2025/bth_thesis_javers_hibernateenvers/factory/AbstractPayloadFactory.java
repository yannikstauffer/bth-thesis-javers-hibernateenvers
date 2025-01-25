package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Payload;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
abstract class AbstractPayloadFactory<T extends Payload> {

    protected final PayloadService payloadService;

    T create(Class<T> type, PayloadType payloadType) {
        T payloadHolder = type.cast(create(type));

        payloadHolder.setContent(payloadService.content());

        if (PayloadType.EXTENDED.equals(payloadType)) {
            payloadHolder.setAttachment(payloadService.attachment());
        }

        return payloadHolder;
    }

    abstract T create(Class<T> type);

}