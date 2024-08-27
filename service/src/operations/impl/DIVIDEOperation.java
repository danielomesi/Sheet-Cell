package operations.impl;

import entities.coordinates.Coordinates;
import entities.sheet.CoreSheet;
import entities.types.NumberWrapper;
import entities.types.undefined.UndefinedNumber;
import operations.core.ObjectBooleanPair;
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
    public Object execute() {
        List<ObjectBooleanPair> effectiveValues = convertToNonOperationObjects();
        Class<?>[] expectedClazzes ={NumberWrapper.class, NumberWrapper.class};
        if (areArgumentsTypesValid(expectedClazzes,effectiveValues)) {
            List<Double> doubles = convertToDouble(effectiveValues);
            double num1, num2, result;
            num1 = doubles.get(0);
            num2 = doubles.get(1);

            if (num2 == 0)
            {
                return new UndefinedNumber();
            }
            else {
                result = num1/num2;
            }

            return new NumberWrapper(result);
        }
        else {
            return new UndefinedNumber();
        }
    }
}
