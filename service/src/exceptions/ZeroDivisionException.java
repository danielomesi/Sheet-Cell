package exceptions;

import entities.CellCoordinates;

public class ZeroDivisionException extends ServiceException{
    public ZeroDivisionException(String message) {
        super(message);
    }

    public ZeroDivisionException(String message, CellCoordinates cellCoordinates) {
        super(message, cellCoordinates);
    }

    public ZeroDivisionException(String message, String input) {
        super(message, input);
    }

    public ZeroDivisionException(String message, CellCoordinates cellCoordinates, String input) {
        super(message, cellCoordinates, input);
    }
}
