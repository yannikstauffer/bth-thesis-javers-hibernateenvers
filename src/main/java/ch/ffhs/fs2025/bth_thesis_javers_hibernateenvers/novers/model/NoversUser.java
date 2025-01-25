package ch.ffhs.fs2025.bth_thesis_javers_hibernateenvers.novers.model;

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
@Entity(name = "novers_users")
public class NoversUser extends BaseEntity implements User<NoversThread, NoversPost, NoversComment> {

    private String username;

    @OneToMany(mappedBy = "owner")
    private List<NoversComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<NoversThread> threads = new ArrayList<>();

    @OneToMany(mappedBy = "owner")
    private List<NoversPost> posts = new ArrayList<>();

}