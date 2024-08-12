package operations;

import entities.Sheet;

import java.util.List;

public class PLUSOperation extends Operation {

    public PLUSOperation(Sheet sheet, List<Object> arguments) {
        super.sheet = sheet;
        super.name = "PLUS";
        super.arguments = arguments;
    }

    @Override
    public Double execute() {
        double result = 0;

        for (int i = 0; i < arguments.size(); i++) {
            Object value = getArgValue(arguments.get(i));
            if (value instanceof Number) {
                result += ((Number) value).doubleValue();
            }
            else {
                throw new IllegalArgumentException("One of the arguments is not a number");
            }
        }

        return result;
    }
}
