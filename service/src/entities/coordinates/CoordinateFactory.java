package entities.coordinates;

import entities.cell.CoreCell;
import entities.sheet.CoreSheet;
import exceptions.CellOutOfBoundsException;
import exceptions.InvalidArgumentException;

public class CoordinateFactory {

    public static CoreCell getCellObjectFromCellID(CoreSheet sheet, String cellName) {
        Coordinates coordinates = new Coordinates(cellName);

        int rowIndex = coordinates.getRow();
        int colIndex = coordinates.getCol();

        if (rowIndex < 0 || rowIndex >= sheet.getNumOfRows() ||
                colIndex < 0 || colIndex >= sheet.getNumOfCols()) {
            throw new CellOutOfBoundsException("Cell is out bounds. Valid row range: [1-" + sheet.getNumOfRows()
                    + "], Valid column range: [1-" + sheet.getNumOfCols() + "]", (rowIndex+1) + "," + (colIndex+1) );
        }

        return getCellObjectFromIndices(sheet,rowIndex,colIndex);
    }

    public static int convertColumnCharToIndex(Character ch) {
        ch = Character.toUpperCase(ch);
        return ch - 'A';
    }

    public static Coordinates getIndicesFromCellObject(CoreCell cell) {
        return new Coordinates(cell.getCoordinates().getRow(), cell.getCoordinates().getCol());
    }

    public static String getCellIDFromCellObject(CoreCell cell) {
        Coordinates coordinates = getIndicesFromCellObject(cell);
        return getCellIDFromIndices(cell.getCoordinates().getRow(), cell.getCoordinates().getCol());
    }

    public static CoreCell getCellObjectFromIndices(CoreSheet sheet, int rowIndex, int colIndex) {
        Coordinates coordinates = new Coordinates(rowIndex, colIndex);

        return sheet.getCoreCellsMap().get(coordinates);
    }

    public static String getCellIDFromIndices(int rowIndex, int colIndex) {
        char colChar = (char) ('A' + colIndex);
        int rowNumber = rowIndex + 1;

        return colChar + Integer.toString(rowNumber);
    }

    public static int[] getIndicesFromCellID(String cellID) {
        int[] indices = new int[2];
        if (cellID == null || cellID.isEmpty()) {
            throw new InvalidArgumentException("Cell name cannot be null or empty", cellID);
        }

        int rowIndex = getRowIndexFromCellID(cellID);
        int colIndex = getColIndexFromCellID(cellID);
        indices[0] = rowIndex;
        indices[1] = colIndex;

        return indices;
    }

    public static int getRowIndexFromCellID(String cellID) {
        String rowPart = cellID.replaceAll("\\D", "");
        if (rowPart.isEmpty()) {
            throw new InvalidArgumentException("Invalid cell format", cellID);
        }

        return Integer.parseInt(rowPart) - 1;
    }

    public static int getColIndexFromCellID(String cellID) {
        String columnPart = cellID.replaceAll("\\d", "");
        if (columnPart.length() != 1) {
            throw new InvalidArgumentException("Invalid cell format", cellID);
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
                throw new InvalidArgumentException("Column id must be an alphabetical letter", columnString);
            }
        }
        else {
            throw new InvalidArgumentException("Invalid column id representation format", columnString);
        }

        return result;
    }

    public static String numberToLetter(int number) {
        if (number < 0 || number > 25) {
            throw new CellOutOfBoundsException("Can't find a cell with a column which is not a letter");
        }
        char ch = (char) ('A' + number);
        return Character.toString(ch);
    }
}
