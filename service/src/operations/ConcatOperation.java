package operations;

import entities.cell.CellCoordinates;
import entities.sheet.CoreSheet;

import java.util.List;

public class ConcatOperation extends Operation {
    public ConcatOperation(CoreSheet sheet, CellCoordinates coordinates, List<Object> arguments) {
        super.sheet = sheet;
        this.coordinates = coordinates;
        super.name = "CONCAT";
        super.arguments = arguments;
    }

    @Override
    public String execute() {
        List<Object> nonOperationObjects = convertToNonOperationObjects();
        Class<?>[] expectedClazzes ={String.class, String.class};
        validateArgumentsTypes(expectedClazzes, nonOperationObjects);
        List<String> strings = convertToList(nonOperationObjects, String.class);

        return strings.get(0) + strings.get(1);
    }
}
