package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.model;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.BaseEntity;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Comment;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
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
@Audited
public class EnversComment extends BaseEntity implements Comment<EnversPost> {

    private String content;
    @Lob
    @Column(columnDefinition="BLOB")
    private byte[] attachment;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private EnversPost post;

}
