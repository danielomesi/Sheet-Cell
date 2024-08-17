package operations;

import entities.CellCoordinates;
import entities.core.CoreSheet;

import java.util.List;

public class POWOperation extends Operation {
    public POWOperation(CoreSheet sheet, CellCoordinates coordinates, List<Object> arguments) {
        super.sheet = sheet;
        this.coordinates = coordinates;
        super.name = "POW";
        super.arguments = arguments;
    }

    @Override
    public Double execute() {
        List<Object> nonOperationObjects = convertToNonOperationObjects();
        Class<?>[] expectedClazzes ={Number.class, Number.class};
        validateArgumentsTypes(expectedClazzes, nonOperationObjects);
        List<Double> doubles = convertToDouble(nonOperationObjects);

        return Math.pow(doubles.get(0),doubles.get(1));
    }
}
