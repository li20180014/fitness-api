package rs.ac.bg.fon.FitnessPortal.entities;

import javax.persistence.*;

/**
 * Class representing the application User's personal profile information.
 *
 * Contains personal information about Users who have created their account and wish to log their height, weight, age and gender.
 * Class attributes: unique id, height, weight, age, gender and User with whom this profile information is associated with.
 *
 * @author Lana
 * @version 1.0
 */
@Entity(name = "users_profile_information")
public class UserProfileInformation {

    /**
     * Unique id number representing the primary key in database table. Id is a primary generated auto increment value.
     */
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * User's height, default value is 0.
     */
    private Integer height;

    /**
     * User's weight, default value is 0.
     */
    private Integer weight;

    /**
     * User's age, default value is 0.
     */
    private Integer age;

    /**
     * User's gender, default value is 0.
     */
    @Enumerated(EnumType.STRING)
    private Gender gender;

    /**
     * User's associated with profile information, default value is null.
     */
    @OneToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Class constructor, sets attributes to their default values.
     */
    public UserProfileInformation() { }

    /**
     * Sets attributes height, weight, age and gender to entered values. Sets other attributes to their default values.
     *
     * @param height
     * @param weight
     * @param age
     * @param gender
     */
    public UserProfileInformation(Integer height, Integer weight, Integer age, Gender gender) {
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.gender = gender;
    }

    /**
     * Sets attributes height, weight, age, gender and User to entered values.
     *
     * @param id
     * @param height
     * @param weight
     * @param age
     * @param gender
     * @param user
     */
    public UserProfileInformation(Integer id, Integer height, Integer weight, Integer age, Gender gender, User user) {
        this.id = id;
        this.height = height;
        this.weight = weight;
        this.age = age;
        this.gender = gender;
        this.user = user;
    }

    /**
     * Returns the user profile id number.
     *
     * @return id as an integer.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the user profile id to entered value.
     *
     * @param id new integer value for user profile id.
     * @throws NullPointerException if provided id is null.
     */
    public void setId(Integer id) {

        if(id== null)
            throw new NullPointerException("Id must not be null");
        this.id = id;
    }

    /**
     * Returns the user associated with profile.
     *
     * @return user as an object of class User.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user associated with profile to entered value.
     *
     * @param user new User entity associated with profile.
     * @throws NullPointerException if provided user is null.
     */
    public void setUser(User user) {
        if(user== null)
            throw new NullPointerException("User must not be null");

        this.user = user;
        user.setUserProfileInformation(this);
    }

    /**
     * Returns the user height.
     *
     * @return height as an integer.
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * Sets users height to entered value.
     *
     * @param height new integer value for users height.
     * @throws NullPointerException if provided height is null.
     */
    public void setHeight(Integer height) {
        if (height == null)
            throw new NullPointerException("Height must not be null");
        this.height = height;
    }

    /**
     * Returns the user weight.
     *
     * @return weight as an integer.
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     * Sets users weight to entered value.
     *
     * @param weight new integer value for users weight.
     */
    public void setWeight(Integer weight) {
        if (weight == null)
            throw new NullPointerException("Weight must not be null");

        this.weight = weight;
    }

    /**
     * Returns the users age.
     *
     * @return age as an integer.
     */
    public Integer getAge() {
        return age;
    }

    /**
     * Sets users age to entered value.
     *
     * @param age new integer value for users age.
     * @throws NullPointerException if provided age is null.
     */
    public void setAge(Integer age) {
        if (age == null)
            throw new NullPointerException("Age must not be null");
        this.age = age;
    }

    /**
     * Returns the users gender .
     *
     * @return gender as a value of Enum type Gender.
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * Sets users gender to entered value.
     *
     * @param gender new enum value of type Gender for users gender.
     * @throws  NullPointerException if provided gender is null.
     */
    public void setGender(Gender gender) {
        if (gender == null)
            throw new NullPointerException("Gender must not be null");

        this.gender = gender;
    }
}
