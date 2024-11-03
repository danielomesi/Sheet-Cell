package service_exceptions;

import entities.coordinates.Coordinates;

public class InvalidRangeException extends ServiceException{
    public String getExceptionName() {return "Invalid Range";}
    public InvalidRangeException(String message) {
        super(message);
    }

    public InvalidRangeException(String message, Coordinates coordinates) {
        super(message, coordinates);
    }

    public InvalidRangeException(String message, String input) {
        super(message, input);
    }

    public InvalidRangeException(String message, Coordinates coordinates, String input) {
        super(message, coordinates, input);
    }
}
