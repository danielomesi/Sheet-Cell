package operations.impl;

import entities.coordinates.Coordinates;
import entities.sheet.CoreSheet;
import entities.types.undefined.UndefinedNumber;
import operations.core.ObjectWrapper;
import operations.core.Operation;

import java.util.List;

public class ABSOperation extends Operation {

    public ABSOperation(CoreSheet sheet, Coordinates coordinates, List<Object> arguments) {
        super.sheet = sheet;
        this.coordinates = coordinates;
        super.name = "ABS";
        super.arguments = arguments;
    }

    @Override
    public ObjectWrapper execute() {
        Object resultObj;
        List<ObjectWrapper> effectiveValues = convertToNonOperationObjects();
        boolean isRefNested = isOneOfTheArgumentsAReference(effectiveValues);
        Class<?>[] expectedClazzes ={Number.class};
        if (areArgumentsTypesValid(expectedClazzes,effectiveValues)) {
            Number number = (Number)(effectiveValues.getFirst().getObj());

            resultObj = Math.abs(number.doubleValue());
        }
        else {
            resultObj = new UndefinedNumber();
        }

        return new ObjectWrapper(resultObj,isRefNested);
    }
}
