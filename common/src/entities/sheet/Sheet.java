package entities.sheet;

import entities.cell.Cell;
import entities.coordinates.Coordinates;
import entities.range.RangeDTO;
import entities.range.RangeInterface;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public interface Sheet extends Serializable {
    public String getOwnerUsername();
    public Cell getCell(int row, int col);
    public Map<Coordinates, Cell> getCellMap();
    public int getNumOfRows();
    public int getNumOfCols();
    public int getVersion();
    public Layout getLayout();
    public String getName();
    public int getNumOfCellsChanged();
    public RangeInterface getRange(String name);
    public Set<String> getRangesNames();
    public Map<String, RangeDTO> getRangesDTOeMap();
}
