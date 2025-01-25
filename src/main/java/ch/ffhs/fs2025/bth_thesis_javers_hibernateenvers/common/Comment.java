package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common;

public interface Comment<U extends User<?, P, ?>, P extends Post<U, ?, ?>> extends Payload {

    P getPost();

    void setPost(P parent);

    U getOwner();

    void setOwner(U owner);

}