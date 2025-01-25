package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.model.EnversUser;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.model.JaversUser;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.model.NoversUser;
import org.springframework.stereotype.Service;

@Service
public class UserFactory {

    <T> T create(Class<T> type) {
        return switch (type.getSimpleName()) {
            case "NoversUser" -> type.cast(new NoversUser());
            case "JaversUser" -> type.cast(new JaversUser());
            case "EnversUser" -> type.cast(new EnversUser());
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };
    }
}