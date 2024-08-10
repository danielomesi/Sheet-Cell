package service.operations;

import service.entities.Sheet;

import java.util.List;

public class MINUSOperation extends Operation {
    public MINUSOperation(Sheet sheet, List<Object> arguments) {
        super.sheet = sheet;
        super.name = "MINUS";
        super.arguments = arguments;
    }

    @Override
    public Double execute() {
        double result;
        Object arg1 = getArgValue(arguments.getFirst());
        Object arg2 = getArgValue(arguments.get(1));

        if ((arg1 instanceof Number) && (arg2 instanceof Number)) {
                result = ((double)arg1) - ((double)arg2);
        }
        else {
            throw new IllegalArgumentException();
        }

        return result;
    }
}
