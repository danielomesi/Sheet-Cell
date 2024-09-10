package entities.sheet;

import entities.cell.Cell;
import entities.range.Range;

import java.io.Serializable;

public interface Sheet extends Serializable {
    public Cell getCell(int row, int col);
    public int getNumOfRows();
    public int getNumOfColumns();
    public int getVersion();
    public Layout getLayout();
    public String getName();
    public int getNumOfCellsChanged();
    public Range getRange(String name);
}
