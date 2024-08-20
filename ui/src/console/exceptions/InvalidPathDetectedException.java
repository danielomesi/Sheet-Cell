package console.exceptions;

public class InvalidPathDetectedException extends ConsoleException{

    public InvalidPathDetectedException(String message) {
        super(message);
    }

    public InvalidPathDetectedException(String message, String input) {
        super(message, input);
    }

    @Override
    public String getExceptionName() {
        return "Invalid Path";
    }
}
