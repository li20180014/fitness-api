package rs.ac.bg.fon.FitnessPortal.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Class representing the application user.
 *
 * Contains personal information unique to every registering user.
 * Class attributes: unique id, firstName, lastName, email, password, verification code, set of user roles and unique profile information.
 *
 * @author Lana
 * @version 1.0
 */
@Entity(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type")
@DiscriminatorValue("User")
public class User {

    /**
     * Unique id number representing the primary key in database table. Id is a primary generated auto increment value.
     */
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * Users first name, default value is null.
     */
    @Column(nullable = false)
    private String firstName;

    /**
     * Users last name, default value is null.
     */
    @Column(nullable = false)
    private String lastName;

    /**
     * Users emil address, default value is null.
     */
    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Users password used for authentication for the application, default value is null;
     */
    @Column(nullable = false)
    private String password;

    /**
     * Verification code used to verify the Users account with email verification.
     */
    @Column(name = "verification_code", length = 64)
    private String verificationCode;

    /**
     * Boolean value used to verify if the Users account has passed email verficiation, default value is false.
     */
    @Column(nullable = false)
    private Boolean enabled = false;

    /**
     * Set of User roles. User can be a site admin, coach or user. Default value is null.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    /**
     * Specific profile information unique to every application User. Default value is null.
     */
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private UserProfileInformation userProfileInformation;


    /**
     * Class constructor, sets attributes to their default values.
     */
    public User() { }

    /**
     * Sets attributes id, firstName, lastName, email and password to entered values. Sets other attributes to their default values.
     *
     * @param id
     * @param firstName
     * @param lastName
     * @param email
     * @param password
     */
    public User(Integer id, String firstName, String lastName, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    /**
     * Returns the User's id number.
     *
     * @return id as an integer.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the User's id to entered value.
     *
     * @param id new integer value for User's id.
     * @throws NullPointerException if provided id is null.
     */
    public void setId(Integer id) {
        if(id==null) throw new NullPointerException("Id must not be null");

        this.id = id;
    }

    /**
     * Returns the User's first name.
     *
     * @return firstName as a string.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the Users first name to entered value.
     *
     * @param firstName new string value for User's first name.
     * @throws NullPointerException if provided first name is null.
     * @throws IllegalArgumentException if provided first name is an empty string.
     */
    public void setFirstName(String firstName) {

        if (firstName == null)
            throw new NullPointerException("First name must not be null");

        if (firstName.isEmpty())
            throw new IllegalArgumentException("First name must not be an empty string");
        this.firstName = firstName;
    }

    /**
     * Returns the User's last name.
     *
     * @return lastName as a string.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the Users last name to entered value.
     *
     * @param lastName new string value for User's last name.
     * @throws NullPointerException if provided last name is null.
     * @throws IllegalArgumentException if provided last name is an empty string.
     */
    public void setLastName(String lastName) {


        if (lastName == null)
            throw new NullPointerException("Last name must not be null");

        if (lastName.isEmpty())
            throw new IllegalArgumentException("Last name must not be an empty string");
        this.lastName = lastName;
    }

    /**
     * Returns the User's email address.
     *
     * @return email as a string.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the User's email to entered value.
     *
     * @param email new string value for User's email.
     * @throws NullPointerException if provided email is null.
     * @throws IllegalArgumentException if provided email is an empty string.
     */
    public void setEmail(String email) {
        if (email == null)
            throw new NullPointerException("Email must not be null");

        if (email.isEmpty())
            throw new IllegalArgumentException("Email must not be an empty string");

        this.email = email;
    }

    /**
     * Returns the User's password.
     *
     * @return password as a string.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the User's password to new entered value.
     *
     * @param password new string value for User's password.
     * @throws NullPointerException if provided password is null.
     * @throws IllegalArgumentException if provided password is an empty string.
     */
    public void setPassword(String password) {
        if (password == null)
            throw new NullPointerException("Password must not be null");

        if (password.isEmpty())
            throw new IllegalArgumentException("Password must not be an empty string");

        this.password = password;
    }

    /**
     * Returns the User's application roles.
     *
     * @return roles as a Set of enum Role.
     */
    public Set<Role> getRoles() {
        return roles;
    }

    /**
     * Sets the User roles to new entered values.
     *
     * @param roles new Set of Role values User application roles.
     * @throws NullPointerException if provided roles are null.
     */
    public void setRoles(Set<Role> roles) {
        if (roles == null)
            throw new NullPointerException("Application roles must not be null");

        this.roles = roles;
    }

    /**
     * Adds new role to User's application roles.
     *
     * @param role
     */
    public void addRole(Role role){
        roles.add(role);
    }


    /**
     * Returns the User's verification code.
     *
     * @return verificationCode as a string value.
     */
    public String getVerificationCode() {
        return verificationCode;
    }

    /**
     * Sets verification code to new entered value.
     *
     * @param verificationCode new string value for verification code.
     */
    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    /**
     *Returns boolean value if User's account is enabled, verified.
     *
     * @return enabled value as a boolean.
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * Sets User's account to new value for enabled field.
     *
     * @param enabled new boolean value for enabled field.
     * @throws NullPointerException if provided value is null.
     */
    public void setEnabled(Boolean enabled) {

        if (enabled == null) throw new NullPointerException("Provided value must not be null");

        this.enabled = enabled;
    }

    /**
     * Returns User's profile information.
     *
     * @return User profile information as an Object.
     */
    public UserProfileInformation getUserProfileInformation() {
        return userProfileInformation;
    }

    /**
     * Sets User profile information to new Object value containing profile information.
     *
     * @param userProfileInformation new Object value containing User profile information.
     *  @throws  NullPointerException if provided profile information is null.
     */
    public void setUserProfileInformation(UserProfileInformation userProfileInformation) {
        if (userProfileInformation == null) throw new NullPointerException("Provided profile information must not be null");

        this.userProfileInformation = userProfileInformation;
    }
}
