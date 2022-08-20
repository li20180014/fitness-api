package rs.ac.bg.fon.FitnessPortal.security.authorization;

/**
 * Enum representing allowed values for application user's roles.
 * @see #USER
 * @see #ADMIN
 * @see #COACH
 *
 * @author Lana
 * @version 1.0
 */
public enum ApplicationUserRole {

    /**
     * Members who register on the application have a role of User.
     *
     */
    USER,
    /**
     * User in charge of application organization and updating has a role of Admin.
     *
     */
    ADMIN,
    /**
     * Fitness employed coaches using the application have a role of Coach.
     *
     */
    COACH
}
