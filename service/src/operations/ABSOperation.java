package operations;

import entities.core.CoreSheet;

import java.util.List;

public class ABSOperation extends Operation {
    public ABSOperation(CoreSheet sheet, List<Object> arguments) {
        super.sheet = sheet;
        super.name = "ABS";
        super.arguments = arguments;
    }

    @Override
    public Double execute() {
        List<Object> nonOperationObjects = convertToNonOperationObjects();
        Class<?>[] expectedClazzes ={Number.class};
        validateArgumentsTypes(expectedClazzes, nonOperationObjects);
        List<Double> doubles = convertToDouble(nonOperationObjects);

        return Math.abs(doubles.getFirst());
    }
}
