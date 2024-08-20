package exceptions;

import entities.cell.CellCoordinates;

import java.util.Optional;

public abstract class ServiceException extends RuntimeException {
    protected CellCoordinates cellCoordinates;
    protected String input;

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
    public abstract String getExceptionName();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Error name: ").append(getExceptionName()).append("\n");
        sb.append("Error message: ").append(getMessage()).append("\n");

        Optional<CellCoordinates> optionalCoordinates = Optional.ofNullable(cellCoordinates);
        Optional<String> optionalInput = Optional.ofNullable(input);

        optionalCoordinates.ifPresent(coords -> sb.append("Relevant cell: ").append(coords.getCellID()).append("\n"));
        optionalInput.ifPresent(inp -> sb.append("Relevant input: ").append(inp).append("\n"));

        return sb.toString();
    }
}
