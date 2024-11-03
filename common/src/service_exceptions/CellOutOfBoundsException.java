package service_exceptions;

import entities.coordinates.Coordinates;

public class CellOutOfBoundsException extends ServiceException{
    public String getExceptionName() {return "Cell Out Of Bounds";}
    public CellOutOfBoundsException(String message) {
        super(message);
    }

    public CellOutOfBoundsException(String message, Coordinates coordinates) {
        super(message, coordinates);
    }

    public CellOutOfBoundsException(String message, String input) {
        super(message, input);
    }

    public CellOutOfBoundsException(String message, Coordinates coordinates, String input) {
        super(message, coordinates, input);
    }
}
