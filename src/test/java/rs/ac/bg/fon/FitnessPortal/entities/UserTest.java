package rs.ac.bg.fon.FitnessPortal.entities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import rs.ac.bg.fon.FitnessPortal.security.authorization.ApplicationUserRole;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    User u;
    @BeforeEach
    void setUp() {
        u = new User();
    }

    @AfterEach
    void tearDown() {
        u = null;
    }

    @Test
    void testUserNull() {
        assertEquals(null, u.getId());
        assertEquals(null, u.getFirstName());
        assertEquals(null, u.getLastName());
        assertEquals(null, u.getEmail());
        assertEquals(null, u.getPassword());
    }

    @Test
    void testUserNotNull() {
        u= new User(1, "Lana", "Ilic", "lana@gmail.com", "password");

        assertEquals(1, u.getId());
        assertEquals("Lana", u.getFirstName());
        assertEquals("Ilic", u.getLastName());
        assertEquals("lana@gmail.com", u.getEmail());
        assertEquals("password", u.getPassword());

    }

    @ParameterizedTest
    @CsvSource({"1", "5", "7", "9"})
    void testSetUserId(int id) {
        u.setId(id);

        assertEquals(id, u.getId());
    }
    @Test
    void testSetUserIdThrowsNullPointer() {

        assertThrows(java.lang.NullPointerException.class, () -> u.setId(null));
    }

    @ParameterizedTest
    @CsvSource({"Name1", "Name2", "Name3", "Name4"})
    void testSetUserFirstName(String name) {
        u.setFirstName(name);

        assertEquals(name, u.getFirstName());
    }
    @Test
    void testSetUserFirstNameThrowsNullPointer() {

        assertThrows(java.lang.NullPointerException.class, () -> u.setFirstName(null));
    }

    @Test
    void testSetUserFirstNameEmptyString() {
        assertThrows(java.lang.IllegalArgumentException.class,
                () -> u.setFirstName("") );
    }

    @ParameterizedTest
    @CsvSource({"Name1", "Name2", "Name3", "Name4"})
    void testSetUserLastName(String name) {
        u.setLastName(name);

        assertEquals(name, u.getLastName());
    }
    @Test
    void testSetUserLastNameThrowsNullPointer() {

        assertThrows(java.lang.NullPointerException.class, () -> u.setLastName(null));
    }

    @Test
    void testSetUserLastNameEmptyString() {
        assertThrows(java.lang.IllegalArgumentException.class,
                () -> u.setLastName("") );
    }

    @ParameterizedTest
    @CsvSource({"Email1", "Email2", "Email3", "Email4"})
    void testSetUserEmail(String email) {
        u.setEmail(email);

        assertEquals(email, u.getEmail());
    }
    @Test
    void testSetUserEmailThrowsNullPointer() {

        assertThrows(java.lang.NullPointerException.class, () -> u.setEmail(null));
    }

    @Test
    void testSetUserEmailEmptyString() {
        assertThrows(java.lang.IllegalArgumentException.class,
                () -> u.setEmail("") );
    }

    @ParameterizedTest
    @CsvSource({"P1", "P2", "P3", "P4"})
    void testSetUserPassword(String pw) {
        u.setPassword(pw);

        assertEquals(pw, u.getPassword());
    }
    @Test
    void testSetUserPasswordThrowsNullPointer() {

        assertThrows(java.lang.NullPointerException.class, () -> u.setPassword(null));
    }

    @Test
    void testSetUserPasswordEmptyString() {
        assertThrows(java.lang.IllegalArgumentException.class,
                () -> u.setPassword("") );
    }

    @Test
    void setUseRoles() {
        Set<Role> roles = new HashSet<>();
        u.setRoles(roles);

        assertEquals(roles, u.getRoles());
    }

    @Test
    void addUserRole() {
        u.addRole(new Role(ApplicationUserRole.ADMIN));
        Set<Role> roles = u.getRoles();

        assertEquals(1, u.getRoles().size());
    }

    @Test
    void setUserRolesThrowsException() {

        assertThrows(java.lang.NullPointerException.class, () -> u.setRoles(null));

    }

    @ParameterizedTest
    @CsvSource({"535dgyg", "435tfdg", "4245gdg", "gdhbgdh424"})
    void testSetUserVerification(String verification) {
        u.setVerificationCode(verification);

        assertEquals(verification, u.getVerificationCode());
    }

    @Test
    void setUserEnabled() {
        u.setEnabled(true);
        assertEquals(true, u.getEnabled());

    }

    @Test
    void setUserEnabledThrowsException() {
        assertThrows(java.lang.NullPointerException.class, () -> u.setEnabled(null));

    }

    @Test
    void setUserProfile() {
        UserProfileInformation userProfileInformation = new UserProfileInformation();
        u.setUserProfileInformation(userProfileInformation);

        assertEquals(userProfileInformation, u.getUserProfileInformation());

    }

    @Test
    void setUserProfileThrowsException() {
        assertThrows(java.lang.NullPointerException.class, () -> u.setUserProfileInformation(null));
    }
}