package rs.ac.bg.fon.FitnessPortal.entities;

import javax.persistence.*;
import java.util.Set;

/**
 * Class representing the employed fitness coach.
 *
 * Contains personal information unique to every coach working in fitness centre.
 * Class attributes: yearsOfExperience, imageSrc, biography and trainings.
 *
 * @author Lana
 * @version 1.0
 */
@Entity
@DiscriminatorValue("Coach")
public class Coach extends User {

    /**
     * Number of years of experience working as a coach, default value is 0.
     */
    private Integer yearsOfExperience;

    /**
     * Image link referring to coach's profile image. Default value is null.
     *
     */
    private String imageSrc;

    /**
     * Longer text about coach's biography, default value is null.
     *
     */
    @Column(columnDefinition = "TEXT")
    private String biography;

    /**
     * Set of coach's available training sessions.
     *
     */
    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL)
    private Set<Training> trainings;

    /**
     * Class constructor, sets attributes to their default values.
     */
    public Coach() {
        super();
    }

    /**
     * Sets attributes id, firstName, lastName, email, password, yearsOfExperience, imageSrc and biography to entered values.
     *
     * @param id
     * @param firstName
     * @param lastName
     * @param email
     * @param password
     * @param yearsOfExperience
     * @param imageSrc
     * @param biography
     */
    public Coach(Integer id, String firstName, String lastName, String email, String password, Integer yearsOfExperience, String imageSrc, String biography) {
        super(id, firstName, lastName, email, password);
        this.yearsOfExperience = yearsOfExperience;
        this.imageSrc = imageSrc;
        this.biography = biography;
    }

    /**
     * Returns coach's years of working experience.
     *
     * @return years of experience as an integer.
     */
    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    /**
     * Sets the coach's years of experience to entered value.
     *
     * @param yearsOfExperience new integer value for coach's years of experience.
     * @throws NullPointerException if provided years of experience are null.
     */
    public void setYearsOfExperience(Integer yearsOfExperience) {

        if(yearsOfExperience == null)
            throw new NullPointerException("Years of experience must not be null");

            this.yearsOfExperience = yearsOfExperience;
    }

    /**
     * Returns coach's profile image link.
     *
     * @return image source link as a string.
     */
    public String getImageSrc() {
        return imageSrc;
    }
    /**
     * Sets the coach's years of experience to entered value.
     *
     * @param imageSrc new string value for coach's image source link.
     * @throws NullPointerException if provided image source is null.
     * @throws IllegalArgumentException if provided image source is an empty string.
     */
    public void setImageSrc(String imageSrc) {

        if (imageSrc == null)
            throw new NullPointerException("Image source must not be null");

        if (imageSrc.isEmpty())
            throw new IllegalArgumentException("Image source must not be an empty string");

        this.imageSrc = imageSrc;
    }

    /**
     * Returns coach's biography.
     *
     * @return biography as a string.
     */
    public String getBiography() {
        return biography;
    }

    /**
     * Sets the coach's biography to entered value.
     *
     * @param biography new string value for coach's biography.
     * @throws NullPointerException if provided biography is null.
     * @throws IllegalArgumentException if provided biography is an empty string.
     */
    public void setBiography(String biography) {
        if (biography == null)
            throw new NullPointerException("Biography must not be null");

        if (biography.isEmpty())
            throw new IllegalArgumentException("Biography must not be an empty string");

        this.biography = biography;
    }

    /**
     * Returns coach's available training.
     *
     * @return trainings as a Set of objects of class Training.
     */
    public Set<Training> getTrainings() {
        return trainings;
    }

    /**
     * Sets the coach's available trainings to entered values.
     *
     * @param trainings new Set of Trainings for coach's available trainings.
     * @throws NullPointerException if provided trainings are null.
     */
    public void setTrainings(Set<Training> trainings) {
        if(trainings == null)
            throw new NullPointerException("Trainings must not be null");
        this.trainings = trainings;
    }
}
