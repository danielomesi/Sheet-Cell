package operations;

import entities.CellCoordinates;
import entities.core.CoreSheet;
import utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Operation {
    protected CoreSheet sheet;
    protected String name;
    protected List<Object> arguments;
    protected static final Map<String, OperationInfo> funcName2OperationInfo = new HashMap<>();

    public String getName() {return name;}
    public List<Object> getArguments() {return arguments;}
    public void setSheet(CoreSheet sheet) {
        this.sheet = sheet;
        for (Object argument : arguments) {
            if (argument instanceof Operation operation) {
                operation.setSheet(sheet);
            }
        }
    }

    static {
        funcName2OperationInfo.put("PLUS", new OperationInfo(PLUSOperation.class, 2));
        funcName2OperationInfo.put("MINUS", new OperationInfo(MINUSOperation.class, 2));
        funcName2OperationInfo.put("TIMES", new OperationInfo(TIMESOperation.class, 2));
        funcName2OperationInfo.put("DIVIDE", new OperationInfo(DIVIDEOperation.class, 2));
        funcName2OperationInfo.put("REF", new OperationInfo(REFOperation.class, 1));
    }

    public static Operation createFunctionHandler(CoreSheet sheet, String name, List<Object> arguments) {
        Class<? extends Operation> functionHandler = Operation.funcName2OperationInfo.get(name).getOperationClass();
        if (functionHandler != null) {
            try {
                return functionHandler.getDeclaredConstructor(CoreSheet.class, List.class).newInstance(sheet, arguments);
            } catch (Exception e) {
                throw new RuntimeException("Error creating instance of " + name, e);
            }
        } else {
            throw new IllegalArgumentException("No handler found for " + name);
        }
    }

    public abstract Object execute();

    protected Object getArgValue(Object arg) {
        Object result;

        if (arg instanceof Operation operation) {
            result = operation.execute();
        }
        else {
            result = arg;
        }
        return result;
    }
    public static Map<String, OperationInfo> getOperationsMap() {return funcName2OperationInfo; }

    protected List<Object> convertToNonOperationObjects()
    {
        List<Object> result = new ArrayList<>();
        for (Object argument : arguments) {
            result.add(getArgValue(argument));
        }
        return result;
    }
    protected List<Double> convertToDouble(List<Object> objects) {
        List<Double> doubles = new ArrayList<>();
        for (Object object : objects) {
            if (object instanceof Number) {
                doubles.add(((Number) object).doubleValue());
            } else {
                throw new IllegalArgumentException("One of the arguments in the function " + name + " is not a number");
            }
        }

        return doubles;
    }
}
