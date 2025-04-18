package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.model;

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
import org.hibernate.envers.Audited;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "envers_posts")
@Table(name = "envers_posts", indexes = {
        @Index(name = "idx_envers_thread_id", columnList = "thread_id")
})
@Audited
public class EnversPost extends BaseEntity implements Post<EnversThread, EnversComment> {


    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EnversComment> comments = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "thread_id", nullable = false)
    private EnversThread thread;

    @Override
    public Class<EnversComment> getChildType() {
        return EnversComment.class;
    }
}
