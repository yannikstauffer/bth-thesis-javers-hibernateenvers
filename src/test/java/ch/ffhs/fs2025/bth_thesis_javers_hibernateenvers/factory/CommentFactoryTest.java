package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Comment;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.model.EnversComment;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.model.JaversComment;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.model.NoversComment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class CommentFactoryTest {

    static Stream<Class<? extends Comment<?, ?, ?>>> commentTypes() {
        return Stream.of(NoversComment.class, JaversComment.class, EnversComment.class);
    }

    @ParameterizedTest
    @MethodSource("commentTypes")
    <T extends Comment<T, ?, ?>> void createComment(Class<T> commentType) {
        Comment<?, ?, ?> comment = CommentFactory.create(commentType);

        assertThat(comment)
                .isNotNull()
                .isInstanceOf(commentType);
    }

    @Test
    void createAndAddChildren() {
        NoversComment parent = CommentFactory.create(NoversComment.class);
        parent.setContent("root");
        CommentFactory.createAndAddChildren(parent, 3, 3);

        assertThat(parent)
                .usingRecursiveComparison()
                .isEqualTo(recursiveNoversComment("root"));

    }

    private NoversComment recursiveNoversComment(String parentContent) {
        NoversComment parent = new NoversComment();
        parent.setContent(parentContent);

        for (int i = 1; i <= 3; i++) {
            NoversComment child = new NoversComment();
            child.setContent("Branch " + i);
            child.setParent(parent);
            parent.getChildren().add(child);

            for (int j = 1; j <= 3; j++) {
                NoversComment grandChild = new NoversComment();
                grandChild.setContent("Branch " + i + "." + j);
                grandChild.setParent(child);
                child.getChildren().add(grandChild);

                for (int k = 1; k <= 3; k++) {
                    NoversComment grandGrandChild = new NoversComment();
                    grandGrandChild.setContent("Leaf " + i + "." + j + "." + k);
                    grandGrandChild.setParent(grandChild);
                    grandChild.getChildren().add(grandGrandChild);
                }
            }
        }

        return parent;
    }


}