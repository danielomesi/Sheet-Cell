package exceptions;

import entities.CellCoordinates;

public class CellOutOfBoundsException extends ServiceException{
    static {
        exceptionName = "Cell Out Of Bounds";
    }
    public CellOutOfBoundsException(String message) {
        super(message);
    }

    public CellOutOfBoundsException(String message, CellCoordinates cellCoordinates) {
        super(message, cellCoordinates);
    }

    public CellOutOfBoundsException(String message, String input) {
        super(message, input);
    }

    public CellOutOfBoundsException(String message, CellCoordinates cellCoordinates, String input) {
        super(message, cellCoordinates, input);
    }
}
