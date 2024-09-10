package entities.sheet;

import entities.cell.Cell;
import entities.cell.DTOCell;
import entities.coordinates.Coordinates;
import entities.range.Range;

import java.util.HashMap;
import java.util.Map;

public class DTOSheet implements Sheet {
    private Map<Coordinates,DTOCell> cellsMap;
    private Map<String, Range> rangesMap;
    private final int numOfRows;
    private final int numOfColumns;
    private final int version;
    private final int numOfCellsChanged;
    private final Layout layout;
    private final String name;

    public DTOSheet(CoreSheet coreSheet) {
        this.numOfRows = coreSheet.getNumOfRows();
        this.numOfColumns = coreSheet.getNumOfColumns();
        this.version = coreSheet.getVersion();
        this.layout = coreSheet.getLayout();
        this.name = coreSheet.getName();
        this.numOfCellsChanged = coreSheet.getNumOfCellsChanged();
        this.rangesMap = coreSheet.getRangesMap();
        if (coreSheet.getCoreCellsMap() != null) {
            this.cellsMap = new HashMap<>();
            coreSheet.getCoreCellsMap().forEach((coordinates, cell) -> cellsMap.put(coordinates, new DTOCell(cell)));
        }
    }

    public int getNumOfRows() {return this.numOfRows;}
    public int getNumOfColumns() {return this.numOfColumns;}
    public int getVersion() {return this.version;}
    public Range getRange(String name) {return rangesMap.get(name);}
    public Layout getLayout() {return layout;}
    public String getName() {return name;}
    public int getNumOfCellsChanged() {return this.numOfCellsChanged;}
    public Cell getCell(int row, int col) {return cellsMap.get(new Coordinates(row, col));}
}
