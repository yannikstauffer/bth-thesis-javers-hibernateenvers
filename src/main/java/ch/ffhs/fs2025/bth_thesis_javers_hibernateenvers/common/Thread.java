package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common;

import java.util.List;

public interface Thread<P extends Post<? extends Thread<P>, ? extends Comment<P>>> extends Payload, ChildType<P>, Parent<P> {

    String getTitle();

    void setTitle(String title);

    List<P> getPosts();

    void setPosts(List<P> comments);

    default void addChild(P post) {
        getPosts().add(post);
        post.setThread(self());
    }

    private <Y extends Thread<P>> Y self() {
        return (Y) this;
    }

    Class<P> getChildType();
}
