package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common;

public interface Payload {

    String getContent();

    void setContent(String content);

    byte[] getAttachment();

    void setAttachment(byte[] data);
}