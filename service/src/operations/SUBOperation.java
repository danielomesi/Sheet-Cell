package operations;

import entities.CellCoordinates;
import entities.core.CoreSheet;
import exceptions.StringIndexOutOfBoundsException;

import java.util.List;

public class SUBOperation extends Operation {
    public SUBOperation(CoreSheet sheet, CellCoordinates coordinates, List<Object> arguments) {
        super.sheet = sheet;
        this.coordinates = coordinates;
        super.name = "SUB";
        super.arguments = arguments;
    }

    @Override
    public String execute() {
        List<Object> nonOperationObjects = convertToNonOperationObjects();
        Class<?>[] expectedClazzes ={String.class, Number.class, Number.class};
        validateArgumentsTypes(expectedClazzes, nonOperationObjects);
        String str = (String) arguments.getFirst();
        int startIndex = ((Number) nonOperationObjects.get(1)).intValue();
        int endIndex =((Number) nonOperationObjects.get(2)).intValue();
        String result;

        try {
           result = str.substring(startIndex, endIndex);
        }
        catch (Exception e) {
            result = "!UNDEFINED";
        }

        return result;
    }

}
