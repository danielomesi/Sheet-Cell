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
                colIndex < 0 || colIndex >= sheet.getNumOfColumns()) {
            throw new CellOutOfBoundsException("Cell is out bounds. Valid row range: [1-" + sheet.getNumOfRows()
                    + "], Valid column range: [1-" + sheet.getNumOfColumns() + "]", (rowIndex+1) + "," + (colIndex+1) );
        }

        return getCellObjectFromIndices(sheet,rowIndex,colIndex);
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
        if (columnPart.isEmpty()) {
            throw new InvalidArgumentException("Invalid cell format", cellID);
        }

        return convertColumnLettersToIndex(columnPart);
    }
}
