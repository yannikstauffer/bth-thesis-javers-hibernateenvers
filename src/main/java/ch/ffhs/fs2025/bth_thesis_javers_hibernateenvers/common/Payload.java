package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common;

public interface Payload {

    Integer getId();

    String getContent();

    void setContent(String content);

    byte[] getAttachment();

    void setAttachment(byte[] data);
}
