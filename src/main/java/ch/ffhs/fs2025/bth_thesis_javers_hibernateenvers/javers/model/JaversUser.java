package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.javers.model;

import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.BaseEntity;
import ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.common.User;
import jakarta.persistence.Entity;
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
@Entity(name = "javers_users")
public class JaversUser extends BaseEntity implements User<JaversThread, JaversPost, JaversComment> {

    private String username;

    @OneToMany(mappedBy = "owner")
    private List<JaversThread> threads = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<JaversPost> posts = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<JaversComment> comments = new ArrayList<>();

}