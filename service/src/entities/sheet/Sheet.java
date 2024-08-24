package entities.sheet;

import entities.cell.Cell;

public interface Sheet {
    public Cell[][] getCellsTable();
    public int getNumOfRows();
    public int getNumOfColumns();
    public int getVersion();
    public Layout getLayout();
    public String getName();
    public int getNumOfCellsChanged();
}
