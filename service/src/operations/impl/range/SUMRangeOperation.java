package operations.impl.range;

import entities.coordinates.Coordinates;
import entities.range.Range;
import entities.sheet.CoreSheet;
import entities.types.undefined.UndefinedNumber;
import operations.core.Operation;

import java.util.List;

import static operations.core.OperationFactory.*;

public class SUMRangeOperation extends Operation implements RangeOperation {
    public SUMRangeOperation(CoreSheet sheet, Coordinates coordinates, List<Object> arguments) {
        super.sheet = sheet;
        this.coordinates = coordinates;
        super.name = "SUM";
        super.arguments = arguments;
    }

    @Override
    public Object execute() {
        Object resultObj;
        List<Object> effectiveValues = convertToNonOperationObjects(arguments);
        Class<?>[] expectedClazzes ={String.class};
        if (areActualArgumentsMatchingToExpectedArguments(expectedClazzes,effectiveValues)) {
            String rangeName = effectiveValues.getFirst().toString();
            Range range = getRangeOrThrow(sheet,rangeName);
            updateDependenciesOfRange(range,sheet,coordinates);
            List<Number> numberList = getRangesAsListOfNumbers(range,sheet);
            resultObj = calculateSumOfRange(numberList);
        }
        else {
            resultObj = new UndefinedNumber();
        }

        return resultObj;
    }

    private Double calculateSumOfRange(List<Number> numberList) {
        double sum = 0;
        for (Number number : numberList) {
            sum += number.doubleValue();
        }

        return sum;
    }
}
