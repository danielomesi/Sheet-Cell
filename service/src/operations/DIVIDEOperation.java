package operations;

import entities.core.CoreSheet;
import exceptions.ZeroDivisionException;

import java.util.List;

public class DIVIDEOperation extends Operation {
    public DIVIDEOperation(CoreSheet sheet, List<Object> arguments) {
        super.sheet = sheet;
        super.name = "DIVIDE";
        super.arguments = arguments;
    }

    @Override
    public Double execute() {
        //need to implement nan somehow
        double num1 = 0, num2 = 0, result;
        List<Object> nonOperationObjects = convertToNonOperationObjects();
        Class<?>[] expectedClazzes ={Number.class, Number.class};
        validateArgumentsTypes(expectedClazzes, nonOperationObjects);
        List<Double> doubles = convertToDouble(nonOperationObjects);
        num1 = doubles.get(0);
        num2 = doubles.get(1);

        if (num2 == 0)
        {
            result = Double.NaN;
        }
       else {
           result = num1/num2;
        }

        return result;
    }
}
