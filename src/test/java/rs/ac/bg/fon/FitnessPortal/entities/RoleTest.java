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

class RoleTest {

    Role r;
    @BeforeEach
    void setUp() {
        r = new Role();
    }

    @AfterEach
    void tearDown() {
        r = null;
    }

    @Test
    void testRolesNull() {
        assertEquals(null, r.getName());
        assertEquals(null, r.getId());
        assertEquals(null, r.getUsers());
    }

    @Test
    void testRolesNotNull() {
        r= new Role(ApplicationUserRole.ADMIN);

        assertEquals(ApplicationUserRole.ADMIN, r.getName());
    }

    @ParameterizedTest
    @CsvSource({"1", "5", "7", "9"})
    void testSetRoleId(int id) {
        r.setId(id);

        assertEquals(id, r.getId());
    }
    @Test
    void testSetRoleIdThrowsNullPointer() {
        assertThrows(java.lang.NullPointerException.class, () -> r.setId(null));
    }

    @Test
    void setName() {
        r.setName(ApplicationUserRole.ADMIN);
        assertEquals(ApplicationUserRole.ADMIN, r.getName());
    }

    @Test
    void setNameThrowsException() {
        assertThrows(java.lang.NullPointerException.class, () -> r.setName(null));
    }

    @Test
    void setUsers() {
        Set<User> users = new HashSet<>();
        r.setUsers(users);
        assertEquals(users, r.getUsers());
    }

    @Test
    void setUsersThrowsException() {
        assertThrows(java.lang.NullPointerException.class, () -> r.setUsers(null));
    }
}