package rs.ac.bg.fon.FitnessPortal.exception_handling;

public class AdminCannotBeModifiedException extends RuntimeException{

    public AdminCannotBeModifiedException(){
        super("Initial admin cannot be modified");
    }
}
