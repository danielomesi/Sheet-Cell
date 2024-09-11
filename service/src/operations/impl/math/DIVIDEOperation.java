package operations.impl.math;

import entities.coordinates.Coordinates;
import entities.sheet.CoreSheet;
import entities.types.undefined.UndefinedNumber;
import operations.core.Operation;

import java.util.List;

import static operations.core.OperationFactory.areActualArgumentsMatchingToExpectedArguments;
import static operations.core.OperationFactory.convertToNonOperationObjects;

public class DIVIDEOperation extends Operation {
    public DIVIDEOperation(CoreSheet sheet, Coordinates coordinates, List<Object> arguments) {
        super.sheet = sheet;
        this.coordinates = coordinates;
        super.name = "DIVIDE";
        super.arguments = arguments;
    }


    @Override
    public Object execute() {
        Object resultObj;
        List<Object> effectiveValues = convertToNonOperationObjects(arguments);
        Class<?>[] expectedClazzes ={Number.class, Number.class};
        if (areActualArgumentsMatchingToExpectedArguments(expectedClazzes,effectiveValues)) {
            Double firstNumber = (Double) effectiveValues.get(0);
            Double secondNumber = (Double) effectiveValues.get(1);

            if (secondNumber == 0)
            {
                resultObj = new UndefinedNumber();
            }
            else {
                resultObj = firstNumber/secondNumber;
            }
        }
        else {
            resultObj = new UndefinedNumber();
        }
        return resultObj;
    }
}
