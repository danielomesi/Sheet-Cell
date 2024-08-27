package exceptions;

import entities.coordinates.Coordinates;

import java.util.Optional;

public abstract class ServiceException extends RuntimeException {
    protected Coordinates coordinates;
    protected String input;

    public ServiceException(String message) {
        super(message);
    }

    // Constructor with a message and CellCoordinates
    public ServiceException(String message, Coordinates coordinates) {
        super(message);
        this.coordinates = coordinates;
    }

    // Constructor with a message and input
    public ServiceException(String message, String input) {
        super(message);
        this.input = input;
    }

    // Constructor with a message, CellCoordinates, and input
    public ServiceException(String message, Coordinates coordinates, String input) {
        super(message);
        this.coordinates = coordinates;
        this.input = input;
    }

    public Coordinates getCellCoordinates() {return coordinates;}
    public String getInput() {return input;}
    public abstract String getExceptionName();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Error name: ").append(getExceptionName()).append("\n");
        sb.append("Error message: ").append(getMessage()).append("\n");

        Optional<Coordinates> optionalCoordinates = Optional.ofNullable(coordinates);
        Optional<String> optionalInput = Optional.ofNullable(input);

        optionalCoordinates.ifPresent(coords -> sb.append("Relevant cell: ").append(coords.getCellID()).append("\n"));
        optionalInput.ifPresent(inp -> sb.append("Relevant input: ").append(inp).append("\n"));

        return sb.toString();
    }
}
