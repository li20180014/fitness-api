package rs.ac.bg.fon.FitnessPortal.exception_handling;

public class FutureAppointmentException extends RuntimeException{
    public FutureAppointmentException(){
        super("You can only update attendances of appointments that have passed.");
    }
}
