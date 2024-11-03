package service_exceptions;

import entities.coordinates.Coordinates;

public class SheetSaveFailureException extends ServiceException{
    public SheetSaveFailureException(String message) {
        super(message);
    }

    public SheetSaveFailureException(String message, Coordinates coordinates) {
        super(message, coordinates);
    }

    public SheetSaveFailureException(String message, String input) {
        super(message, input);
    }

    public SheetSaveFailureException(String message, Coordinates coordinates, String input) {
        super(message, coordinates, input);
    }

    @Override
    public String getExceptionName() {
        return "Sheet Save Failure";
    }
}
