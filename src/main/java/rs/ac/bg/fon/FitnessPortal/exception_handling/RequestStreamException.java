package rs.ac.bg.fon.FitnessPortal.exception_handling;

public class RequestStreamException extends RuntimeException{

    public RequestStreamException(){
        super("There is a problem with the request stream. Please try again");
    }
}
