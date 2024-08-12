package entities;

import entities.core.CoreCell;
import utils.Utils;

public class CellCoordinates implements Cloneable
{
    private int row;
    private int col;

    public CellCoordinates(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public CellCoordinates(CoreCell cell) {
        this.row = cell.getCoordinates().getRow();
        this.col = cell.getCoordinates().getCol();
    }

    @Override
    public CellCoordinates clone() throws CloneNotSupportedException {
        CellCoordinates cellCoordinates = (CellCoordinates) super.clone();
        cellCoordinates.row = this.row;
        cellCoordinates.col = this.col;
        return cellCoordinates;
    }

    public int getCol() {return col;}
    public int getRow() {return row;}

    public String getCellID(int row, int col)  {
        return Utils.getCellIDFromIndices(row, col);
    }

    @Override
    public String toString() {
        return Utils.getCellIDFromIndices(row,col);
    }
}
