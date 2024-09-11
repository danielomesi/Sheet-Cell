package operations.impl.string;

import entities.coordinates.Coordinates;
import entities.sheet.CoreSheet;
import entities.types.undefined.UndefinedString;
import operations.core.Operation;

import java.util.List;

import static operations.core.OperationFactory.areActualArgumentsMatchingToExpectedArguments;
import static operations.core.OperationFactory.convertToNonOperationObjects;

public class ConcatOperation extends Operation {
    public ConcatOperation(CoreSheet sheet, Coordinates coordinates, List<Object> arguments) {
        super.sheet = sheet;
        this.coordinates = coordinates;
        super.name = "CONCAT";
        super.arguments = arguments;
    }

    @Override
    public Object execute() {
        Object resultObj;
        List<Object> effectiveValues = convertToNonOperationObjects(arguments);
        Class<?>[] expectedClazzes ={String.class, String.class};
        if (areActualArgumentsMatchingToExpectedArguments(expectedClazzes,effectiveValues)) {
            String firstString = (String) effectiveValues.get(0);
            String secondString = (String) effectiveValues.get(1);

            resultObj = firstString + secondString;
        }
        else {
            resultObj = new UndefinedString();
        }

        return resultObj;
    }
}
