package operations;

import entities.core.CoreSheet;

import java.util.List;

public class TIMESOperation extends Operation {
    public TIMESOperation(CoreSheet sheet, List<Object> arguments) {
        super.sheet = sheet;
        super.name = "TIMES";
        super.arguments = arguments;
    }

    @Override
    public Double execute() {
        List<Object> nonOperationObjects = convertToNonOperationObjects();
        Class<?>[] expectedClazzes ={Number.class, Number.class};
        validateArgumentsTypes(expectedClazzes, nonOperationObjects);
        List<Double> doubles = convertToDouble(nonOperationObjects);

        return doubles.get(0)*doubles.get(1);
    }
}
