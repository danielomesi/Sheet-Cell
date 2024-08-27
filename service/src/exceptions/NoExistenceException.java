package exceptions;

import entities.coordinates.Coordinates;

public class NoExistenceException extends ServiceException {

    public String getExceptionName() {return "No Existence";}
    public NoExistenceException(String message) {
        super(message);
    }

    public NoExistenceException(String message, Coordinates coordinates) {
        super(message, coordinates);
    }

    public NoExistenceException(String message, String input) {
        super(message, input);
    }

    public NoExistenceException(String message, Coordinates coordinates, String input) {
        super(message, coordinates, input);
    }
}
