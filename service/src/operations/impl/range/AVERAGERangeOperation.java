package operations.impl.range;

import entities.coordinates.Coordinates;
import entities.range.Range;
import entities.sheet.CoreSheet;
import entities.types.undefined.UndefinedNumber;
import exceptions.InvalidRangeException;
import operations.core.Operation;
import operations.core.OperationFactory;

import java.util.List;

import static operations.core.OperationFactory.*;

public class AVERAGERangeOperation extends Operation implements RangeOperation {
    public AVERAGERangeOperation(CoreSheet sheet, Coordinates coordinates, List<Object> arguments) {
        super.sheet = sheet;
        this.coordinates = coordinates;
        super.name = "AVERAGE";
        super.arguments = arguments;
    }

    @Override
    public Object execute() {
        Object resultObj;
        List<Object> effectiveValues = convertToNonOperationObjects(arguments);
        Class<?>[] expectedClazzes ={String.class};
        if (areActualArgumentsMatchingToExpectedArguments(expectedClazzes,effectiveValues)) {
            String rangeName = effectiveValues.getFirst().toString();
            Range range = OperationFactory.getRangeOrThrow(sheet, rangeName);
            updateDependenciesOfRange(range,sheet,coordinates);
            List<Number> numberList = getRangesAsListOfNumbers(range,sheet);
            resultObj = calculateAverageOfRangeOrThrowIfNoNumbers(numberList);
        }
        else {
            resultObj = new UndefinedNumber();
        }

        return resultObj;
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
