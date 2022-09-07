package rs.ac.bg.fon.FitnessPortal.entities;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * Class representing the fitness club's registered member.
 *
 * Contains information unique to every member.
 * Class attributes: appointments.
 *
 * @author Lana
 * @version 1.0
 */
@Entity
@DiscriminatorValue("Member")
public class Member extends User {

    /**
     * Set of appointments members have scheduled with their coach. Default value is null.
     *
     */
    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private Set<Appointment> appointments;

    /**
     * Class constructor, sets attributes to their default values.
     */
    public Member() {
        super();
    }

    /**
     * Sets attributes id, firstName, lastName, email and password  to entered values.
     *
     * @param id
     * @param firstName
     * @param lastName
     * @param email
     * @param password
     */
    public Member(Integer id, String firstName, String lastName, String email, String password) {
        super(id, firstName, lastName, email, password);
    }

    /**
     * Returns members scheduled appointments.
     *
     * @return appointments as a Set of objects of class Appointment.
     */
    public Set<Appointment> getAppointments() {
        return appointments;
    }

    /**
     * Sets the member's scheduled appointments to entered value.
     *
     * @param appointments new set of appointments for member's scheduled appointments.
     * @throws  NullPointerException if provided appointments are null.
     */
    public void setAppointments(Set<Appointment> appointments) {

        if(appointments == null)
        throw new NullPointerException("Appointments must not be null");

        this.appointments = appointments;
    }
}
