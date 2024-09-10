package operations.impl.range;

import entities.coordinates.Coordinates;
import entities.range.Range;
import entities.sheet.CoreSheet;
import entities.types.undefined.UndefinedNumber;
import exceptions.InvalidRangeException;
import operations.core.ObjectWrapper;
import operations.core.Operation;

import java.util.List;

public class AVERAGERangeOperation extends Operation {
    public AVERAGERangeOperation(CoreSheet sheet, Coordinates coordinates, List<Object> arguments) {
        super.sheet = sheet;
        this.coordinates = coordinates;
        super.name = "AVERAGE";
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
            resultObj = calculateAverageOfRangeOrThrowIfNoNumbers(numberList);
        }
        else {
            resultObj = new UndefinedNumber();
        }

        return new ObjectWrapper(resultObj, isRefNested);
    }

    private Double calculateAverageOfRangeOrThrowIfNoNumbers(List<Number> numberList) {
        if (numberList.isEmpty()) {
            throw new InvalidRangeException("No numbers found to calculate average");
        }
        int numberOfNumbers = numberList.size();
        double sum = 0;
        for (Number number : numberList) {
            sum += number.doubleValue();
        }

        return sum/(double)numberOfNumbers;
    }
}
