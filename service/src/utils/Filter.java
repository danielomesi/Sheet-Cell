package utils;

import entities.cell.Cell;
import entities.coordinates.CoordinateFactory;
import entities.sheet.Sheet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Filter {
    public static List<Object> getEffectiveValuesInSpecificCol(Sheet sheet, String colName) {
        List<Object> effectiveValues = new ArrayList<>();
        int numRows = sheet.getNumOfRows();

        for (int row = 0; row < numRows; row++) {
            List<Cell> values = new ArrayList<>();
            int col = CoordinateFactory.convertColumnStringToIndex(colName);
            Cell cell = sheet.getCell(row, col);
            if (cell != null) {
                effectiveValues.add(cell.getEffectiveValue());
            }
        }

        return effectiveValues;
    }
}
