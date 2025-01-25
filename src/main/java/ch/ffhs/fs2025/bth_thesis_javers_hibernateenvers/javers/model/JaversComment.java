package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.model;

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
@Entity(name = "javers_comments")
public class JaversComment extends BaseEntity implements Comment<JaversUser, JaversPost> {

    private String content;
    private byte[] attachment;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private JaversPost post;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private JaversUser owner;

}