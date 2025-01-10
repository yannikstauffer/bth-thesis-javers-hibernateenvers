package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common;

import java.util.List;

public interface Comment<C extends Comment<?, ?, ?>, T extends Thread<?, ?>, U extends User<?, ?>> {

    String getContent();

    void setContent(String content);

    C getParent();

    void setParent(C parent);

    List<C> getChildren();

    void setChildren(List<C> children);

    U getOwner();

    void setOwner(U owner);

    T getThread();

    void setThread(T thread);

}