package exceptions;

import entities.cell.CellCoordinates;

public class CircleReferenceException extends ServiceException{
    public String getExceptionName() {return "Circle Reference";}

    public CircleReferenceException(String message) {
        super(message);
    }

    public CircleReferenceException(String message, CellCoordinates cellCoordinates) {
        super(message, cellCoordinates);
    }

    public CircleReferenceException(String message, String input) {
        super(message, input);
    }

    public CircleReferenceException(String message, CellCoordinates cellCoordinates, String input) {
        super(message, cellCoordinates, input);
    }
}
