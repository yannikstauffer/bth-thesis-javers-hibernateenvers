package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.model;

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

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "javers_comments")
public class JaversComment extends BaseEntity implements Comment<JaversComment, JaversThread, JaversUser> {

    private String content;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private JaversComment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JaversComment> children = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private JaversUser owner;

    @ManyToOne
    @JoinColumn(name = "thread_id")
    private JaversThread thread;

}