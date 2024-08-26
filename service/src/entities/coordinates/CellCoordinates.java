package entities.coordinates;

import entities.cell.CoreCell;
import entities.sheet.CoreSheet;
import exceptions.CellOutOfBoundsException;
import exceptions.InvalidArgumentException;

import java.io.Serializable;

public class CellCoordinates implements Cloneable, Serializable
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

    public String getCellID()  {
        return CellCoordinates.getCellIDFromIndices(row, col);
    }
    public String getCellID(int row, int col)  {
        return CellCoordinates.getCellIDFromIndices(row, col);
    }

    @Override
    public String toString() {
        return CellCoordinates.getCellIDFromIndices(row,col);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CellCoordinates other = (CellCoordinates) o;
        return row == other.row && col == other.col;
    }

    @Override
    public int hashCode() {
        return 31*row + col; //31 is a prime number and it will make this function generate a unique id for each
    }

    public static CoreCell getCellObjectFromCellID(CoreSheet sheet, String cellName) {
        if (cellName == null || cellName.isEmpty()) {
            throw new InvalidArgumentException("Cell name cannot be null or empty", cellName);
        }

        String rowPart = cellName.replaceAll("\\D", "");
        String columnPart = cellName.replaceAll("\\d", "");

        if (columnPart.isEmpty() || rowPart.isEmpty()) {
            throw new InvalidArgumentException("Invalid cell format", columnPart+rowPart);
        }

        int colIndex = convertColumnLettersToIndex(columnPart);

        int rowIndex = Integer.parseInt(rowPart) - 1;

        if (rowIndex < 0 || rowIndex >= sheet.getNumOfRows() ||
                colIndex < 0 || colIndex >= sheet.getNumOfColumns()) {
            throw new CellOutOfBoundsException("Cell is out bounds. Maximum number of rows is " + sheet.getNumOfRows()
                    + ", maximum number of columns is " + sheet.getNumOfColumns(), columnPart+rowPart);
        }

        return sheet.getCellsTable()[rowIndex][colIndex];
    }

    public static int convertColumnLettersToIndex(String letters) {
        int columnIndex = 0;
        letters = letters.toUpperCase();
        int length = letters.length();
        for (int i = 0; i < length; i++) {
            char letter = letters.charAt(i);
            columnIndex = columnIndex * 26 + (letter - 'A');
        }
        return columnIndex;
    }


    public static CellCoordinates getIndicesFromCellObject(CoreCell cell) {
        return new CellCoordinates(cell.getCoordinates().getRow(), cell.getCoordinates().getCol());
    }

    public static String getCellIDFromCellObject(CoreCell cell) {
        CellCoordinates coordinates = getIndicesFromCellObject(cell);
        return getCellIDFromIndices(cell.getCoordinates().getRow(), cell.getCoordinates().getCol());
    }

    public static CoreCell getCellObjectFromIndices(CoreSheet sheet, int rowIndex, int colIndex) {
        return sheet.getCellsTable()[rowIndex][colIndex];
    }

    public static String getCellIDFromIndices(int rowIndex, int colIndex) {
        StringBuilder columnLetters = new StringBuilder();
        int dividend = colIndex + 1; // Adding 1 to handle zero-based index
        while (dividend > 0) {
            int modulo = (dividend - 1) % 26;
            columnLetters.insert(0, (char) (modulo + 'A'));
            dividend = (dividend - modulo) / 26;
        }

        Integer rowNumber = rowIndex + 1;
        return  columnLetters + rowNumber.toString();
    }
}
