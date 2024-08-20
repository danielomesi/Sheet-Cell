package exceptions;

import entities.cell.CellCoordinates;

public class InvalidArgumentException extends ServiceException{

    public String getExceptionName() {return "Invalid Argument";}

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
