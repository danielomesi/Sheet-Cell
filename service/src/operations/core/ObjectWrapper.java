package operations.core;

import java.io.Serializable;

public class ObjectWrapper implements Serializable {
    private final Object obj;
    private final boolean isCreatedByRefOperation;
    public ObjectWrapper(Object obj, Boolean isCreatedByRefOperation) {
        this.obj = obj;
        this.isCreatedByRefOperation = isCreatedByRefOperation;
    }
    public Object getObj() {return obj;}
    public boolean getIsRefOperation() {return isCreatedByRefOperation;}
}
