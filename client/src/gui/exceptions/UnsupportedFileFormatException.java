package gui.exceptions;

public class UnsupportedFileFormatException extends GUIException{
    public UnsupportedFileFormatException(String message) {
        super(message);
    }

    public UnsupportedFileFormatException(String message, String input) {
        super(message, input);
    }

    @Override
    public String getExceptionName() {
        return "Unsupported File Format";
    }
}
