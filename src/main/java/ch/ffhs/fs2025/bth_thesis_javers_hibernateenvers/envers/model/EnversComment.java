package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.model;

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
import org.hibernate.envers.Audited;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "envers_comments")
@Table(name = "envers_comments", indexes = {
        @Index(name = "idx_envers_post_id", columnList = "post_id")
})
@Audited
public class EnversComment extends BaseEntity implements Comment<EnversPost> {

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private EnversPost post;

}
