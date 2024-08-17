package operations;

import entities.core.CoreSheet;
import exceptions.StringIndexOutOfBoundsException;

import java.util.List;

public class SUBOperation extends Operation {
    public SUBOperation(CoreSheet sheet, List<Object> arguments) {
        super.sheet = sheet;
        super.name = "SUB";
        super.arguments = arguments;
    }

    @Override
    public String execute() {
        List<Object> nonOperationObjects = convertToNonOperationObjects();
        Class<?>[] expectedClazzes ={String.class, Number.class, Number.class};
        validateArgumentsTypes(expectedClazzes, nonOperationObjects);
        String str = (String) arguments.getFirst();
        int startIndex = (Integer) arguments.get(1);
        int endIndex = (Integer) arguments.get(2);
        String result;

        try {
           result = str.substring(startIndex, endIndex);
        }
        catch (Exception e) {
            result = "!UNDEFINED";
        }

        return result;
    }

}
