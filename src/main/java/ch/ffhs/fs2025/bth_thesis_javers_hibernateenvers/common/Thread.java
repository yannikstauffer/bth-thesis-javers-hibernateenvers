package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common;

import java.util.Set;

public interface Thread<P extends Post<? extends Thread<P>, ? extends Comment<P>>> extends Payload, ChildType<P>, Parent<P> {

    String getTitle();

    void setTitle(String title);

    Set<P> getPosts();

    void setPosts(Set<P> comments);

    default void addChild(P post) {
        getPosts().add(post);
        post.setThread(self());
    }

    private <Y extends Thread<P>> Y self() {
        return (Y) this;
    }

    Class<P> getChildType();
}
