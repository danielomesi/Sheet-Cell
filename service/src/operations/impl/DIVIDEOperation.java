package operations.impl;

import entities.coordinates.Coordinates;
import entities.sheet.CoreSheet;
import entities.types.NumberWrapper;
import entities.types.undefined.UndefinedNumber;
import operations.core.ObjectWrapper;
import operations.core.Operation;

import java.util.List;

public class DIVIDEOperation extends Operation {
    public DIVIDEOperation(CoreSheet sheet, Coordinates coordinates, List<Object> arguments) {
        super.sheet = sheet;
        this.coordinates = coordinates;
        super.name = "DIVIDE";
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
            double num1, num2, result;
            num1 = doubles.get(0);
            num2 = doubles.get(1);

            if (num2 == 0)
            {
                return new ObjectWrapper(new UndefinedNumber(),isRefNested);
            }
            else {
                result = num1/num2;
            }

            return new ObjectWrapper(new NumberWrapper(result),isRefNested);
        }
        else {
            return new ObjectWrapper(new UndefinedNumber(),isRefNested);
        }
    }
}
