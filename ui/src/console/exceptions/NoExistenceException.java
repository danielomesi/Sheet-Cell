package console.exceptions;

public class NoExistenceException extends ConsoleException{
    public NoExistenceException(String message) {
        super(message);
    }

    public NoExistenceException(String message, String input) {
        super(message, input);
    }

    @Override
    public String getExceptionName() {
        return "No Existence";
    }
}
