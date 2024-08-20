package exceptions;

import entities.cell.CellCoordinates;

public class NoExistenceException extends ServiceException {

    public String getExceptionName() {return "No Existence";}
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
