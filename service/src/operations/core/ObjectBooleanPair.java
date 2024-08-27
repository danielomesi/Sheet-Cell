package operations.core;

import java.io.Serializable;

public class ObjectBooleanPair implements Serializable {
    private final Object obj;
    private final boolean isCreatedByRefOperation;
    public ObjectBooleanPair(Object obj, Boolean isCreatedByRefOperation) {
        this.obj = obj;
        this.isCreatedByRefOperation = isCreatedByRefOperation;
    }
    public Object getObj() {return obj;}
    public boolean getIsRefOperation() {return isCreatedByRefOperation;}
}
