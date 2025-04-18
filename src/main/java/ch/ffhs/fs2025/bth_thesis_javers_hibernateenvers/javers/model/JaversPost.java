package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.model;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.BaseEntity;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Post;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "javers_posts")
@Table(name = "javers_posts", indexes = {
        @Index(name = "idx_javers_thread_id", columnList = "thread_id")
})
public class JaversPost extends BaseEntity implements Post<JaversThread, JaversComment> {

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JaversComment> comments = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "thread_id", nullable = false)
    private JaversThread thread;


    @Override
    public Class<JaversComment> getChildType() {
        return JaversComment.class;
    }
}
