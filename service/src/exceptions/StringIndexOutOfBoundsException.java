package exceptions;

import entities.CellCoordinates;

public class StringIndexOutOfBoundsException extends ServiceException{
    public StringIndexOutOfBoundsException(String message) {
        super(message);
    }

    public StringIndexOutOfBoundsException(String message, CellCoordinates cellCoordinates) {
        super(message, cellCoordinates);
    }

    public StringIndexOutOfBoundsException(String message, String input) {
        super(message, input);
    }

    public StringIndexOutOfBoundsException(String message, CellCoordinates cellCoordinates, String input) {
        super(message, cellCoordinates, input);
    }
}
