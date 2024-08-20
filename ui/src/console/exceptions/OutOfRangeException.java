package console.exceptions;

public class OutOfRangeException extends ConsoleException{
    public OutOfRangeException(String message) {
        super(message);
    }

    public OutOfRangeException(String message, String input) {
        super(message, input);
    }

    @Override
    public String getExceptionName() {
        return "Out Of Range";
    }
}
