package service.entities;

import service.utils.Utils;

public class CellCoordinates
{
    private int row;
    private int col;

    public CellCoordinates(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public CellCoordinates(Cell cell) {
        this.row = cell.getRow();
        this.col = cell.getCol();
    }

    public int getCol() {return col;}
    public void setCol(int col) {this.col = col;}
    public int getRow() {return row;}
    public void setRow(int row) {this.row = row;}

    public static String getCellNameFromIndices(int rowIndex, int colIndex) {
        StringBuilder columnLetters = new StringBuilder();
        int dividend = colIndex + 1; // Adding 1 to handle zero-based index
        while (dividend > 0) {
            int modulo = (dividend - 1) % 26;
            columnLetters.insert(0, (char) (modulo + 'A'));
            dividend = (dividend - modulo) / 26;
        }

        int rowNumber = rowIndex + 1;

        return columnLetters.append(rowNumber).toString();
    }

    @Override
    public String toString() {
        return getCellNameFromIndices(row,col);
    }
}
