package operations;

import entities.CellCoordinates;
import entities.core.CoreCell;
import entities.core.CoreSheet;
import exceptions.InvalidArgumentException;

import java.util.*;

public abstract class Operation {
    protected CoreSheet sheet;
    protected CellCoordinates coordinates;
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

    //try to make this code self-maintainable
    static {
        funcName2OperationInfo.put("PLUS", new OperationInfo(PLUSOperation.class, 2));
        funcName2OperationInfo.put("MINUS", new OperationInfo(MINUSOperation.class, 2));
        funcName2OperationInfo.put("TIMES", new OperationInfo(TIMESOperation.class, 2));
        funcName2OperationInfo.put("DIVIDE", new OperationInfo(DIVIDEOperation.class, 2));
        funcName2OperationInfo.put("MOD", new OperationInfo(MODOperation.class, 2));
        funcName2OperationInfo.put("POW", new OperationInfo(POWOperation.class, 2));
        funcName2OperationInfo.put("ABS", new OperationInfo(ABSOperation.class, 1));
        funcName2OperationInfo.put("CONCAT", new OperationInfo(ConcatOperation.class, 2));
        funcName2OperationInfo.put("SUB", new OperationInfo(SUBOperation.class, 3));
        funcName2OperationInfo.put("REF", new OperationInfo(REFOperation.class, 1));


    }

    public static Operation createFunctionHandler(CoreSheet sheet,CellCoordinates coordinates, String name, List<Object> arguments) {
        Class<? extends Operation> functionHandler = Operation.funcName2OperationInfo.get(name).getOperationClass();
        if (functionHandler != null) {
            try {
                return functionHandler.getDeclaredConstructor(CoreSheet.class,CellCoordinates.class,List.class).newInstance(sheet,coordinates,arguments);
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
                throw new InvalidArgumentException("One of the arguments in the function " + name + " is not a number", coordinates);
            }
        }

        return doubles;
    }

    protected <T> List<T> convertToList(List<Object> objects, Class<T> classType) {
        List<T> result = new ArrayList<>();
        for (Object object : objects) {
            if (classType.isInstance(object)) {
                result.add(classType.cast(object));
            }
            else {
                throw new InvalidArgumentException("One of the arguments in the function " + name + " is not matching the required type",coordinates);
            }
        }

        return result;
    }

    protected void validateArgumentsTypes(Class<?>[] clazzes, List<Object> nonOperationArguments) {
        for (int i = 0; i < clazzes.length; i++) {
            Class<?> clazz = clazzes[i];
            Object obj = nonOperationArguments.get(i);
            if (!clazz.isInstance(obj)) {
                throw new InvalidArgumentException("Argument #" +(i+1) + " in the function " + name +
                        " is not a " + clazz.getSimpleName(),coordinates, String.valueOf(obj));
            }
        }
    }
}
