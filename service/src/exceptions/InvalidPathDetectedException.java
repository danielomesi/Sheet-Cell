package exceptions;

import entities.coordinates.CellCoordinates;

public class InvalidPathDetectedException extends ServiceException{
    public InvalidPathDetectedException(String message) {
        super(message);
    }

    public InvalidPathDetectedException(String message, CellCoordinates cellCoordinates) {
        super(message, cellCoordinates);
    }

    public InvalidPathDetectedException(String message, String input) {
        super(message, input);
    }

    public InvalidPathDetectedException(String message, CellCoordinates cellCoordinates, String input) {
        super(message, cellCoordinates, input);
    }

    @Override
    public String getExceptionName() {
        return "Invalid Path Detected";
    }
}
