package exceptions;

import entities.coordinates.CellCoordinates;

public class SheetSaveFailureException extends ServiceException{
    public SheetSaveFailureException(String message) {
        super(message);
    }

    public SheetSaveFailureException(String message, CellCoordinates cellCoordinates) {
        super(message, cellCoordinates);
    }

    public SheetSaveFailureException(String message, String input) {
        super(message, input);
    }

    public SheetSaveFailureException(String message, CellCoordinates cellCoordinates, String input) {
        super(message, cellCoordinates, input);
    }

    @Override
    public String getExceptionName() {
        return "Sheet Save Failure";
    }
}
