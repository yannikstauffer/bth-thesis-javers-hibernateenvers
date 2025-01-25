package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.model;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.BaseEntity;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Comment;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "novers_comments")
public class NoversComment extends BaseEntity implements Comment<NoversUser, NoversPost> {

    private String content;
    private byte[] attachment;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private NoversPost post;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private NoversUser owner;


}