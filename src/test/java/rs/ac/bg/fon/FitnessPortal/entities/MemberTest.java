package rs.ac.bg.fon.FitnessPortal.entities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    Member m;

    @BeforeEach
    void setUp() {
        m = new Member();
    }

    @AfterEach
    void tearDown() {
        m= null;
    }

    @Test
    void testMemberNull() {
        assertEquals(null, m.getId());
        assertEquals(null, m.getFirstName());
        assertEquals(null, m.getLastName());
        assertEquals(null, m.getEmail());
        assertEquals(null, m.getPassword());
        assertEquals(null, m.getAppointments());


    }

    @Test
    void testMemberNotNull() {
        m = new Member(1, "Lana", "Ilic", "lana@gmail.com", "lana@gmail.com");

        assertEquals(1, m.getId());
        assertEquals("Lana", m.getFirstName());
        assertEquals("Ilic", m.getLastName());
        assertEquals("lana@gmail.com", m.getEmail());

    }

    @Test
    void setAppointments() {
        Set<Appointment> appointments = new HashSet<>();
        m.setAppointments(appointments);

        assertEquals(appointments, m.getAppointments());
    }

    @Test
    void setAppointmentsNullPointer() {
        assertThrows(java.lang.NullPointerException.class, () -> m.setAppointments(null));

    }
}