package service.operations;

import service.entities.Cell;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Function {
    protected String name;
    protected List<Object> arguments;
    private static final Map<String, Class<? extends Function>> funcName2Clazz = new HashMap<>();

    public String getName() {return name;}
    public List<Object> getArguments() {return arguments;}

    static {
        funcName2Clazz.put("PLUS", PLUSFunction.class);
    }

    public static Function createFunctionHandler(String name, List<Object> arguments) {
        Class<? extends Function> functionHandler = Function.funcName2Clazz.get(name);
        if (functionHandler != null) {
            try {
                return functionHandler.getDeclaredConstructor(List.class).newInstance(arguments);
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

        if (arg instanceof Cell) {
            result = ((Cell) arg).getEffectiveValue();
        } else if (arg instanceof Function) {
            result = ((Function) arg).execute();
        }
        else {
            result = arg;
        }
        return result;
    }


}
