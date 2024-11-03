package service_exceptions;

import entities.coordinates.Coordinates;

public class ZeroDivisionException extends ServiceException{
    public String getExceptionName() {return "Zero Division";}

    public ZeroDivisionException(String message) {
        super(message);
    }

    public ZeroDivisionException(String message, Coordinates coordinates) {
        super(message, coordinates);
    }

    public ZeroDivisionException(String message, String input) {
        super(message, input);
    }

    public ZeroDivisionException(String message, Coordinates coordinates, String input) {
        super(message, coordinates, input);
    }
}
