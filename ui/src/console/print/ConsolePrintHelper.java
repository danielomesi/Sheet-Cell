package console.print;

import console.exceptions.ConsoleException;
import entities.cell.Cell;
import entities.coordinates.Coordinates;
import entities.sheet.Sheet;
import entities.types.undefined.Undefined;
import entities.types.undefined.UndefinedBoolean;
import entities.types.undefined.UndefinedNumber;
import entities.types.undefined.UndefinedString;
import exceptions.ServiceException;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;

public class ConsolePrintHelper {

    public static void printSheet(Sheet sheet) {
        int numOfColumns = sheet.getNumOfColumns();
        int numOfRows = sheet.getNumOfRows();

        int columnWidth = sheet.getLayout().getColumnWidthUnits();
        int rowHeight = sheet.getLayout().getRowHeightUnits();
        int rowIndexWidth = Math.max(Integer.toString(numOfRows).length(), 2);

        double rowPadding = ((double)rowHeight - (double)1) / 2;
        int leftPadding = (int) Math.floor(rowPadding);
        int rightPadding = (int) Math.ceil(rowPadding);

        // Print version
        System.out.println("Sheet Name: " + sheet.getName());
        System.out.println("Version: " + sheet.getVersion());

        // Print column headers
        System.out.print(" ".repeat(rowIndexWidth));
        for (int j = 0; j < numOfColumns; j++) {
            String colName = getColumnName(j);
            System.out.print(centerText(colName, columnWidth));
            if (j < numOfColumns - 1) {
                System.out.print("|");
            }
        }
        System.out.println();



        for (int i = 0; i < numOfRows; i++) {
            moveDown(leftPadding);
            String rowIndex = String.format("%0" + rowIndexWidth + "d", i + 1);
            System.out.print(rowIndex);

            System.out.print(" ".repeat(rowIndexWidth-rowIndex.length()));

        // Print cell values
            for (int j = 0; j < numOfColumns; j++) {
                Cell cell = sheet.getCell(i,j);
                Object value =  cell != null ? cell.getEffectiveValue() : null;
                String valueString = value != null ? valueToString(value) : "";
                System.out.print(centerText(valueString, columnWidth+1));
                //added 1 to prepare the next text to be inserted after a |
            }
            System.out.println();
            moveDown(rightPadding);
        }
    }

    public static void moveDown(int lines) {
        for (int i = 0; i < lines; i++) {
            System.out.println();
        }
    }


    private static String centerText(String text, int width) {
        if (text.length() > width) {
            return text.substring(0, width-1);
        }
        double padding = ((double)width - (double)text.length())/2;
        int leftPadding = (int) Math.floor(padding);
        int rightPadding = (int) Math.ceil(padding);
        return " ".repeat(leftPadding) + text + " ".repeat(rightPadding);
    }

    private static String getColumnName(int index) {
        StringBuilder columnName = new StringBuilder();
        while (index >= 0) {
            columnName.insert(0, (char) ('A' + index % 26));
            index = index / 26 - 1;
        }
        return columnName.toString();
    }

    private static String valueToString(Object obj) {
        String result;

        if (obj instanceof Number number) {
            if (number.doubleValue() % 1 == 0) {
                result = String.format("%,d", number.longValue());
            } else {
                result = String.format("%,.2f", number.doubleValue());
            }
        }
        else if (obj instanceof Boolean bool) {
            result = bool.toString().toUpperCase();
        }
        else if (obj instanceof Undefined) {
            if (obj instanceof UndefinedNumber){
                result = "NaN";
            }
            else if (obj instanceof UndefinedString){
                result = "!UNDEFINED!";
            }
            else if (obj instanceof UndefinedBoolean) {
                result = "UNKNOWN";
            }
            else {
                result = "Unknown Type of " + obj.getClass().getSimpleName();
            }
        }
        else {
            result = obj.toString();
        }

        return result;
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
        printBasicCellInfo(cell);
        if (cell == null) {
            return;
        }
        int version = cell.getVersion();
        Set<Coordinates> cellsAffectedByMe = cell.getCellsAffectedByMe();
        Set<Coordinates> cellsAffectingMe = cell.getCellsAffectingMe();

        System.out.println("Last Modified Version: " + version);

        System.out.print("Cells Affected By Me: ");
        if (cellsAffectedByMe.isEmpty()) {
            System.out.println("None");
        } else {
            for (Coordinates coordinates : cellsAffectedByMe) {
                System.out.print(coordinates + " ");
            }
            System.out.println();
        }

        System.out.print("Cells Affecting Me: ");
        if (cellsAffectingMe.isEmpty()) {
            System.out.println("None");
        } else {
            for (Coordinates coordinates : cellsAffectingMe) {
                System.out.print(coordinates + " ");
            }
            System.out.println();
        }
    }

    public static void printBasicCellInfo(Cell cell) {
        if (cell != null) {
            Coordinates coordinates = cell.getCoordinates();
            Object effectiveValue = cell.getEffectiveValue();
            String originalExpression = cell.getOriginalExpression();
            System.out.println("Cell Identifier: " + coordinates);
            System.out.println("Original Expression: " + originalExpression);
            System.out.println("Effective Value: " + effectiveValue);
        }
        else {
            System.out.println("No info on this cell, since it wasn't created/referenced yet");
        }
    }

    public static void printSheetVersionsInfo(List<Sheet> sheetList) {
        if (sheetList == null || sheetList.isEmpty()) {
            System.out.println("No sheets available.");
            return;
        }

        System.out.printf("%-10s | %-20s\n", "Version", "Cells Changed");
        System.out.println("-----------------------------");

        for (int i = 0; i < sheetList.size(); i++) {
            Sheet sheet = sheetList.get(i);
            int version = i + 1; // Assuming version is based on index, adjust as needed
            int cellsChanged = sheet.getNumOfCellsChanged();

            System.out.printf("%-10d | %-20d\n", version, cellsChanged);
        }
    }

}
