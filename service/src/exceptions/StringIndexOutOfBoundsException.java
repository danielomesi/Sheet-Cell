package exceptions;

import entities.cell.CellCoordinates;

public class StringIndexOutOfBoundsException extends ServiceException{
    public String getExceptionName() {return "String Index Out Of Bounds";}

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
