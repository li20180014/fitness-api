package rs.ac.bg.fon.FitnessPortal.entities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentTest {
    private Appointment a;

    @BeforeEach
    void setUp() {
        a = new Appointment();
    }

    @AfterEach
    void tearDown() {
        a = null;
    }

    @Test
    void testAppointmentNull() {
        assertEquals(null, a.getId());
        assertEquals(null, a.getTraining());
        assertEquals(null, a.getAttended());
        assertEquals(null, a.getMember());

    }

    @Test
    void testAppointmentNotNull() {
        a = new Appointment();
        Member m = new Member();
        Training t = new Training();
        a.setId(1);
        a.setMember(m);
        a.setTraining(t);
        a.setAttended(true);
        assertEquals(1, a.getId());
        assertEquals(t, a.getTraining());
        assertEquals(true, a.getAttended());
        assertEquals(m, a.getMember());

    }
    @Test
    void testAppointmentNotAllAttributes() {
        Member m = new Member();
        Training t = new Training();
        a = new Appointment(m, t);

        assertEquals(null, a.getId());
        assertEquals(t, a.getTraining());
        assertEquals(null, a.getAttended());
        assertEquals(m, a.getMember());

    }

    @ParameterizedTest
    @CsvSource({"1", "5", "7", "9"})
    void testSetAppointmentId(int id) {
        a.setId(id);

        assertEquals(id, a.getId());
    }
    @Test
    void testSetAppointmentIdThrowsNullPointer() {

        assertThrows(java.lang.NullPointerException.class, () -> a.setId(null));
    }


    @Test
    void setMember() {
        Member m = new Member();
        a.setMember(m);


        assertEquals(m, a.getMember());
    }

    @Test
    void setMemberThrowsNullPointer() {

        assertThrows(java.lang.NullPointerException.class, () -> a.setMember(null));
    }

    @Test
    void setTraining() {
        Training  t = new Training();
        a.setTraining(t);


        assertEquals(t, a.getTraining());
    }

    @Test
    void setTrainingThrowsNullPointer() {


        assertThrows(java.lang.NullPointerException.class, () -> a.setTraining(null));
    }

    @Test
    void setAttendance() {
        a.setAttended(true);
        assertEquals(true, a.getAttended());
    }

    @Test
    void setAttendanceThrowsNullPointer() {


        assertThrows(java.lang.NullPointerException.class, () -> a.setAttended(null));
    }

}