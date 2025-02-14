package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common;

import java.util.List;

public interface Post<T extends Thread<?>, C extends Comment<?>> extends Payload, ChildType<C> {

    T getThread();

    void setThread(T thread);

    List<C> getComments();

    void setComments(List<C> comments);
}
