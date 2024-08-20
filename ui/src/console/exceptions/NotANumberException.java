package console.exceptions;

public class NotANumberException extends ConsoleException{
    public NotANumberException(String message) {
        super(message);
    }

    public NotANumberException(String message, String input) {
        super(message, input);
    }

    @Override
    public String getExceptionName() {
        return "Not A Number";
    }
}
