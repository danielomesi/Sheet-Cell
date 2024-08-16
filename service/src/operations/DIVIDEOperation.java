package operations;

import entities.core.CoreSheet;

import java.util.List;

public class DIVIDEOperation extends Operation {
    public DIVIDEOperation(CoreSheet sheet, List<Object> arguments) {
        super.sheet = sheet;
        super.name = "DIVIDE";
        super.arguments = arguments;
    }

    @Override
    public Double execute() {
        //need to implement nan somehow
        double result = 0;
        List<Object> nonOperationObjects = convertToNonOperationObjects();
        Class<?>[] expectedClazzes ={Number.class, Number.class};
        validateArgumentsTypes(expectedClazzes, nonOperationObjects);
        List<Double> doubles = convertToDouble(nonOperationObjects);

        return doubles.get(0)/doubles.get(1);
    }
}
