package operations.impl;

import entities.coordinates.Coordinates;
import entities.sheet.CoreSheet;
import entities.types.undefined.UndefinedString;
import operations.core.ObjectBooleanPair;
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
    public Object execute() {
        List<ObjectBooleanPair> effectiveValues = convertToNonOperationObjects();
        Class<?>[] expectedClazzes ={String.class, String.class};
        if (areArgumentsTypesValid(expectedClazzes,effectiveValues)) {
            List<String> strings = convertToList(effectiveValues, String.class);

            return strings.get(0) + strings.get(1);
        }
        else {
            return new UndefinedString();
        }

    }
}
