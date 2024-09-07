package gui.exceptions;

public class UnsupportedFileFormat extends GUIException{
    public UnsupportedFileFormat(String message) {
        super(message);
    }

    public UnsupportedFileFormat(String message, String input) {
        super(message, input);
    }

    @Override
    public String getExceptionName() {
        return "Unsupported File Format";
    }
}
