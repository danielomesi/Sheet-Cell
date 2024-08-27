package operations.impl;

import entities.coordinates.Coordinates;
import entities.sheet.CoreSheet;
import entities.types.NumberWrapper;
import entities.types.undefined.UndefinedNumber;
import operations.core.ObjectBooleanPair;
import operations.core.Operation;

import java.util.List;

public class ABSOperation extends Operation {

    public ABSOperation(CoreSheet sheet, Coordinates coordinates, List<Object> arguments) {
        super.sheet = sheet;
        this.coordinates = coordinates;
        super.name = "ABS";
        super.arguments = arguments;
    }

    @Override
    public Object execute() {
        List<ObjectBooleanPair> effectiveValues = convertToNonOperationObjects();
        Class<?>[] expectedClazzes ={NumberWrapper.class};
        if (areArgumentsTypesValid(expectedClazzes,effectiveValues)) {
            NumberWrapper number = (NumberWrapper)(effectiveValues.getFirst().getObj());

            return new NumberWrapper(Math.abs(number.getDoubleValue()));
        }
        else {
            return new UndefinedNumber();
        }
    }
}
