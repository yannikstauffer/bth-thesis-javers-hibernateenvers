package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common;

import java.util.List;

public interface User<T extends Thread<?, P>, P extends Post<?, T, C>, C extends Comment<?, P>> {

    String getUsername();

    void setUsername(String username);

    List<C> getComments();

    void setComments(List<C> comments);

    List<T> getThreads();

    void setThreads(List<T> threads);

    List<P> getPosts();

    void setPosts(List<P> posts);
}