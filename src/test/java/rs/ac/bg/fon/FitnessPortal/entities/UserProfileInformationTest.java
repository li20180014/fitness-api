package rs.ac.bg.fon.FitnessPortal.entities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class UserProfileInformationTest {

    UserProfileInformation u;

    @BeforeEach
    void setUp() {
        u = new UserProfileInformation();
    }

    @AfterEach
    void tearDown() {
        u = null;
    }

    @Test
    void testUserProfileInfoNull() {
        assertEquals(null, u.getId());
        assertEquals(null, u.getUser());
        assertEquals(null, u.getGender());
        assertEquals(null, u.getAge());
        assertEquals(null, u.getHeight());
        assertEquals(null, u.getWeight());

    }

    @Test
    void testUserProfileInfoNotNull() {
        User user = new User();
        u= new UserProfileInformation(1, 150, 50, 23, Gender.FEMALE, user);

        assertEquals(1, u.getId());
        assertEquals(150, u.getHeight());
        assertEquals(50, u.getWeight());
        assertEquals(23, u.getAge());
        assertEquals(Gender.FEMALE, u.getGender());
        assertEquals(user, u.getUser());


    }

    @Test
    void testUserProfileInfoNotNullAllParams() {
        u= new UserProfileInformation(150, 50, 23, Gender.FEMALE);

        assertEquals(150, u.getHeight());
        assertEquals(50, u.getWeight());
        assertEquals(23, u.getAge());
        assertEquals(Gender.FEMALE, u.getGender());

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

    @Test
    void setUser() {
        User user = new User();
        u.setUser(user);

        assertEquals(user, u.getUser());

    }

    @Test
    void setUserThrowsException() {

        assertThrows(java.lang.NullPointerException.class, () -> u.setUser(null));

    }
    @ParameterizedTest
    @CsvSource({"150", "155", "157", "159"})
    void testSetHeight(int height) {
        u.setHeight(height);

        assertEquals(height, u.getHeight());
    }
    @Test
    void testSetHeightThrowsNullPointer() {

        assertThrows(java.lang.NullPointerException.class, () -> u.setHeight(null));
    }

    @ParameterizedTest
    @CsvSource({"50", "55", "57", "59"})
    void testSetWeight(int weight) {
        u.setWeight(weight);

        assertEquals(weight, u.getWeight());
    }
    @Test
    void testSetWeightThrowsNullPointer() {

        assertThrows(java.lang.NullPointerException.class, () -> u.setWeight(null));
    }

    @ParameterizedTest
    @CsvSource({"50", "55", "57", "59"})
    void testSetAge(int age) {
        u.setAge(age);

        assertEquals(age, u.getAge());
    }
    @Test
    void testSetAgeThrowsNullPointer() {

        assertThrows(java.lang.NullPointerException.class, () -> u.setAge(null));
    }

    @Test
    void setGender() {
        u.setGender(Gender.FEMALE);
        assertEquals(Gender.FEMALE, u.getGender());

    }

    @Test
    void setGenderThrowsNullPointer() {
        assertThrows(java.lang.NullPointerException.class, () -> u.setGender(null));


    }
}