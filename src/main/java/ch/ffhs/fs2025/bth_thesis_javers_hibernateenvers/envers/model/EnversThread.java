package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.model;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.BaseEntity;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.Thread;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
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
@Entity(name = "envers_threads")
@Audited
public class EnversThread extends BaseEntity implements Thread<EnversUser, EnversPost> {

    private String title;

    private String content;
    @Lob
    private byte[] attachment;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private EnversUser owner;

    @OneToMany(mappedBy = "thread", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EnversPost> posts = new ArrayList<>();

    @Override
    public Class<EnversPost> getChildType() {
        return EnversPost.class;
    }
}