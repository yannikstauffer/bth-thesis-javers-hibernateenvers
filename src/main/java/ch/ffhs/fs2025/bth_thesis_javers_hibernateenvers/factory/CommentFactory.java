package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Comment;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.model.EnversComment;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.model.JaversComment;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.model.NoversComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class CommentFactory extends AbstractPayloadFactory<Comment<?>> {

    @Autowired
    public CommentFactory(PayloadService payloadService) {
        super(payloadService);
    }

    @Override
    protected Comment<?> create(Class<Comment<?>> type) {
        return switch (type.getSimpleName()) {
            case "NoversComment" -> type.cast(new NoversComment());
            case "JaversComment" -> type.cast(new JaversComment());
            case "EnversComment" -> type.cast(new EnversComment());
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }
}
