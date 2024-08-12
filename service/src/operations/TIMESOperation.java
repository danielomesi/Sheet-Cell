package operations;

import entities.Sheet;

import java.util.List;

public class TIMESOperation extends Operation {
    public TIMESOperation(Sheet sheet, List<Object> arguments) {
        super.sheet = sheet;
        super.name = "TIMES";
        super.arguments = arguments;
    }

    @Override
    public Double execute() {
        double result = 1;

        for (int i = 0; i < arguments.size(); i++) {
            Object value = getArgValue(arguments.get(i));
            if (value instanceof Number) {
                result *= ((Number) value).doubleValue();
            }
            else {
                throw new IllegalArgumentException("One of the arguments is not a number");
            }
        }

        return result;
    }
}
