package operations.impl.logical;

import entities.coordinates.Coordinates;
import entities.sheet.CoreSheet;
import entities.types.undefined.UndefinedBoolean;
import entities.types.undefined.UndefinedNumber;
import operations.core.ObjectWrapper;
import operations.core.Operation;

import java.util.List;

public class EQUALOperation extends Operation {

    public EQUALOperation(CoreSheet sheet, Coordinates coordinates, List<Object> arguments) {
        super.sheet = sheet;
        this.coordinates = coordinates;
        super.name = "EQUAL";
        super.arguments = arguments;
    }

    @Override
    public ObjectWrapper execute() {
        Object resultObj;
        List<ObjectWrapper> effectiveValues = convertToNonOperationObjects();
        boolean isRefNested = isOneOfTheArgumentsAReference(effectiveValues);
        Class<?>[] expectedClazzes ={Object.class,Object.class};
        if (areArgumentsTypesValid(expectedClazzes,effectiveValues)) {
            Object firstArg = effectiveValues.getFirst().getObj();
            Object secondArg = effectiveValues.get(1).getObj();
            resultObj = (Boolean) firstArg.equals(secondArg);
        }
        else {
            resultObj = new UndefinedBoolean();
        }

        return new ObjectWrapper(resultObj,isRefNested);
    }
}

