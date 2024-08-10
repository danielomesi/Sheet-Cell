package service.utils;

import service.entities.Cell;
import service.entities.CellCoordinates;
import service.entities.Sheet;

public class Utils {
    public static Cell getCellObjectFromCellName(Sheet sheet, String cellName) {
        if (cellName == null || cellName.isEmpty()) {
            throw new IllegalArgumentException("Cell name cannot be null or empty");
        }

        String rowPart = cellName.replaceAll("\\D", "");
        String columnPart = cellName.replaceAll("\\d", "");

        if (columnPart.isEmpty() || rowPart.isEmpty()) {
            throw new IllegalArgumentException("Invalid cell format");
        }

        int colIndex = convertColumnLettersToIndex(columnPart);

        int rowIndex = Integer.parseInt(rowPart) - 1;

        if (rowIndex < 0 || rowIndex >= sheet.getNumOfRows() ||
                colIndex < 0 || colIndex >= sheet.getNumOfColumns()) {
            throw new IllegalArgumentException("Cell out of bounds");
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


    public static int[] getIndicesFromCellObject(Cell cell) {
        int[] indices = new int[2];
        indices[0] = cell.getRow();
        indices[1] = cell.getCol();

        return indices;
    }

    public static String getCellNameFromCellObject(Cell cell) {
        int[] indices = getIndicesFromCellObject(cell);
        return CellCoordinates.getCellNameFromIndices(indices[0], indices[1]);
    }

    public static Cell getCellObjectFromIndices(Sheet sheet, int rowIndex, int colIndex) {
        return sheet.getCellsTable()[rowIndex][colIndex];
    }


}
