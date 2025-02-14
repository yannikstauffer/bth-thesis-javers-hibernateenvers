package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.rest;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Post;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Thread;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
public class ThreadDto {
    private Integer id;
    private String title;
    private String content;
    private byte[] attachment;

    private List<PostDto> posts = List.of();

    public <T extends Thread<?>> T update(T thread) {
        thread.setTitle(this.title);
        thread.setContent(this.content);
        thread.setAttachment(this.attachment);

        for (PostDto postDto : this.posts) {
            boolean found = false;
            for (Post<?, ?> comment : thread.getPosts()) {
                if (comment.getId().equals(postDto.getId())) {
                    postDto.update(comment);
                    found = true;
                    break;
                }
            }
            if (!found) {
                log.error("Post with id {} not found in thread {}", postDto.getId(), thread.getId());
            }
        }
        return thread;
    }
}
