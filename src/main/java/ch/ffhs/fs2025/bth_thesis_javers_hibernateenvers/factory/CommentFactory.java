package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Comment;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.model.EnversComment;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.model.JaversComment;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.model.NoversComment;

import java.util.ArrayList;
import java.util.List;

public class CommentFactory {

    public static <T extends Comment<T, ?, ?>> T create(Class<T> type) {
        return switch (type.getSimpleName()) {
            case "NoversComment" -> type.cast(new NoversComment());
            case "JaversComment" -> type.cast(new JaversComment());
            case "EnversComment" -> type.cast(new EnversComment());
            default -> throw new IllegalArgumentException("Unknown type: " + type);
        };

    }

    public static <T extends Comment<T, ?, ?>> T create(Class<T> type, Complexity complexity) {
        return create(type, complexity, "Trunk of " + complexity.name() + " complexity");
    }

    private static <T extends Comment<T, ?, ?>> T create(Class<T> type, Complexity complexity, String content) {
        T comment = type.cast(create(type));

        if (complexity == Complexity.COMPLEX) {
            createAndAddChildren(comment, 3, 3);
        }
        comment.setContent(content);

        return comment;
    }

    private static <T extends Comment<T, ?, ?>> List<T> createAndAddChildren(T parent, int count, String content) {
        Class<T> type = getType(parent);

        List<T> comments = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            T child = type.cast(create(type, Complexity.SINGLE, content + i));
            child.setParent(parent);
            comments.add(child);
        }
        return comments;
    }

    static <T extends Comment<T, ?, ?>> void createAndAddChildren(T parent, int count, int depth) {
        createAndAddChildren(parent, count, depth, 0, "");

    }

    private static <T extends Comment<T, ?, ?>> void createAndAddChildren(T parent, int count, int depth, int currentDepth, String breadCrumb) {
        boolean isLeaf = depth - 1 == currentDepth;

        String content = isLeaf ? "Leaf " : "Branch ";
        String newBreadCrumb = currentDepth == 0 ? "" : breadCrumb + currentDepth + ".";


        List<T> children = createAndAddChildren(parent, count, content + newBreadCrumb);
        parent.setChildren(children);

        if (!isLeaf) {
            for (T child : children) {
                createAndAddChildren(child, count, depth, currentDepth + 1, newBreadCrumb);
            }
        }

    }

    private static <T> Class<T> getType(T clazz) {
        return (Class<T>) clazz.getClass();
    }
}