package operations.impl.range;

import entities.cell.CoreCell;
import entities.coordinates.CoordinateFactory;
import entities.coordinates.Coordinates;
import entities.range.Range;
import entities.sheet.CoreSheet;
import entities.types.undefined.UndefinedNumber;
import exceptions.InvalidRangeException;
import operations.core.ObjectWrapper;
import operations.core.Operation;

import java.util.List;

public class SUMRangeOperation extends Operation {
    public SUMRangeOperation(CoreSheet sheet, Coordinates coordinates, List<Object> arguments) {
        super.sheet = sheet;
        this.coordinates = coordinates;
        super.name = "SUM";
        super.arguments = arguments;
    }

    @Override
    public ObjectWrapper execute() {
        Object resultObj;
        List<ObjectWrapper> effectiveValues = convertToNonOperationObjects();
        boolean isRefNested = isOneOfTheArgumentsAReference(effectiveValues);
        Class<?>[] expectedClazzes ={String.class};
        if (areArgumentsTypesValid(expectedClazzes,effectiveValues)) {
            String rangeName = effectiveValues.getFirst().getObj().toString();
            Range range = getRangeOrThrow(rangeName);
            updateDependenciesOfRange(range);
            List<Number> numberList = getRangesAsListOfNumbers(range);
            resultObj = calculateSumOfRange(numberList);
        }
        else {
            resultObj = new UndefinedNumber();
        }

        return new ObjectWrapper(resultObj, isRefNested);
    }

    private Double calculateSumOfRange(List<Number> numberList) {
        double sum = 0;
        for (Number number : numberList) {
            sum += number.doubleValue();
        }

        return sum;
    }
}
