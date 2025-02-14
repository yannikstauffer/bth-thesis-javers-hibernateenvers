package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.rest;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Comment;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentDto {
    private Integer id;
    private String content;
    private byte[] attachment;

    public <T extends Comment<?>> T update(T comment) {
        comment.setContent(this.content);
        comment.setAttachment(this.attachment);

        return comment;
    }
}
