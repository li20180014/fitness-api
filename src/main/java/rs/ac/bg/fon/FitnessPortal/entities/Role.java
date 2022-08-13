package rs.ac.bg.fon.FitnessPortal.entities;

import rs.ac.bg.fon.FitnessPortal.security.authorization.ApplicationUserRole;

import javax.persistence.*;
import java.util.Set;

@Entity(name = "roles")
public class Role {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private ApplicationUserRole name;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Role(ApplicationUserRole name) {
        this.name = name;
    }

    public Role() { }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ApplicationUserRole getName() {
        return name;
    }

    public void setName(ApplicationUserRole name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
