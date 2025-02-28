package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common;

import java.util.Set;

public interface Post<T extends Thread<? extends Post<T, C>>, C extends Comment<? extends Post<T, C>>> extends Payload, ChildType<C>, Parent<C> {

    T getThread();

    void setThread(T thread);

    Set<C> getComments();

    void setComments(Set<C> comments);

    default void addChild(C comment) {
        getComments().add(comment);
        comment.setPost(self());
    }

    private <Y extends Post<T, C>> Y self() {
        return (Y) this;
    }
}
