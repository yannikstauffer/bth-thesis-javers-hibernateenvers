package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.envers.model;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.BaseEntity;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.User;
import jakarta.persistence.Entity;
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
@Entity(name = "envers_users")
@Audited
public class EnversUser extends BaseEntity implements User<EnversThread, EnversPost, EnversComment> {

    private String username;

    @OneToMany(mappedBy = "owner")
    private List<EnversThread> threads = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<EnversPost> posts = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<EnversComment> comments = new ArrayList<>();

}