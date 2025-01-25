package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common;

import java.util.List;

public interface Post<U extends User<T, ?, C>, T extends Thread<U, ?>, C extends Comment<U, ?>> extends Payload, ChildType<C> {

    T getThread();

    void setThread(T thread);

    U getOwner();

    void setOwner(U owner);

    List<C> getComments();

    void setComments(List<C> comments);
}