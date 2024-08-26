package exceptions;

import entities.coordinates.CellCoordinates;

public class CellOutOfBoundsException extends ServiceException{
    public String getExceptionName() {return "Cell Out Of Bounds";}
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
