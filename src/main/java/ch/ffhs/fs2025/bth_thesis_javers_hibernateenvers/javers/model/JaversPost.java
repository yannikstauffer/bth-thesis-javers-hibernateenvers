package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.model;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.BaseEntity;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Post;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class JaversPost extends BaseEntity implements Post<JaversThread, JaversComment> {

    private String content;
    @Lob
    @Column(columnDefinition="BLOB")
    private byte[] attachment;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<JaversComment> comments = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "thread_id")
    private JaversThread thread;


    @Override
    public Class<JaversComment> getChildType() {
        return JaversComment.class;
    }
}
