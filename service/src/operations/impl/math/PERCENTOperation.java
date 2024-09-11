package operations.impl.math;

import entities.coordinates.Coordinates;
import entities.sheet.CoreSheet;
import entities.types.undefined.UndefinedNumber;
import operations.core.Operation;

import java.util.List;

import static operations.core.OperationFactory.areActualArgumentsMatchingToExpectedArguments;
import static operations.core.OperationFactory.convertToNonOperationObjects;

public class PERCENTOperation extends Operation {
    public PERCENTOperation(CoreSheet sheet, Coordinates coordinates, List<Object> arguments) {
        super.sheet = sheet;
        this.coordinates = coordinates;
        super.name = "PERCENT";
        super.arguments = arguments;
    }

    @Override
    public Object execute() {
        Object resultObj;
        List<Object> effectiveValues = convertToNonOperationObjects(arguments);
        Class<?>[] expectedClazzes ={Number.class, Number.class};
        if (areActualArgumentsMatchingToExpectedArguments(expectedClazzes,effectiveValues)) {
            Double partAsPercent = (Double) effectiveValues.get(0);
            Double whole = (Double) effectiveValues.get(1);

            resultObj = partAsPercent*whole/100;
        }
        else {
            resultObj = new UndefinedNumber();
        }

        return resultObj;
    }
}
