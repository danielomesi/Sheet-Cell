package operations.core;

import entities.coordinates.CellCoordinates;
import entities.sheet.CoreSheet;
import exceptions.InvalidArgumentException;

import java.io.Serializable;
import java.util.*;

public abstract class Operation implements Serializable {
    protected CoreSheet sheet;
    protected CellCoordinates coordinates;
    protected String name;
    protected List<Object> arguments;


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
