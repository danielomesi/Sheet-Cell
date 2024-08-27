package exceptions;

import entities.coordinates.Coordinates;

public class CircleReferenceException extends ServiceException{
    public String getExceptionName() {return "Circle Reference";}

    public CircleReferenceException(String message) {
        super(message);
    }

    public CircleReferenceException(String message, Coordinates coordinates) {
        super(message, coordinates);
    }

    public CircleReferenceException(String message, String input) {
        super(message, input);
    }

    public CircleReferenceException(String message, Coordinates coordinates, String input) {
        super(message, coordinates, input);
    }
}
