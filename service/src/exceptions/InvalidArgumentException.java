package exceptions;

import entities.coordinates.Coordinates;

public class InvalidArgumentException extends ServiceException{

    public String getExceptionName() {return "Invalid Argument";}

    public InvalidArgumentException(String message) {
        super(message);
    }

    public InvalidArgumentException(String message, Coordinates coordinates) {
        super(message, coordinates);
    }

    public InvalidArgumentException(String message, String input) {
        super(message, input);
    }

    public InvalidArgumentException(String message, Coordinates coordinates, String input) {
        super(message, coordinates, input);
    }
}
