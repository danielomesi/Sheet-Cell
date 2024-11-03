package entities.coordinates;

import entities.cell.Cell;
import service_exceptions.CellOutOfBoundsException;

import java.io.Serializable;

public class Coordinates implements Serializable
{
    private final int row;
    private final int col;

    public Coordinates(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Coordinates(Cell cell) {
        this.row = cell.getCoordinates().getRow();
        this.col = cell.getCoordinates().getCol();
    }

    public Coordinates(String cellID) {
        this(getRowIndexFromCellID(cellID),getColIndexFromCellID(cellID));
    }

    public int getCol() {return col;}
    public int getRow() {return row;}

    public String getCellID()  {
        return getCellIDFromIndices(row, col);
    }

    public static int getRowIndexFromCellID(String cellID) {
        String rowPart = cellID.replaceAll("\\D", "");
        if (rowPart.isEmpty()) {
            throw new IllegalArgumentException("Invalid cell format");
        }

        return Integer.parseInt(rowPart) - 1;
    }

    public static int getColIndexFromCellID(String cellID) {
        String columnPart = cellID.replaceAll("\\d", "");
        if (columnPart.length() != 1) {
            throw new IllegalArgumentException("Invalid cell format");
        }
        Character colChar = columnPart.charAt(0);

        return convertColumnCharToIndex(colChar);
    }

    public static int convertColumnStringToIndex(String columnString) {
        int result;
        if (columnString !=null && columnString.length() == 1) {
            char colChar = columnString.charAt(0);
            colChar = Character.toUpperCase(colChar);
            if (colChar >= 'A' && colChar <= 'Z') {
                result = convertColumnCharToIndex(colChar);
            }
            else {
                throw new IllegalArgumentException("Column id must be an alphabetical letter");
            }
        }
        else {
            throw new IllegalArgumentException("Invalid column id representation format");
        }

        return result;
    }

    public static int convertColumnCharToIndex(Character ch) {
        ch = Character.toUpperCase(ch);
        return ch - 'A';
    }

    public static String getCellIDFromIndices(int rowIndex, int colIndex) {
        char colChar = (char) ('A' + colIndex);
        int rowNumber = rowIndex + 1;

        return colChar + Integer.toString(rowNumber);
    }

    @Override
    public String toString() {
        return getCellIDFromIndices(row,col);
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

    public static String numberToLetter(int number) {
        if (number < 0 || number > 25) {
            throw new CellOutOfBoundsException("Can't find a cell with a column which is not a letter");
        }
        char ch = (char) ('A' + number);
        return Character.toString(ch);
    }


}
