package operations;

import entities.cell.CellCoordinates;
import entities.cell.CoreCell;
import entities.sheet.CoreSheet;
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
            CoreCell referencedCell = Utils.getCellObjectFromCellID(sheet, arg.toString().toUpperCase());
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
