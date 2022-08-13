package rs.ac.bg.fon.FitnessPortal.exception_handling;

public class InvalidUserException extends RuntimeException{

    public InvalidUserException(){
        super("You can only use the crud operations on your account");
    }
}
