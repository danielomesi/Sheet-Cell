package exceptions;

import entities.CellCoordinates;

public class CircleReferenceException extends ServiceException{
    static {
        exceptionName = "Circle Reference";
    }

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
