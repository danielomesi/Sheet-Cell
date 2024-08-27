package exceptions;

import entities.coordinates.Coordinates;

public class InvalidXMLException extends ServiceException{
    public InvalidXMLException(String message) {
        super(message);
    }

    public InvalidXMLException(String message, Coordinates coordinates) {
        super(message, coordinates);
    }

    public InvalidXMLException(String message, String input) {
        super(message, input);
    }

    public InvalidXMLException(String message, Coordinates coordinates, String input) {
        super(message, coordinates, input);
    }

    @Override
    public String getExceptionName() {
        return "Invalid XML";
    }
}
