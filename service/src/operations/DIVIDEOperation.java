package operations;

import entities.Sheet;

import java.util.List;

public class DIVIDEOperation extends Operation {
    public DIVIDEOperation(Sheet sheet, List<Object> arguments) {
        super.sheet = sheet;
        super.name = "DIVIDE";
        super.arguments = arguments;
    }

    @Override
    public Double execute() {
        double result = 0;
        Object arg1 = getArgValue(arguments.getFirst());
        Object arg2 = getArgValue(arguments.get(1));

        if ((arg1 instanceof Number) && (arg2 instanceof Number)) {
            if ( ((double)arg2) == 0) {
             throw new ArithmeticException();
            }
            else {
                result = ((double)arg1) / ((double)arg2);
            }
        }
        else {
            throw new IllegalArgumentException();
        }

        return result;
    }
}
