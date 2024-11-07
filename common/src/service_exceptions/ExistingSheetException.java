package service_exceptions;

import entities.coordinates.Coordinates;

public class ExistingSheetException extends ServiceException {
    public ExistingSheetException(String message) {
        super(message);
    }

    public ExistingSheetException(String message, Coordinates coordinates) {
        super(message, coordinates);
    }

    public ExistingSheetException(String message, String input) {
        super(message, input);
    }

    public ExistingSheetException(String message, Coordinates coordinates, String input) {
        super(message, coordinates, input);
    }

    @Override
    public String getExceptionName() {
        return "Existing Sheet Exception";
    }
}
