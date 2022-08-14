package rs.ac.bg.fon.FitnessPortal.entities;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@DiscriminatorValue("Member")
public class Member extends User {

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private Set<Appointment> appointments;
    public Member() {
        super();
    }

    public Member(Integer id, String firstName, String lastName, String email, String password) {
        super(id, firstName, lastName, email, password);
    }

    public Set<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(Set<Appointment> appointments) {
        this.appointments = appointments;
    }
}
