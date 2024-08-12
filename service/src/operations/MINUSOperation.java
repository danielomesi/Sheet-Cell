package operations;

import entities.core.CoreSheet;

import java.util.List;

public class MINUSOperation extends Operation {
    public MINUSOperation(CoreSheet sheet, List<Object> arguments) {
        super.sheet = sheet;
        super.name = "MINUS";
        super.arguments = arguments;
    }

    @Override
    public Double execute() {
        List<Object> nonOperationObjects = convertToNonOperationObjects();
        List<Double> doubles = convertToDouble(nonOperationObjects);

        return doubles.get(0)-doubles.get(1);
    }
}
