package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.model;

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
@Entity(name = "novers_comments")
@Table(name = "novers_comments", indexes = {
        @Index(name = "idx_novers_post_id", columnList = "post_id")
})
public class NoversComment extends BaseEntity implements Comment<NoversPost> {

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private NoversPost post;

}
