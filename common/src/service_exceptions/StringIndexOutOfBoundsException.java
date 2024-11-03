package service_exceptions;

import entities.coordinates.Coordinates;

public class StringIndexOutOfBoundsException extends ServiceException{
    public String getExceptionName() {return "String Index Out Of Bounds";}

    public StringIndexOutOfBoundsException(String message) {
        super(message);
    }

    public StringIndexOutOfBoundsException(String message, Coordinates coordinates) {
        super(message, coordinates);
    }

    public StringIndexOutOfBoundsException(String message, String input) {
        super(message, input);
    }

    public StringIndexOutOfBoundsException(String message, Coordinates coordinates, String input) {
        super(message, coordinates, input);
    }
}
