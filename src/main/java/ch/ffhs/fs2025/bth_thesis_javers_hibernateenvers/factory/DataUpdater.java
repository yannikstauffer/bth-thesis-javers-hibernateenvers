package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Comment;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Payload;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Post;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Thread;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataUpdater {

    private final PayloadService payloadService;

    public <T extends Thread<?>> T update(T thread, PayloadType payloadType) {
        thread.setTitle(thread.getTitle() + " updated");
        return updatePayload(thread, payloadType);
    }

    public <T extends Post<?,?>> T update(T post, PayloadType payloadType) {
        return updatePayload(post, payloadType);
    }

    public <T extends Comment<?>> T update(T comment, PayloadType payloadType) {
        return updatePayload(comment, payloadType);
    }

    private <T extends Payload> T updatePayload(T payload, PayloadType payloadType) {
        if(payloadType == PayloadType.EXTENDED) {
            payload.setAttachment(payloadService.attachment());
        }

        payload.setContent(payloadService.content());
        return payload;
    }
}
