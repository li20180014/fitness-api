package rs.ac.bg.fon.FitnessPortal.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

/**
 * Class representing a training session provided by a fitness coach.
 *
 * Contains information about duration of training, number of members who can attend and the coach in charge.
 * Class attributes: unique id, date, startTime, endTime, maxSpots, remainingSpots, coach and .
 *
 * @author Lana
 * @version 1.0
 */
@Entity(name = "trainings")
public class Training {

    /**
     * Unique id number representing the primary key in database table. Id is a primary generated auto increment value.
     */
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * Date of the training session.
     */
    @Column(nullable = false)
    private LocalDate date;

    /**
     * Start time of the training session.
     */
    @Column(nullable = false)
    private LocalTime startTime;

    /**
     * End time of the training session.
     */
    @Column(nullable = false)
    private LocalTime endTime;

    /**
     * Maximum number of members who can attend the training session.
     */
    @Column(nullable = false)
    private Integer maxSpots;

    /**
     * Remaining spots for training session.
     */
    @Column(nullable = false)
    private Integer remainingSpots;

    /**
     * Coach in charge of training session.
     */
    @ManyToOne
    @JoinColumn(name = "coach_id", nullable = false)
    private Coach coach;

    /**
     * Representing the scheduled training session by a member.
     */
    @OneToMany(mappedBy = "training", cascade = CascadeType.ALL)
    private Set<Appointment> appointments;

    /**
     * Class constructor, sets attributes to their default values.
     */
    public Training() { }

    /**
     * Returns the training id.
     *
     * @return id as an integer value.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id to entered value.
     *
     * @param id new integer value as id.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Returns the date the training is being held.
     *
     * @return date as LocalDate value.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Sets the date to entered value.
     *
     * @param date new LocalDate value as date.
     */
    public void setDate(LocalDate date) {
        this.date = date;
    }

    /**
     * Returns the time the training starts.
     *
     * @return startTime as LocalTime value.
     */
    public LocalTime getStartTime() {
        return startTime;
    }

    /**
     * Sets startTime to entered value.
     *
     * @param startTime  new LocalTime value as start time.
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Returns the time the training ends.
     *
     * @return endTime as LocalTime value.
     */
    public LocalTime getEndTime() {
        return endTime;
    }

    /**
     * Sets endTime to entered value.
     *
     * @param endTime new LocalTime value as endTime.
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Returns the coach in charge of the training session.
     *
     * @return coach as an object of class Coach.
     */
    public Coach getCoach() {
        return coach;
    }

    /**
     * Sets coach to entered value.
     *
     * @param coach new object of class Coach.
     */
    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    /**
     * Returns maximum number of spots for training session.
     *
     * @return maxSpots as integer value.
     */
    public Integer getMaxSpots() {
        return maxSpots;
    }

    /**
     * Sets max number of spots to entered value.
     *
     * @param maxSpots new integer value as max number of spots.
     */
    public void setMaxSpots(Integer maxSpots) {
        this.maxSpots = maxSpots;
    }

    /**
     * Returns the remaining  number of spots for training session.
     *
     * @return remainingSpots as integer value.
     */
    public Integer getRemainingSpots() {
        return remainingSpots;
    }

    /**
     * Sets remaining spots to entered value.
     *
     * @param remainingSpots new integer value as number of remaining spots.
     */
    public void setRemainingSpots(Integer remainingSpots) {
        this.remainingSpots = remainingSpots;
    }

    /**
     * Returns the list of scheduled appointments for selected training session.
     *
     * @return appointments as set of objects of class Appointment.
     */
    public Set<Appointment> getAppointments() {
        return appointments;
    }

    /**
     * Sets appointments to entered values.
     *
     * @param appointments new set of objects of class Appointment.
     */
    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
    }
}
