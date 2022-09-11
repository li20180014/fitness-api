package rs.ac.bg.fon.FitnessPortal.entities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TrainingTest {

    Training t;
    @BeforeEach
    void setUp() {
        t = new Training();
    }

    @AfterEach
    void tearDown() {
        t = null;
    }

    @Test
    void testTrainingNull() {
        assertEquals(null,t.getId());
        assertEquals(null,t.getDate());
        assertEquals(null,t.getCoach());
        assertEquals(null,t.getStartTime());
        assertEquals(null,t.getAppointments());
        assertEquals(null,t.getEndTime());
        assertEquals(null,t.getRemainingSpots());
        assertEquals(null,t.getMaxSpots());
    }

    @ParameterizedTest
    @CsvSource({"1", "5", "7", "9"})
    void testSetTrainingId(int id) {
        t.setId(id);

        assertEquals(id, t.getId());
    }
    @Test
    void testSetTrainingIdThrowsNullPointer() {

        assertThrows(java.lang.NullPointerException.class, () -> t.setId(null));
    }
    @Test
    void setDate() {
        t.setDate(LocalDate.of(2022,10,5));

        assertEquals(LocalDate.of(2022,10,5), t.getDate());
    }

    @Test
    void setDateNullPointer() {
        assertThrows(java.lang.NullPointerException.class, () -> t.setDate(null));
    }

    @Test
    void setStartTime() {
        t.setStartTime(LocalTime.of(14,5));

        assertEquals(LocalTime.of(14,5), t.getStartTime());
    }

    @Test
    void setStartTimePointer() {
        assertThrows(java.lang.NullPointerException.class, () -> t.setStartTime(null));
    }

    @Test
    void setEndTime() {
        t.setEndTime(LocalTime.of(14,5));

        assertEquals(LocalTime.of(14,5), t.getEndTime());
    }

    @Test
    void setEndTimePointer() {
        assertThrows(java.lang.NullPointerException.class, () -> t.setEndTime(null));
    }

    @Test
    void setCoach() {
        Coach c = new Coach();
        t.setCoach(c);

        assertEquals(c, t.getCoach());
    }

    @Test
    void setCoachNullPointer() {
        assertThrows(java.lang.NullPointerException.class, () -> t.setCoach(null));
    }

    @ParameterizedTest
    @CsvSource({"1", "5", "7", "9"})
    void testSetMaxSpots(int maxSPots) {
        t.setMaxSpots(maxSPots);

        assertEquals(maxSPots, t.getMaxSpots());
    }
    @Test
    void testSetMaxSpotsThrowsNullPointer() {

        assertThrows(java.lang.NullPointerException.class, () -> t.setMaxSpots(null));
    }

    @ParameterizedTest
    @CsvSource({"1", "5", "7", "9"})
    void testSetRemainingSpots(int spots) {
        t.setRemainingSpots(spots);

        assertEquals(spots, t.getRemainingSpots());
    }
    @Test
    void testSetRemainingSpotsThrowsNullPointer() {

        assertThrows(java.lang.NullPointerException.class, () -> t.setRemainingSpots(null));
    }

    @Test
    void setAppointments() {
        Set<Appointment> appointments = new HashSet<>();
        t.setAppointments(appointments);

        assertEquals(appointments, t.getAppointments());
    }

    @Test
    void setAppointmentsNullPointer() {
        assertThrows(java.lang.NullPointerException.class, () -> t.setAppointments(null));

    }
}