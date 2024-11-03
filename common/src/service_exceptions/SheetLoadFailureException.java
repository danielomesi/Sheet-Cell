package service_exceptions;

import entities.coordinates.Coordinates;

public class SheetLoadFailureException extends ServiceException{
    public SheetLoadFailureException(String message) {
        super(message);
    }

    public SheetLoadFailureException(String message, Coordinates coordinates) {
        super(message, coordinates);
    }

    public SheetLoadFailureException(String message, String input) {
        super(message, input);
    }

    public SheetLoadFailureException(String message, Coordinates coordinates, String input) {
        super(message, coordinates, input);
    }

    @Override
    public String getExceptionName() {
        return "Sheet Load Failure";
    }
}
