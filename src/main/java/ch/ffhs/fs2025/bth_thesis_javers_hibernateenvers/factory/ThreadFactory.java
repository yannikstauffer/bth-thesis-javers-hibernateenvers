package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.model.EnversThread;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.model.JaversThread;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.model.NoversThread;

public class ThreadFactory {

    public static <T> T create(Class<T> type) {
        return switch (type.getSimpleName()) {
            case "NoversThread" -> type.cast(new NoversThread());
            case "JaversThread" -> type.cast(new JaversThread());
            case "EnversThread" -> type.cast(new EnversThread());
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };

    }
}