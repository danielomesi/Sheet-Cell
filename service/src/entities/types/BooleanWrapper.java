package entities.types;

import java.io.Serializable;

public class BooleanWrapper implements Serializable {
    private boolean value;

    public BooleanWrapper(boolean value){
        this.value = value;
    }

    public boolean getValue(){return value;}
    public void setValue(boolean value){this.value = value;}

    @Override
    public String toString(){
        return value ? "TRUE" : "FALSE";
    }
}
