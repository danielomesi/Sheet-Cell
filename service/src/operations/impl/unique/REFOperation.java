package operations.impl.unique;

import entities.coordinates.Coordinates;
import entities.cell.CoreCell;
import entities.coordinates.CoordinateFactory;
import entities.sheet.CoreSheet;
import exceptions.InvalidArgumentException;
import operations.core.Operation;

import java.util.List;

import static operations.core.OperationFactory.getArgValue;

public class REFOperation extends Operation {
    public REFOperation(CoreSheet sheet, Coordinates coordinates, List<Object> arguments) {
        super.sheet = sheet;
        super.coordinates = coordinates;
        super.name = "REF";
        super.arguments = arguments;
    }

    @Override
    public Object execute() {
        Object arg = getArgValue(arguments.getFirst());
        Object obj = null;
        CoreCell referencingCell = CoordinateFactory.getCellObjectFromCellID(sheet, coordinates.getCellID());
        try {
            Coordinates referencedCoordinates = new Coordinates(arg.toString().toUpperCase());
            CoreCell referencedCell;
            if (sheet.getCoreCellsMap().containsKey(referencedCoordinates)) {
                referencedCell = sheet.getCoreCellsMap().get(referencedCoordinates);
            }
            else {
                referencedCell = new CoreCell(sheet,referencedCoordinates.getRow(),referencedCoordinates.getCol());
                sheet.getCoreCellsMap().put(referencedCoordinates,referencedCell);
            }
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
