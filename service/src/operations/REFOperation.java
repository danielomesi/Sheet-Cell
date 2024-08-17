package operations;

import entities.Cell;
import entities.CellCoordinates;
import entities.core.CoreCell;
import entities.core.CoreSheet;
import exceptions.InvalidArgumentException;
import utils.Utils;

import java.util.List;

public class REFOperation extends Operation {
    public REFOperation(CoreSheet sheet, CellCoordinates coordinates, List<Object> arguments) {
        super.sheet = sheet;
        super.coordinates = coordinates;
        super.name = "REF";
        super.arguments = arguments;
    }

    @Override
    public Object execute() {
        Object arg = getArgValue(arguments.getFirst());
        Object obj = null;
        CoreCell referencingCell = Utils.getCellObjectFromCellID(sheet, coordinates.getCellID());
        try {
            CoreCell referencedCell = Utils.getCellObjectFromCellID(sheet, (String) arg);
            obj = referencedCell.getEffectiveValue();
            referencingCell.getCellsAffectingMe().add(referencedCell.getCoordinates());
            referencedCell.getCellsAffectedByMe().add(referencingCell.getCoordinates());

        }
        catch (Exception e) {
            throw new InvalidArgumentException("The value '" + arg + "' is not a valid cell ID");
        }

        return obj;
    }
}
