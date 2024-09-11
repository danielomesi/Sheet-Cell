package operations.impl.string;

import entities.coordinates.Coordinates;
import entities.sheet.CoreSheet;
import entities.types.undefined.UndefinedString;
import operations.core.Operation;

import java.util.List;

import static operations.core.OperationFactory.areActualArgumentsMatchingToExpectedArguments;
import static operations.core.OperationFactory.convertToNonOperationObjects;

public class SUBOperation extends Operation {
    public SUBOperation(CoreSheet sheet, Coordinates coordinates, List<Object> arguments) {
        super.sheet = sheet;
        this.coordinates = coordinates;
        super.name = "SUB";
        super.arguments = arguments;
    }

    @Override
    public Object execute() {
        Object resultObj;
        List<Object> effectiveValues = convertToNonOperationObjects(arguments);
        Class<?>[] expectedClazzes ={String.class, Number.class, Number.class};
        if (areActualArgumentsMatchingToExpectedArguments(expectedClazzes,effectiveValues)) {
            String str = (String) effectiveValues.getFirst();
            int startIndex = ((Number) effectiveValues.get(1)).intValue();
            int endIndex = ((Number) effectiveValues.get(2)).intValue();

            try {
                resultObj = str.substring(startIndex, endIndex + 1);
            }
            catch (Exception e) {
               resultObj = new UndefinedString();
            }
        }
        else {
            resultObj = new UndefinedString();
        }

        return resultObj;
    }
}
