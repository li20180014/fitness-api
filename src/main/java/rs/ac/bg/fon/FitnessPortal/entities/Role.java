package rs.ac.bg.fon.FitnessPortal.entities;

import rs.ac.bg.fon.FitnessPortal.security.authorization.ApplicationUserRole;

import javax.persistence.*;
import java.util.Set;
/**
 * Class representing the application user's roles.
 *
 * Class attributes: id, name, users.
 *
 * @author Lana
 * @version 1.0
 */
@Entity(name = "roles")
public class Role {

    /**
     * Unique id number representing the primary key in database table. Id is a primary generated auto increment value.
     */
    @Id
    @GeneratedValue
    private Integer id;

    /**
     *Name of application user's role. Default value is null.
     *
     */
    @Column(nullable = false)
    private ApplicationUserRole name;

    /**
     * Set of User's with associated role.
     *
     */
    @ManyToMany(mappedBy = "roles")
    private Set<User> users;

    public Role(ApplicationUserRole name) {
        this.name = name;
    }

    /**
     * Class constructor, sets attributes to their default values.
     */
    public Role() { }

    /**
     * Returns the id number of associated application role.
     *
     * @return id as an integer.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the application role id to entered value.
     *
     * @param id new integer value for role id.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Returns name of application role.
     *
     * @return name as a value type of enum ApplicationUserRole.
     */
    public ApplicationUserRole getName() {
        return name;
    }

    /**
     * Sets application role name to entered value.
     *
     * @param name new  value of type ApplicationUserRole.
     */
    public void setName(ApplicationUserRole name) {
        this.name = name;
    }

    /**
     * Returns set of users associated with application role.
     *
     * @return set of objects of class User.
     */
    public Set<User> getUsers() {
        return users;
    }

    /**
     * Sets users to new entered values.
     *
     * @param users new Set of objects of class User.
     */
    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
