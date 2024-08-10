package ui.console;

import service.Engine;
import service.entities.Cell;
import service.entities.Sheet;
import service.EngineImpl;

public class Application {

    public static void main(String[] args) throws CloneNotSupportedException {
        Engine engine = new EngineImpl();
        engine.loadSheetFromDummyData();
        try {
            System.out.println("Before: ");
            printSheet(engine.getSheet());
            engine.UpdateSpecificCell("E6", "{MINUS, {PLUS,A4,5},{TIMES,D7,G2}}");
            engine.UpdateSpecificCell("E7", "{TIMES, A4,A4}");
            System.out.println("After: ");
            printSheet(engine.getSheet());
            engine.UpdateSpecificCell("A4", "6");
            System.out.println("After 2: ");
            printSheet(engine.getSheet());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }


    }

    public static void printSheet(Sheet sheet) {
        Cell[][] cellsTable = sheet.getCellsTable();
        int numOfColumns = cellsTable[0].length;
        int[] columnWidths = new int[numOfColumns];

        // Determine the maximum width for each column
        for (int i = 0; i < cellsTable.length; i++) {
            for (int j = 0; j < numOfColumns; j++) {
                Object value = cellsTable[i][j].getEffectiveValue();
                int valueLength = (value != null ? value.toString().length() : 4); // 4 for "NULL"
                if (valueLength > columnWidths[j]) {
                    columnWidths[j] = valueLength;
                }
            }
        }

        // Determine width for the row index column
        int rowIndexWidth = Integer.toString(cellsTable.length).length();

        // Print the column headers with row index space
        System.out.print(" ".repeat(rowIndexWidth) + " | ");  // Space for row index
        for (int j = 0; j < numOfColumns; j++) {
            String colName = getColumnName(j);
            System.out.print(centerText(colName, columnWidths[j]));
            if (j < numOfColumns - 1) {
                System.out.print(" | ");
            }
        }
        System.out.println();

        // Print the rows with the corresponding values and row indices
        for (int i = 0; i < cellsTable.length; i++) {
            String rowIndex = Integer.toString(i + 1);
            System.out.print(centerText(rowIndex, rowIndexWidth) + " | ");  // Row index

            for (int j = 0; j < numOfColumns; j++) {
                Object value = cellsTable[i][j].getEffectiveValue();
                String valueString = value != null ? value.toString() : "NULL";
                System.out.print(centerText(valueString, columnWidths[j]));
                if (j < numOfColumns - 1) {
                    System.out.print(" | ");
                }
            }
            System.out.println();
        }
    }

    // Helper method to center text in a given width
    private static String centerText(String text, int width) {
        if (text.length() >= width) {
            return text;
        }
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text + " ".repeat(width - text.length() - padding);
    }

    // Helper method to get the column name from the index
    private static String getColumnName(int index) {
        StringBuilder columnName = new StringBuilder();
        while (index >= 0) {
            columnName.insert(0, (char) ('A' + index % 26));
            index = index / 26 - 1;
        }
        return columnName.toString();
    }
}
