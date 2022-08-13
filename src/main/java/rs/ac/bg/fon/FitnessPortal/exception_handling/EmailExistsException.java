package rs.ac.bg.fon.FitnessPortal.exception_handling;

public class EmailExistsException extends RuntimeException{

    public EmailExistsException(String email){
        super("User with the email " + email + " already exists.");
    }
}
