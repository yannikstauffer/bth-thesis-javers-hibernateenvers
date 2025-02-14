package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Post;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.model.EnversPost;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.model.JaversPost;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.model.NoversPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class PostFactory extends AbstractPayloadFactory<Post<?, ?>> {

    @Autowired
    public PostFactory(PayloadService payloadService) {
        super(payloadService);
    }

    @Override
    Post<?, ?> create(Class<Post<?, ?>> type) {
        return switch (type.getSimpleName()) {
            case "NoversPost" -> type.cast(new NoversPost());
            case "JaversPost" -> type.cast(new JaversPost());
            case "EnversPost" -> type.cast(new EnversPost());
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }
}
