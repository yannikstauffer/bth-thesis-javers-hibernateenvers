package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common;

public interface Comment<P extends Post<? extends Thread<P>, ? extends Comment<P>>> extends Payload {

    P getPost();

    void setPost(P parent);

}
