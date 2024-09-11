package operations.impl.unique;

import entities.coordinates.Coordinates;
import entities.sheet.CoreSheet;
import entities.types.undefined.UndefinedBoolean;
import exceptions.InvalidArgumentException;
import operations.core.Operation;

import java.util.List;

import static operations.core.OperationFactory.areActualArgumentsMatchingToExpectedArguments;
import static operations.core.OperationFactory.convertToNonOperationObjects;

public class IFOperation extends Operation {
    public IFOperation(CoreSheet sheet, Coordinates coordinates, List<Object> arguments) {
        super.sheet = sheet;
        super.coordinates = coordinates;
        super.name = "IF";
        super.arguments = arguments;
    }

    @Override
    public Object execute() {
        Object resultObj;
        List<Object> effectiveValues = convertToNonOperationObjects(arguments);
        Class<?>[] expectedClazzes ={Boolean.class, Object.class, Object.class};
        validateLegalArgumentsOrThrow(effectiveValues);
        if (areActualArgumentsMatchingToExpectedArguments(expectedClazzes,effectiveValues)) {
            Boolean ifResult = (Boolean) effectiveValues.getFirst();
            if (ifResult) {
                resultObj = effectiveValues.get(1);
            }
            else {
                resultObj = effectiveValues.get(2);
            }
        }
        else {
            resultObj = new UndefinedBoolean();
        }

        return resultObj;
    }

    private void validateLegalArgumentsOrThrow(List<Object> effectiveValues) {
        Class<?> thenClazz = effectiveValues.get(1).getClass();
        Class<?> elseClazz = effectiveValues.get(2).getClass();
        if (thenClazz != elseClazz) {
            throw new InvalidArgumentException("The arguments in the function are not of the same type",coordinates,name);
        }
    }

}
