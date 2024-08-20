package console;

import console.exceptions.ConsoleException;
import entities.cell.Cell;
import entities.cell.CellCoordinates;
import entities.sheet.Sheet;
import exceptions.ServiceException;

import java.text.DecimalFormat;
import java.util.List;

public class ConsolePrintHelper {

    public static void printSheet(Sheet sheet) {
        Cell[][] cellsTable = sheet.getCellsTable();
        int numOfColumns = cellsTable[0].length;
        int numOfRows = cellsTable.length;

        int columnWidth = sheet.getLayout().getColumnWidthUnits();
        int rowHeight = sheet.getLayout().getRowHeightUnits();

        int rowIndexWidth = Integer.toString(numOfRows).length();

        System.out.println("Version: " + sheet.getVersion());

        System.out.print(" ".repeat(rowIndexWidth) + " | ");  // Space for row index
        for (int j = 0; j < numOfColumns; j++) {
            String colName = getColumnName(j);
            System.out.print(centerText(colName, columnWidth));
            if (j < numOfColumns - 1) {
                System.out.print(" | ");
            }
        }
        System.out.println();

        for (int i = 0; i < numOfRows; i++) {
            for (int heightIndex = 0; heightIndex < rowHeight; heightIndex++) {
                if (heightIndex == rowHeight / 2) {
                    String rowIndex = Integer.toString(i + 1);
                    System.out.print(centerText(rowIndex, rowIndexWidth) + " | ");  // Row index
                } else {
                    System.out.print(" ".repeat(rowIndexWidth) + " | ");
                }

                for (int j = 0; j < numOfColumns; j++) {
                    Object value = cellsTable[i][j].getEffectiveValue();
                    String valueString = value != null ? objectValueAsString(value) : "";
                    System.out.print(centerText(valueString, columnWidth));
                    if (j < numOfColumns - 1) {
                        System.out.print(" | ");
                    }
                }
                System.out.println();
            }
        }
    }

    private static String centerText(String text, int width) {
        if (text.length() >= width) {
            return text;
        }
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text + " ".repeat(width - text.length() - padding);
    }

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
        System.out.println("Unfortunately, an error occurred");
        if (e instanceof ServiceException || e instanceof ConsoleException) {
            System.out.println(e.toString());
        }
        else {
            System.out.println("Error message: " + e.getMessage());
        }
    }

    public static void printCellInfo(Cell cell) {
        if (cell == null) {
            System.out.println("The cell is null");
            return;
        }


        int version = cell.getVersion();
        List<CellCoordinates> cellsAffectedByMe = cell.getCellsAffectedByMe();
        List<CellCoordinates> cellsAffectingMe = cell.getCellsAffectingMe();

        printBasicCellInfo(cell);
        System.out.println("Last Modified Version: " + version);

        System.out.print("Cells Affected By Me: ");
        if (cellsAffectedByMe.isEmpty()) {
            System.out.println("None");
        } else {
            for (CellCoordinates coord : cellsAffectedByMe) {
                System.out.print(coord + " ");
            }
            System.out.println();
        }

        System.out.print("Cells Affecting Me: ");
        if (cellsAffectingMe.isEmpty()) {
            System.out.println("None");
        } else {
            for (CellCoordinates coord : cellsAffectingMe) {
                System.out.print(coord + " ");
            }
            System.out.println();
        }
    }

    public static void printBasicCellInfo(Cell cell) {
        CellCoordinates coordinates = cell.getCoordinates();
        Object effectiveValue = cell.getEffectiveValue();
        String originalExpression = cell.getOriginalExpression();

        System.out.println("Cell Identifier: " + coordinates);
        System.out.println("Original Expression: " + originalExpression);
        System.out.println("Effective Value: " + effectiveValue);
    }

}
