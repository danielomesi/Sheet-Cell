package operations;

import entities.CellCoordinates;
import entities.Sheet;
import utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Operation {
    protected Sheet sheet;
    protected String name;
    protected List<Object> arguments;
    private static final Map<String, Class<? extends Operation>> funcName2Clazz = new HashMap<>();

    public String getName() {return name;}
    public List<Object> getArguments() {return arguments;}
    public void setSheet(Sheet sheet) {
        this.sheet = sheet;
        for (Object argument : arguments) {
            if (argument instanceof Operation) {
                Operation operation = (Operation) argument;
                operation.setSheet(sheet);
            }
        }
    }

    static {
        funcName2Clazz.put("PLUS", PLUSOperation.class);
        funcName2Clazz.put("MINUS", MINUSOperation.class);
        funcName2Clazz.put("DIVIDE", DIVIDEOperation.class);
        funcName2Clazz.put("TIMES", TIMESOperation.class);
    }

    public static Operation createFunctionHandler(Sheet sheet, String name, List<Object> arguments) {
        Class<? extends Operation> functionHandler = Operation.funcName2Clazz.get(name);
        if (functionHandler != null) {
            try {
                return functionHandler.getDeclaredConstructor(Sheet.class, List.class).newInstance(sheet, arguments);
            } catch (Exception e) {
                throw new RuntimeException("Error creating instance of " + name, e);
            }
        } else {
            throw new IllegalArgumentException("No handler found for " + name);
        }
    }

    public abstract Object execute();

    public Object getArgValue(Object arg) {
        Object result;

        if (arg instanceof CellCoordinates) {
            CellCoordinates cellCoordinates = (CellCoordinates) arg;
            result = Utils.getCellObjectFromIndices(sheet, cellCoordinates.getRow(), cellCoordinates.getCol()).getEffectiveValue();
        } else if (arg instanceof Operation) {
            result = ((Operation) arg).execute();
        }
        else {
            result = arg;
        }
        return result;
    }


}
