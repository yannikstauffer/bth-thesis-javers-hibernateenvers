package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.model;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.BaseEntity;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Comment;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "envers_comments")
@Audited
public class EnversComment extends BaseEntity implements Comment<EnversComment, EnversThread, EnversUser> {

    protected String content;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private EnversComment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EnversComment> children = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private EnversUser owner;

    @ManyToOne
    @JoinColumn(name = "thread_id")
    private EnversThread thread;

}