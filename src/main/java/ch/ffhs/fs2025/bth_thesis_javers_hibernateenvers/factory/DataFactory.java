package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.factory;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Comment;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Post;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Thread;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DataFactory {

    private final CommentFactory commentFactory;
    private final PostFactory postFactory;
    private final ThreadFactory threadFactory;

    public <T> T create(Class<T> type, PayloadType payloadType, ObjectGraphComplexity objectGraphComplexity) {
        T testData;

        if (isPost(type)) {
            testData = getPostAsT(type, payloadType, objectGraphComplexity);
        } else if (isThread(type)) {
            testData = type.cast(threadFactory.create((Class<Thread<?>>) type, payloadType, objectGraphComplexity));
            Thread thread = ((Thread) testData);
            for (int i = 0; i < objectGraphComplexity.getEntityCount(); i++) {
                var post = create(thread.getChildType(), payloadType, objectGraphComplexity);
                thread.addChild((Post) post);
            }
        } else if (isComment(type)) {
            testData = type.cast(commentFactory.create((Class<Comment<?>>) type, payloadType));
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type);

        }

        return testData;
    }


    private <T> T getPostAsT(Class<T> type, PayloadType payloadType, ObjectGraphComplexity objectGraphComplexity) {
        T testData;
        testData = type.cast(postFactory.create((Class<Post<?, ?>>) type, payloadType));
        Post post = ((Post) testData);
        for (int i = 0; i < objectGraphComplexity.getEntityCount(); i++) {
            var comment = create(post.getChildType(), payloadType, objectGraphComplexity);
            post.addChild((Comment) comment);
        }
        return testData;
    }

    private boolean isPost(Class<?> type) {
        return Post.class.isAssignableFrom(type);
    }

    private boolean isComment(Class<?> type) {
        return Comment.class.isAssignableFrom(type);
    }

    private boolean isThread(Class<?> type) {
        return Thread.class.isAssignableFrom(type);
    }

}
