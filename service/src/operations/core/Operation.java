package operations.core;

import entities.coordinates.Coordinates;
import entities.sheet.CoreSheet;
import exceptions.InvalidArgumentException;

import java.io.Serializable;
import java.util.*;

public abstract class Operation implements Serializable {
    protected CoreSheet sheet;
    protected Coordinates coordinates;
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


    public abstract ObjectWrapper execute();

    protected ObjectWrapper getArgValue(Object arg) {
        ObjectWrapper result;
        boolean isRefOperation = false;

        if (arg instanceof Operation operation) {
            result = operation.execute();

        }
        else {
            result = new ObjectWrapper(arg,false);
        }
        return result;
    }


    protected List<ObjectWrapper> convertToNonOperationObjects()
    {
        List<ObjectWrapper> result = new ArrayList<>();
        for (Object argument : arguments) {
            result.add(getArgValue(argument));
        }
        return result;
    }
    protected List<Double> convertToDouble(List<ObjectWrapper> list) {
        List<Double> doubles = new ArrayList<>();
        for (ObjectWrapper pair : list) {
            Object obj = pair.getObj();
            if (obj instanceof Number number) {
                doubles.add(number.doubleValue());
            } else {
                throw new InvalidArgumentException("One of the arguments in the function " + name + " is not a number", coordinates);
            }
        }

        return doubles;
    }

    protected <T> List<T> convertToList(List<ObjectWrapper> list, Class<T> classType) {
        List<T> result = new ArrayList<>();
        for (ObjectWrapper pair : list) {
            Object obj = pair.getObj();
            if (classType.isInstance(obj)) {
                result.add(classType.cast(obj));
            }
            else {
                throw new InvalidArgumentException("One of the arguments in the function " + name + " is not matching the required type",coordinates);
            }
        }

        return result;
    }

    protected boolean areArgumentsTypesValid(Class<?>[] clazzes, List<ObjectWrapper> list) {
        boolean result = true;
        for (int i = 0; i < clazzes.length; i++) {
            Class<?> clazz = clazzes[i];
            Object obj = list.get(i).getObj();
            if (!list.get(i).getIsRefOperation()) {
                if (!clazz.isInstance(obj)) {
                    throw new InvalidArgumentException("Argument #" +(i+1) + " in the function " + name +
                            " is not a " + clazz.getSimpleName(),coordinates, String.valueOf(obj));
                }
            }
            else {
                if (!clazz.isInstance(obj)) {
                    result = false;
                }
            }
        }

        return result;
    }

    protected boolean isOneOfTheArgumentsAReference(List<ObjectWrapper> list) {
        for (ObjectWrapper pair : list) {
            boolean isRefOperation = pair.getIsRefOperation();
            if (isRefOperation) {
                return true;
            }
        }
        return false;
    }
}
