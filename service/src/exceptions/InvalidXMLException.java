package exceptions;

import entities.coordinates.CellCoordinates;

public class InvalidXMLException extends ServiceException{
    public InvalidXMLException(String message) {
        super(message);
    }

    public InvalidXMLException(String message, CellCoordinates cellCoordinates) {
        super(message, cellCoordinates);
    }

    public InvalidXMLException(String message, String input) {
        super(message, input);
    }

    public InvalidXMLException(String message, CellCoordinates cellCoordinates, String input) {
        super(message, cellCoordinates, input);
    }

    @Override
    public String getExceptionName() {
        return "Invalid XML";
    }
}
