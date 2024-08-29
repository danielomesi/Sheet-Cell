package operations.impl;

import entities.coordinates.Coordinates;
import entities.sheet.CoreSheet;
import entities.types.NumberWrapper;
import entities.types.undefined.UndefinedNumber;
import operations.core.ObjectWrapper;
import operations.core.Operation;

import java.util.List;

public class PLUSOperation extends Operation {
    public PLUSOperation(CoreSheet sheet, Coordinates coordinates, List<Object> arguments) {
        super.sheet = sheet;
        this.coordinates = coordinates;
        super.name = "PLUS";
        super.arguments = arguments;
    }

    @Override
    public ObjectWrapper execute() {
        Object resultObj;
        List<ObjectWrapper> effectiveValues = convertToNonOperationObjects();
        boolean isRefNested = isOneOfTheArgumentsAReference(effectiveValues);
        Class<?>[] expectedClazzes ={NumberWrapper.class, NumberWrapper.class};
        if (areArgumentsTypesValid(expectedClazzes,effectiveValues)) {
            List<Double> doubles = convertToDouble(effectiveValues);

            resultObj = new NumberWrapper(doubles.get(0)+doubles.get(1));
        }
        else {
            resultObj = new UndefinedNumber();
        }

        return new ObjectWrapper(resultObj, isRefNested);
    }
}
