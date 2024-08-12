package console;

import engine.Engine;
import engine.EngineImpl;
import entities.Cell;
import entities.Sheet;

public class Application {

    public static void main(String[] args) throws CloneNotSupportedException {
        Engine engine = new EngineImpl();
        engine.loadSheetFromDummyData();
        try {
            System.out.println("Before: ");
            printSheet(engine.getSheet());
            engine.updateSpecificCell("E6", "{MINUS, 8,5}");
            System.out.println("After: ");
            printSheet(engine.getSheet());

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
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
                int valueLength = (value != null ? objectValueAsString(value).length() : 4); // 4 for "NULL"
                if (valueLength > columnWidths[j]) {
                    columnWidths[j] = valueLength;
                }
            }
        }

        // Determine width for the row index column
        int rowIndexWidth = Integer.toString(cellsTable.length).length();

        //Prints the Version
        System.out.println("Version: " + sheet.getVersion());

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
                String valueString = value != null ? objectValueAsString(value) : "NULL";
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

    private static String objectValueAsString(Object obj) {
        if (obj instanceof Double doubleNum) {
            if (doubleNum % 1 == 0) {
                return String.format("%.0f", doubleNum);
            }
        }
        return obj.toString();
    }

}
