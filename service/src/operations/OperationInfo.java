package operations;

public class OperationInfo {
    private final Class<? extends Operation> operationClass;
    private final int numOfArgsRequired;

    public OperationInfo(Class<? extends Operation> operationClass, int numOfArgsRequired) {
        this.operationClass = operationClass;
        this.numOfArgsRequired = numOfArgsRequired;
    }

    public Class<? extends Operation> getOperationClass() {
        return operationClass;
    }

    public int getNumOfArgsRequired() {
        return numOfArgsRequired;
    }

    @Override
    public String toString() {
        return "OperationInfo{" +
                "operationClass=" + operationClass +
                ", numOfArgsRequired=" + numOfArgsRequired +
                '}';
    }
}

