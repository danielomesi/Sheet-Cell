package service_exceptions;

import entities.coordinates.Coordinates;

public class CloneFailureException extends ServiceException{
    public CloneFailureException(String message) {
        super(message);
    }

    public CloneFailureException(String message, Coordinates coordinates) {
        super(message, coordinates);
    }

    public CloneFailureException(String message, String input) {
        super(message, input);
    }

    public CloneFailureException(String message, Coordinates coordinates, String input) {
        super(message, coordinates, input);
    }

    @Override
    public String getExceptionName() {
        return "Clone Failure";
    }
}
