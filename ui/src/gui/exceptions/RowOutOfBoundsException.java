package gui.exceptions;

public class RowOutOfBoundsException extends GUIException{
    public RowOutOfBoundsException(String message, String input) {
        super(message, input);
    }

    public RowOutOfBoundsException(String message) {
        super(message);
    }

    @Override
    public String getExceptionName() {
        return "Row Out Of Bounds";
    }
}
