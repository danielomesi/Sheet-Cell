package operations;

import entities.core.CoreSheet;

import java.util.ArrayList;
import java.util.List;

public class ConcatOperation extends Operation {
    public ConcatOperation(CoreSheet sheet, List<Object> arguments) {
        super.sheet = sheet;
        super.name = "CONCAT";
        super.arguments = arguments;
    }

    @Override
    public String execute() {
        List<Object> nonOperationObjects = convertToNonOperationObjects();
        Class<?>[] expectedClazzes ={String.class, String.class};
        validateArgumentsTypes(expectedClazzes, nonOperationObjects);
        List<String> strings = convertToList(nonOperationObjects, String.class);

        return strings.get(0) + strings.get(1);
    }
}
