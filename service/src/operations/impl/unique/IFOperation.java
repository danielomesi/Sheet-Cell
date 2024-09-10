package operations.impl.unique;

import entities.cell.CoreCell;
import entities.coordinates.CoordinateFactory;
import entities.coordinates.Coordinates;
import entities.sheet.CoreSheet;
import entities.types.undefined.UndefinedBoolean;
import exceptions.InvalidArgumentException;
import operations.core.ObjectWrapper;
import operations.core.Operation;
import operations.impl.logical.ANDOperation;
import operations.impl.logical.EQUALOperation;
import operations.impl.logical.NOTOperation;
import operations.impl.logical.OROperation;

import java.util.List;

public class IFOperation extends Operation {
    public IFOperation(CoreSheet sheet, Coordinates coordinates, List<Object> arguments) {
        super.sheet = sheet;
        super.coordinates = coordinates;
        super.name = "IF";
        super.arguments = arguments;
    }

    @Override
    public ObjectWrapper execute() {
        Object resultObj;
        List<ObjectWrapper> effectiveValues = convertToNonOperationObjects();
        boolean isRefNested = isOneOfTheArgumentsAReference(effectiveValues);
        Class<?>[] expectedClazzes ={Boolean.class, Object.class, Object.class};
        if (areArgumentsTypesValid(expectedClazzes,effectiveValues) && isLegalArguments(effectiveValues)) {
            Boolean ifResult = (Boolean) effectiveValues.getFirst().getObj();
            if (ifResult) {
                resultObj = effectiveValues.get(1).getObj();
            }
            else {
                resultObj = effectiveValues.get(2).getObj();
            }
        }
        else {
            resultObj = new UndefinedBoolean();
        }

        return new ObjectWrapper(resultObj,isRefNested);
    }

    private boolean isLegalArguments(List<ObjectWrapper> effectiveValues) {
        Class<?> thenClazz = effectiveValues.get(1).getObj().getClass();
        Class<?> elseClazz = effectiveValues.get(2).getObj().getClass();
        if (thenClazz != elseClazz) {
            return false;
        }

        return true;
    }

}
