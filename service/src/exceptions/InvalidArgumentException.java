package exceptions;

import entities.CellCoordinates;

public class InvalidArgumentException extends ServiceException{
    static {
        exceptionName = "Invalid Argument";
    }
    public InvalidArgumentException(String message) {
        super(message);
    }

    public InvalidArgumentException(String message, CellCoordinates cellCoordinates) {
        super(message, cellCoordinates);
    }

    public InvalidArgumentException(String message, String input) {
        super(message, input);
    }

    public InvalidArgumentException(String message, CellCoordinates cellCoordinates, String input) {
        super(message, cellCoordinates, input);
    }
}
