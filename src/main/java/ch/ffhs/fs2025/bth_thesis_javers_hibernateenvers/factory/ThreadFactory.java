package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Thread;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.model.EnversThread;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.model.JaversThread;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.model.NoversThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class ThreadFactory extends AbstractPayloadFactory<Thread<?, ?>> {

    @Autowired
    public ThreadFactory(PayloadService payloadService) {
        super(payloadService);
    }

    public Thread<?, ?> create(Class<Thread<?, ?>> type, ObjectGraphComplexity objectGraphComplexity, PayloadType payloadType) {
        Thread<?, ?> thread = super.create(type, payloadType);
        thread.setTitle(payloadService.name(objectGraphComplexity, payloadType));

        return thread;
    }

    @Override
    Thread<?, ?> create(Class<Thread<?, ?>> type) {
        return switch (type.getSimpleName()) {
            case "NoversThread" -> type.cast(new NoversThread());
            case "JaversThread" -> type.cast(new JaversThread());
            case "EnversThread" -> type.cast(new EnversThread());
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }

}