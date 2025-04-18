package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.model;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.BaseEntity;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Comment;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "javers_comments")
@Table(name = "javers_comments", indexes = {
        @Index(name = "idx_javers_post_id", columnList = "post_id")
})
public class JaversComment extends BaseEntity implements Comment<JaversPost> {

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private JaversPost post;

}
