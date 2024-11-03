package operations.core;

import entities.coordinates.Coordinates;
import entities.sheet.CoreSheet;

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


    public abstract Object execute();


}
