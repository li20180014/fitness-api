package rs.ac.bg.fon.FitnessPortal.entities;

import javax.persistence.*;

/**
 * Class representing an appointment as a scheduled training session.
 *
 * Contains information about the member who has scheduled a training session provided by the clubs coach.
 * Class attributes: unique id, member, training, attended.
 *
 * @author Lana
 * @version 1.0
 */
@Entity(name = "appointments")
public class Appointment {

    /**
     * Unique id number representing the primary key in database table. Id is a primary generated auto increment value.
     */
    @Id
    @GeneratedValue
    @Column(name = "appointment_id")
    private Integer id;

    /**
     * Member who has made the appointment - scheduled the training session.
     */
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    /**
     * The training session provided by the clubs coach.
     */
    @ManyToOne
    @JoinColumn(name = "training_id", nullable = false)
    private Training training;

    /**
     * Record of the member's attendance on the scheduled appointment.
     */
    private Boolean attended;

    /**
     * Class constructor, sets attributes to their default values.
     */
    public Appointment() { }

    /**
     * Sets attributes member and training to entered values.
     *
     * @param member
     * @param training
     */
    public Appointment(Member member, Training training) {
        this.member = member;
        this.training = training;
    }

    /**
     * Returns the id number of the scheduled appointment.
     *
     * @return id as an integer value.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets appointment id to entered value.
     *
     * @param id new integer value as id.
     * @throws NullPointerException if provided id is null.
     */
    public void setId(Integer id) {
        if (id == null)
            throw new NullPointerException("Id can not be null");

        this.id = id;
    }

    /**
     * Returns the member who has scheduled the training session.
     *
     * @return member as an object of class Member.
     */
    public Member getMember() {
        return member;
    }

    /**
     * Sets the member of the appointment to entered value.
     *
     * @param member new object of class Member as member.
     * @throws NullPointerException if provided member instance is null.
     */
    public void setMember(Member member) {

        if (member == null)
            throw new NullPointerException("Member can not be null");


        this.member = member;
    }

    /**
     * Returns the scheduled training session.
     *
     * @return training as an object of class Training.
     */
    public Training getTraining() {
        return training;
    }

    /**
     * Sets the appointments training session to entered value.
     *
     * @param training new object of class Training as the appointments training.
     * @throws NullPointerException if provided training instance is null.
     */
    public void setTraining(Training training) {

        if (training == null)
            throw new NullPointerException("Training can not be null");


        this.training = training;
    }

    /**
     * Returns information about the member attendance on the appointment.
     *
     * @return attended as a boolean value.
     */
    public Boolean getAttended() {
        return attended;
    }

    /**
     * Sets attendance to entered value.
     *
     * @param attended new boolean value as attendance.
     * @throws NullPointerException if provided value for attendance is null.
     */
    public void setAttended(Boolean attended){
     if (attended == null)
            throw new NullPointerException("Attendance can not be null");

    this.attended = attended;
    }
}
