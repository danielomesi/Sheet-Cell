package exceptions;

import entities.coordinates.CellCoordinates;

public class SheetLoadFailureException extends ServiceException{
    public SheetLoadFailureException(String message) {
        super(message);
    }

    public SheetLoadFailureException(String message, CellCoordinates cellCoordinates) {
        super(message, cellCoordinates);
    }

    public SheetLoadFailureException(String message, String input) {
        super(message, input);
    }

    public SheetLoadFailureException(String message, CellCoordinates cellCoordinates, String input) {
        super(message, cellCoordinates, input);
    }

    @Override
    public String getExceptionName() {
        return "Sheet Load Failure";
    }
}
