package operations;

import entities.core.CoreSheet;

import java.util.List;

public class MODOperation extends Operation {
    public MODOperation(CoreSheet sheet, List<Object> arguments) {
        super.sheet = sheet;
        super.name = "MOD";
        super.arguments = arguments;
    }

    @Override
    public Double execute() {
        List<Object> nonOperationObjects = convertToNonOperationObjects();
        Class<?>[] expectedClazzes ={Number.class, Number.class};
        validateArgumentsTypes(expectedClazzes, nonOperationObjects);
        List<Double> doubles = convertToDouble(nonOperationObjects);

        return doubles.get(0)%doubles.get(1);
    }
}
