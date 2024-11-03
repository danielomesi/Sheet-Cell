package service_exceptions;

import entities.coordinates.Coordinates;

public class InvalidPathDetectedException extends ServiceException{
    public InvalidPathDetectedException(String message) {
        super(message);
    }

    public InvalidPathDetectedException(String message, Coordinates coordinates) {
        super(message, coordinates);
    }

    public InvalidPathDetectedException(String message, String input) {
        super(message, input);
    }

    public InvalidPathDetectedException(String message, Coordinates coordinates, String input) {
        super(message, coordinates, input);
    }

    @Override
    public String getExceptionName() {
        return "Invalid Path Detected";
    }
}
