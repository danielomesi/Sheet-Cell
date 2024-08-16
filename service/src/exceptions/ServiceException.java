package exceptions;

import entities.CellCoordinates;

public abstract class ServiceException extends RuntimeException {
    protected CellCoordinates cellCoordinates;
    protected String input;
    protected static String exceptionName;

    public ServiceException(String message) {
        super(message);
    }

    // Constructor with a message and CellCoordinates
    public ServiceException(String message, CellCoordinates cellCoordinates) {
        super(message);
        this.cellCoordinates = cellCoordinates;
    }

    // Constructor with a message and input
    public ServiceException(String message, String input) {
        super(message);
        this.input = input;
    }

    // Constructor with a message, CellCoordinates, and input
    public ServiceException(String message, CellCoordinates cellCoordinates, String input) {
        super(message);
        this.cellCoordinates = cellCoordinates;
        this.input = input;
    }

    public CellCoordinates getCellCoordinates() {return cellCoordinates;}
    public String getInput() {return input;}
    public String getExceptionName() {return exceptionName;}
}
