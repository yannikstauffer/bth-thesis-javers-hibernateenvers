package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common;

import java.util.List;

public interface Thread<C extends Comment<?, ?, ?>, U extends User<?, ?>> {

    String getTitle();

    void setTitle(String title);

    String getContent();

    void setContent(String content);

    U getOwner();

    void setOwner(U owner);

    List<C> getComments();

    void setComments(List<C> comments);

}