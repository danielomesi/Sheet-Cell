package operations;

import entities.coordinates.CellCoordinates;
import entities.sheet.CoreSheet;

import java.util.List;

public class ABSOperation extends Operation {

    public ABSOperation(CoreSheet sheet, CellCoordinates coordinates, List<Object> arguments) {
        super.sheet = sheet;
        this.coordinates = coordinates;
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
