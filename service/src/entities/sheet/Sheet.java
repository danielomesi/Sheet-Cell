package entities.sheet;

import entities.cell.Cell;
import entities.range.Range;

import java.io.Serializable;
import java.util.Set;

public interface Sheet extends Serializable {
    public Cell getCell(int row, int col);
    public int getNumOfRows();
    public int getNumOfCols();
    public int getVersion();
    public Layout getLayout();
    public String getName();
    public int getNumOfCellsChanged();
    public Range getRange(String name);
    public Set<String> getRangesNames();
}
