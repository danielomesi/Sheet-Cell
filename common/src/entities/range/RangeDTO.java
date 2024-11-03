package entities.range;

import entities.coordinates.Coordinates;
import entities.sheet.Sheet;

import java.util.HashSet;
import java.util.Set;

public class RangeDTO implements RangeInterface{
    private final Set<Coordinates> cells;
    private final String name;

    public RangeDTO(RangeInterface range) {
        cells = new HashSet<Coordinates>(range.getCells());
        name = range.getName();
    }

    public RangeDTO(String fromCellID, String toCellID) {
        name = null;
        cells = new HashSet<>();
        Coordinates from = new Coordinates(fromCellID);
        Coordinates to = new Coordinates(toCellID);
        int fromRow = from.getRow();
        int fromCol = from.getCol();
        int toRow = to.getRow();
        int toCol = to.getCol();
        for (int i = fromRow; i <= toRow; i++) {
            for (int j = fromCol; j <= toCol; j++) {
                cells.add(new Coordinates(i,j));
            }
        }
    }



    //getters
    public Set<Coordinates> getCells() {return cells;}
    public String getName() {return name;}
}
