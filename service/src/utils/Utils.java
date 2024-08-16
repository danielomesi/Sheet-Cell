package utils;

import entities.core.CoreCell;
import entities.CellCoordinates;
import entities.core.CoreSheet;
import exceptions.CellOutOfBoundsException;
import exceptions.InvalidArgumentException;

public class Utils {
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

    private static int convertColumnLettersToIndex(String letters) {
        int columnIndex = 0;
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
        return Utils.getCellIDFromIndices(cell.getCoordinates().getRow(), cell.getCoordinates().getCol());
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

        int rowNumber = rowIndex + 1;

        return columnLetters.append(rowNumber).toString();
    }

}
