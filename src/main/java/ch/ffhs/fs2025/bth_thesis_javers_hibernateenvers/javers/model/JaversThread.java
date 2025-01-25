package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.model;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.BaseEntity;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Thread;
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
@Entity(name = "javers_threads")
public class JaversThread extends BaseEntity implements Thread<JaversUser, JaversPost> {

    private String title;

    private String content;
    private byte[] attachment;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private JaversUser owner;

    @OneToMany(mappedBy = "thread", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JaversPost> posts = new ArrayList<>();

    @Override
    public Class<JaversPost> getChildType() {
        return JaversPost.class;
    }
}