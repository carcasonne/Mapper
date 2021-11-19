package bfst21.vector.Exceptions;

public class InvalidPathException extends RuntimeException{
    public InvalidPathException(){
        super("There is no path between the two specified addresses you chose!");
    }
}
