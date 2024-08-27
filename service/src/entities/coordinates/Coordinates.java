package entities.coordinates;

import entities.cell.CoreCell;

import java.io.Serializable;

public class Coordinates implements Serializable
{
    private final int row;
    private final int col;

    public Coordinates(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Coordinates(CoreCell cell) {
        this.row = cell.getCoordinates().getRow();
        this.col = cell.getCoordinates().getCol();
    }

    public Coordinates(String cellID) {
        this(CoordinateFactory.getRowIndexFromCellID(cellID), CoordinateFactory.getColIndexFromCellID(cellID));
    }

    public int getCol() {return col;}
    public int getRow() {return row;}

    public String getCellID()  {
        return CoordinateFactory.getCellIDFromIndices(row, col);
    }

    @Override
    public String toString() {
        return CoordinateFactory.getCellIDFromIndices(row,col);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Coordinates other = (Coordinates) o;
        return row == other.row && col == other.col;
    }

    @Override
    public int hashCode() {
        return 31*row + col; //31 is a prime number and it will make this function generate a unique id for each
    }


}
