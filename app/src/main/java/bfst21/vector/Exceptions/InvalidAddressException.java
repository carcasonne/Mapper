package bfst21.vector.Exceptions;

public class InvalidAddressException extends RuntimeException {
    public InvalidAddressException(){
        super("The address must be complete with street, house number, postcode and city!");
    }
}
