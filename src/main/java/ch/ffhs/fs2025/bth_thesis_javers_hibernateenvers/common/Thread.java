package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common;

import java.util.List;

public interface Thread<P extends Post<?, ?>> extends Payload, ChildType<P> {

    String getTitle();

    void setTitle(String title);

    List<P> getPosts();

    void setPosts(List<P> comments);

}
