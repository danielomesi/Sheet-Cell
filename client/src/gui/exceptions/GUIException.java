package gui.exceptions;

import java.util.Optional;

public abstract class GUIException extends RuntimeException {
    protected String input;

    public GUIException(String message) {
        super(message);
    }

    public GUIException(String message, String input) {
        super(message);
        this.input = input;
    }

    public abstract String getExceptionName();

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Error name: ").append(getExceptionName()).append("\n");
        sb.append("Error message: ").append(getMessage()).append("\n");

        Optional<String> optionalInput = Optional.ofNullable(input);

        optionalInput.ifPresent(inp -> sb.append("Relevant Data: ").append(inp).append("\n"));

        return sb.toString();
    }
}
