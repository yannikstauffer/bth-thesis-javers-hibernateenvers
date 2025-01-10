package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common;

import java.util.List;

public interface User<C extends Comment<?, ?, ?>, T extends Thread<?, ?>> {

    String getUsername();

    void setUsername(String username);

    List<C> getComments();

    void setComments(List<C> comments);

    List<T> getThreads();

    void setThreads(List<T> threads);
}