package exceptions;

import entities.coordinates.CellCoordinates;

public class CloneFailureException extends ServiceException{
    public CloneFailureException(String message) {
        super(message);
    }

    public CloneFailureException(String message, CellCoordinates cellCoordinates) {
        super(message, cellCoordinates);
    }

    public CloneFailureException(String message, String input) {
        super(message, input);
    }

    public CloneFailureException(String message, CellCoordinates cellCoordinates, String input) {
        super(message, cellCoordinates, input);
    }

    @Override
    public String getExceptionName() {
        return "Clone Failure";
    }
}
