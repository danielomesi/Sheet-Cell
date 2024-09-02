package operations.impl.logical;

import entities.coordinates.Coordinates;
import entities.sheet.CoreSheet;
import entities.types.undefined.UndefinedBoolean;
import entities.types.undefined.UndefinedNumber;
import operations.core.ObjectWrapper;
import operations.core.Operation;

import java.util.List;

public class ANDOperation extends Operation {

    public ANDOperation(CoreSheet sheet, Coordinates coordinates, List<Object> arguments) {
        super.sheet = sheet;
        this.coordinates = coordinates;
        super.name = "AND";
        super.arguments = arguments;
    }

    @Override
    public ObjectWrapper execute() {
        Object resultObj;
        List<ObjectWrapper> effectiveValues = convertToNonOperationObjects();
        boolean isRefNested = isOneOfTheArgumentsAReference(effectiveValues);
        Class<?>[] expectedClazzes ={Boolean.class, Boolean.class};
        if (areArgumentsTypesValid(expectedClazzes,effectiveValues)) {
            Boolean firstArg = (Boolean) effectiveValues.getFirst().getObj();
            Boolean secondArg = (Boolean) effectiveValues.get(1).getObj();
            resultObj = (Boolean) firstArg && secondArg;
        }
        else {
            resultObj = new UndefinedBoolean();
        }

        return new ObjectWrapper(resultObj,isRefNested);
    }
}

