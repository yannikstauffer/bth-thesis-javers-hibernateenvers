package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common;

import java.util.List;

public interface Thread<U extends User<?, P, ?>, P extends Post<U, ?, ?>> extends Payload, ChildType<P> {

    String getTitle();

    void setTitle(String title);

    U getOwner();

    void setOwner(U owner);

    List<P> getPosts();

    void setPosts(List<P> comments);

}