package operations.impl;

import entities.coordinates.Coordinates;
import entities.sheet.CoreSheet;
import entities.types.NumberWrapper;
import entities.types.undefined.UndefinedNumber;
import operations.core.ObjectBooleanPair;
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
    public Object execute() {
        List<ObjectBooleanPair> effectiveValues = convertToNonOperationObjects();
        Class<?>[] expectedClazzes ={NumberWrapper.class, NumberWrapper.class};
        if (areArgumentsTypesValid(expectedClazzes,effectiveValues)) {
            List<Double> doubles = convertToDouble(effectiveValues);

            return new NumberWrapper(doubles.get(0)+doubles.get(1));
        }
        else {
            return new UndefinedNumber();
        }
    }
}
