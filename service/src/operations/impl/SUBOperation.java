package operations.impl;

import entities.coordinates.Coordinates;
import entities.sheet.CoreSheet;
import entities.types.NumberWrapper;
import entities.types.undefined.UndefinedString;
import operations.core.ObjectWrapper;
import operations.core.Operation;

import java.util.List;

public class SUBOperation extends Operation {
    public SUBOperation(CoreSheet sheet, Coordinates coordinates, List<Object> arguments) {
        super.sheet = sheet;
        this.coordinates = coordinates;
        super.name = "SUB";
        super.arguments = arguments;
    }

    @Override
    public ObjectWrapper execute() {
        List<ObjectWrapper> effectiveValues = convertToNonOperationObjects();
        boolean isRefNested = isOneOfTheArgumentsAReference(effectiveValues);
        Class<?>[] expectedClazzes ={String.class, NumberWrapper.class, NumberWrapper.class};
        if (areArgumentsTypesValid(expectedClazzes,effectiveValues)) {
            String str = (String) effectiveValues.getFirst().getObj();
            int startIndex = ((NumberWrapper) effectiveValues.get(1).getObj()).getIntValue();
            int endIndex = ((NumberWrapper) effectiveValues.get(2).getObj()).getIntValue();
            String result;

            try {
                result = str.substring(startIndex, endIndex + 1);
            }
            catch (Exception e) {
                return new ObjectWrapper(new UndefinedString(),isRefNested);
            }

            return new ObjectWrapper(result,isRefNested);
        }
        else {
            return new ObjectWrapper(new UndefinedString(),isRefNested);
        }

    }

}
