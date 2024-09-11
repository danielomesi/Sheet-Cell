package operations.impl.logical;

import entities.coordinates.Coordinates;
import entities.sheet.CoreSheet;
import entities.types.undefined.UndefinedBoolean;
import operations.core.Operation;

import java.util.List;

import static operations.core.OperationFactory.areActualArgumentsMatchingToExpectedArguments;
import static operations.core.OperationFactory.convertToNonOperationObjects;

public class LESSOperation extends Operation {
    public LESSOperation(CoreSheet sheet, Coordinates coordinates, List<Object> arguments) {
        super.sheet = sheet;
        this.coordinates = coordinates;
        super.name = "LESS";
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

            resultObj = (Boolean) (firstNumber<=secondNumber);
        }
        else {
            resultObj = new UndefinedBoolean();
        }

        return resultObj;
    }
}
