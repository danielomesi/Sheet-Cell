package entities.sheet;

import entities.cell.Cell;
import entities.cell.DTOCell;
import entities.coordinates.Coordinates;
import entities.range.RangeDTO;
import entities.range.RangeInterface;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class DTOSheet implements Sheet {
    private final String ownerUsername;
    private Map<Coordinates,DTOCell> cellsMap;
    private final Map<String, RangeDTO> rangesMap;
    private final int numOfRows;
    private final int numOfColumns;
    private final int version;
    private final int numOfCellsChanged;
    private final Layout layout;
    private final String name;

    public DTOSheet(Sheet coreSheet) {
        this.numOfRows = coreSheet.getNumOfRows();
        this.numOfColumns = coreSheet.getNumOfCols();
        this.version = coreSheet.getVersion();
        this.layout = coreSheet.getLayout();
        this.name = coreSheet.getName();
        this.numOfCellsChanged = coreSheet.getNumOfCellsChanged();
        this.rangesMap = coreSheet.getRangesDTOeMap();
        this.ownerUsername = coreSheet.getOwnerUsername();
        if (coreSheet.getCellMap() != null) {
            this.cellsMap = new HashMap<>();
            coreSheet.getCellMap().forEach((coordinates, cell) -> cellsMap.put(coordinates, new DTOCell(cell)));
        }
    }

    public String getOwnerUsername() {return ownerUsername;}
    public int getNumOfRows() {return this.numOfRows;}
    public int getNumOfCols() {return this.numOfColumns;}
    public int getVersion() {return this.version;}
    public RangeInterface getRange(String name) {return rangesMap.get(name);}
    public Set<String> getRangesNames() {return rangesMap.keySet();}

    @Override
    public Map<String, RangeDTO> getRangesDTOeMap() {return rangesMap;}

    public Layout getLayout() {return layout;}
    public String getName() {return name;}
    public int getNumOfCellsChanged() {return this.numOfCellsChanged;}
    public Cell getCell(int row, int col) {return cellsMap.get(new Coordinates(row, col));}

    @Override
    public Map<Coordinates, Cell> getCellMap() {
        Map<Coordinates, Cell> result = cellsMap.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return result;
    }
}
