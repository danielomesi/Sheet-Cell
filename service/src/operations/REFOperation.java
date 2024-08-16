package operations;

import entities.core.CoreCell;
import entities.core.CoreSheet;
import exceptions.InvalidArgumentException;
import utils.Utils;

import java.util.List;

public class REFOperation extends Operation {
    public REFOperation(CoreSheet sheet, List<Object> arguments) {
        super.sheet = sheet;
        super.name = "REF";
        super.arguments = arguments;
    }

    @Override
    public Object execute() {
        Object arg = getArgValue(arguments.getFirst());
        Object obj = null;
        try {
            obj = Utils.getCellObjectFromCellID(sheet, (String) arg).getEffectiveValue();
        }
        catch (Exception e) {
            throw new InvalidArgumentException("The value '" + arg + "' is not a valid cell ID");
        }

        return obj;
    }
}
