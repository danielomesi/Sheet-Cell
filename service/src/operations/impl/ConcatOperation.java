package operations.impl;

import entities.coordinates.Coordinates;
import entities.sheet.CoreSheet;
import entities.types.undefined.UndefinedString;
import operations.core.ObjectWrapper;
import operations.core.Operation;

import java.util.List;

public class ConcatOperation extends Operation {
    public ConcatOperation(CoreSheet sheet, Coordinates coordinates, List<Object> arguments) {
        super.sheet = sheet;
        this.coordinates = coordinates;
        super.name = "CONCAT";
        super.arguments = arguments;
    }

    @Override
    public ObjectWrapper execute() {
        Object resultObj;
        List<ObjectWrapper> effectiveValues = convertToNonOperationObjects();
        boolean isRefNested = isOneOfTheArgumentsAReference(effectiveValues);
        Class<?>[] expectedClazzes ={String.class, String.class};
        if (areArgumentsTypesValid(expectedClazzes,effectiveValues)) {
            List<String> strings = convertToList(effectiveValues, String.class);

            resultObj = strings.get(0) + strings.get(1);
        }
        else {
            resultObj = new UndefinedString();
        }

        return new ObjectWrapper(resultObj, isRefNested);
    }
}
