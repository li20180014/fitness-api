package rs.ac.bg.fon.FitnessPortal.exception_handling;

public class BlogNotFoundException extends RuntimeException{
    public BlogNotFoundException(int id) {
        super("Blog with id " + id + " not found");
    }

}
