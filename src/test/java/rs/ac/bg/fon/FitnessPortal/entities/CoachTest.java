package rs.ac.bg.fon.FitnessPortal.entities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CoachTest {

    Coach c;

    @BeforeEach
    void setUp() {
        c= new Coach();
    }

    @AfterEach
    void tearDown() {
        c= null;
    }

    @Test
    void testCoachNull() {
        assertEquals(null, c.getId());
        assertEquals(null, c.getFirstName());
        assertEquals(null, c.getLastName());
        assertEquals(null, c.getEmail());
        assertEquals(null, c.getPassword());
        assertEquals(null, c.getYearsOfExperience());
        assertEquals(null, c.getImageSrc());
        assertEquals(null, c.getBiography());

    }

    @Test
    void testCoachNotNull() {
        c = new Coach(1, "Lana", "Ilic", "lana@gmail.com", "password", 10, "img", "biography");

        assertEquals(1, c.getId());
        assertEquals("Lana", c.getFirstName());
        assertEquals("Ilic", c.getLastName());
        assertEquals("lana@gmail.com", c.getEmail());
        assertEquals("password", c.getPassword());
        assertEquals(10, c.getYearsOfExperience());
        assertEquals("img", c.getImageSrc());
        assertEquals("biography", c.getBiography());

    }

    @ParameterizedTest
    @CsvSource({"1", "5", "7", "9"})
    void testSetYearsOfExperience(int yearsOfExperience) {
        c.setYearsOfExperience(yearsOfExperience);

        assertEquals(yearsOfExperience, c.getYearsOfExperience());
    }
    @Test
    void testSetYearsOfExperienceThrowsNullPointer() {

        assertThrows(java.lang.NullPointerException.class, () -> c.setYearsOfExperience(null));
    }

    @ParameterizedTest
    @CsvSource({"Test1", "Test2", "Test3", "Test4"})
    void testSetCoachImageSource(String source) {
        c.setImageSrc(source);

        assertEquals(source, c.getImageSrc());
    }
    @Test
    void testSetCoachImageSourceThrowsNullPointer() {

        assertThrows(java.lang.NullPointerException.class, () -> c.setImageSrc(null));
    }

    @Test
    void testSetCoachImageSrcEmptyString() {
        assertThrows(java.lang.IllegalArgumentException.class,
                () -> c.setImageSrc("") );
    }

    @ParameterizedTest
    @CsvSource({"Test1", "Test2", "Test3", "Test4"})
    void testSetCoachBio(String biography) {
        c.setBiography(biography);

        assertEquals(biography, c.getBiography());
    }
    @Test
    void testSetCoachBioThrowsNullPointer() {

        assertThrows(java.lang.NullPointerException.class, () -> c.setBiography(null));
    }

    @Test
    void testSetCoachBioEmptyString() {
        assertThrows(java.lang.IllegalArgumentException.class,
                () -> c.setBiography("") );
    }

    @Test
    void setTrainings() {
        Set<Training> trainings = new HashSet<>();
        c.setTrainings(trainings);

        assertEquals(trainings, c.getTrainings());

    }

    @Test
    void setTrainingsNullPointer() {
        assertThrows(java.lang.NullPointerException.class, () -> c.setTrainings(null));


    }
}