package console;

import entities.Cell;
import entities.CellCoordinates;
import entities.Sheet;
import exceptions.ServiceException;

import java.text.DecimalFormat;
import java.util.Optional;

public class ConsoleUtils {
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
                String valueString = value != null ? objectValueAsString(value) : "";
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
        DecimalFormat wholeNumberFormatter = new DecimalFormat("#,###");
        DecimalFormat decimalFormatter = new DecimalFormat("#,###.00");

        if (obj instanceof Double doubleNum) {
            if (doubleNum % 1 == 0) {
                return wholeNumberFormatter.format(doubleNum);
            } else {
                return decimalFormatter.format(doubleNum);
            }
        }
        return obj.toString();
    }

    public static void printExceptionInfo(Exception e) {
        System.out.println("Unfortunately, an error occurred.");
        if (e instanceof ServiceException) {
            printServiceException((ServiceException) e);
        }
        else {
            printNonServiceException(e);
        }
    }

    private static void printServiceException(ServiceException serviceException) {
        Optional<CellCoordinates> optionalCoordinates = Optional.ofNullable(serviceException.getCellCoordinates());
        Optional<String> optionalInput = Optional.ofNullable(serviceException.getInput());
        System.out.println("Error name: " + serviceException.getExceptionName());
        System.out.println("Error message: " + serviceException.getMessage());
        optionalCoordinates.ifPresent((cellCoordinates) -> System.out.println(("Relevant cell: " + cellCoordinates.getCellID())));
        optionalInput.ifPresent((input) -> System.out.println(("Relevant input: " + input)));
    }

    private static void printNonServiceException(Exception e) {
        System.out.println("Error message: " + e.getMessage());
    }
}
