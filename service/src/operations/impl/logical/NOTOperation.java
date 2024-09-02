package operations.impl.logical;

import entities.coordinates.Coordinates;
import entities.sheet.CoreSheet;
import entities.types.undefined.UndefinedBoolean;
import entities.types.undefined.UndefinedNumber;
import operations.core.ObjectWrapper;
import operations.core.Operation;

import java.util.List;

public class NOTOperation extends Operation {

    public NOTOperation(CoreSheet sheet, Coordinates coordinates, List<Object> arguments) {
        super.sheet = sheet;
        this.coordinates = coordinates;
        super.name = "NOT";
        super.arguments = arguments;
    }

    @Override
    public ObjectWrapper execute() {
        Object resultObj;
        List<ObjectWrapper> effectiveValues = convertToNonOperationObjects();
        boolean isRefNested = isOneOfTheArgumentsAReference(effectiveValues);
        Class<?>[] expectedClazzes ={Boolean.class};
        if (areArgumentsTypesValid(expectedClazzes,effectiveValues)) {
            Boolean arg = (Boolean) effectiveValues.getFirst().getObj();
            resultObj = (Boolean) !arg;
        }
        else {
            resultObj = new UndefinedBoolean();
        }

        return new ObjectWrapper(resultObj,isRefNested);
    }
}

