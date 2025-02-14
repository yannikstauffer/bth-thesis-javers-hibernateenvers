package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.rest;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Comment;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Post;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
public class PostDto {
    private Integer id;
    private String content;
    private byte[] attachment;

    private List<CommentDto> comments = List.of();

    public <T extends Post<?, ?>> T update(T post) {
        post.setContent(this.content);
        post.setAttachment(this.attachment);

        for (CommentDto commentDto : this.comments) {
            boolean found = false;
            for (Comment<?> comment : post.getComments()) {
                if (comment.getId().equals(commentDto.getId())) {
                    commentDto.update(comment);
                    found = true;
                    break;
                }
            }
            if (!found) {
                log.error("Comment with id {} not found in post {}", commentDto.getId(), post.getId());
            }
        }

        return post;
    }
}
