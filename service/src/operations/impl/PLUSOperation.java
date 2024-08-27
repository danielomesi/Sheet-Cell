package operations.impl;

import entities.coordinates.Coordinates;
import entities.sheet.CoreSheet;
import operations.core.Operation;

import java.util.List;

public class PLUSOperation extends Operation {
    public PLUSOperation(CoreSheet sheet, Coordinates coordinates, List<Object> arguments) {
        super.sheet = sheet;
        this.coordinates = coordinates;
        super.name = "PLUS";
        super.arguments = arguments;
    }

    @Override
    public Double execute() {
        List<Object> nonOperationObjects = convertToNonOperationObjects();
        Class<?>[] expectedClazzes ={Number.class, Number.class};
        validateArgumentsTypes(expectedClazzes, nonOperationObjects);
        List<Double> doubles = convertToDouble(nonOperationObjects);

        return doubles.get(0)+doubles.get(1);
    }
}
