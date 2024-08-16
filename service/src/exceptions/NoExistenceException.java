package exceptions;

import entities.CellCoordinates;

public class NoExistenceException extends ServiceException {

    static {
        exceptionName = "No Existence";
    }
    public NoExistenceException(String message) {
        super(message);
    }

    public NoExistenceException(String message, CellCoordinates cellCoordinates) {
        super(message, cellCoordinates);
    }

    public NoExistenceException(String message, String input) {
        super(message, input);
    }

    public NoExistenceException(String message, CellCoordinates cellCoordinates, String input) {
        super(message, cellCoordinates, input);
    }
}
