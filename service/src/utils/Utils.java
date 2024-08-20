package utils;

import entities.cell.CoreCell;
import entities.cell.CellCoordinates;
import entities.sheet.CoreSheet;
import exceptions.CellOutOfBoundsException;
import exceptions.InvalidArgumentException;
import operations.Operation;
import operations.OperationInfo;

import java.util.ArrayList;
import java.util.List;

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

    public static int convertColumnLettersToIndex(String letters) {
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

        Integer rowNumber = rowIndex + 1;
        return  columnLetters + rowNumber.toString();
    }

    public static Operation parseFunctionExpression(CoreSheet sheet, CellCoordinates coordinates, String originalExpression) {
        originalExpression = originalExpression.trim();
        if (originalExpression.startsWith("{") && originalExpression.endsWith("}")) {
            originalExpression = originalExpression.substring(1, originalExpression.length() - 1).trim();
        }

        int firstCommaIndex = originalExpression.indexOf(',');
        if (firstCommaIndex == -1) {
            throw new InvalidArgumentException("Invalid function format",coordinates, originalExpression);
        }
        String functionName = originalExpression.substring(0, firstCommaIndex).trim();
        OperationInfo operationInfo = Operation.getOperationsMap().get(functionName);
        if (operationInfo == null) {
            throw new InvalidArgumentException("Function name is not recognized",coordinates, functionName);
        }

        String argumentsString = originalExpression.substring(firstCommaIndex + 1);
        List<Object> arguments = parseArguments(sheet, coordinates, argumentsString);
        if (arguments.size() != operationInfo.getNumOfArgsRequired()){
            throw new InvalidArgumentException("Number of arguments given in function "
                    + functionName + " does not match expected number of arguments", coordinates);
        }

        return Operation.createFunctionHandler(sheet,coordinates, functionName, arguments);
    }

    private static List<Object> parseArguments(CoreSheet sheet, CellCoordinates coordinates, String input) {
        List<Object> arguments = new ArrayList<>();
        StringBuilder currentArg = new StringBuilder();
        int braceCount = 0;

        for (char c : input.toCharArray()) {
            if (c == ',' && braceCount == 0) {
                arguments.add(parseArgument(sheet, coordinates, currentArg.toString()));
                currentArg = new StringBuilder();
            } else {
                if (c == '{') braceCount++;
                if (c == '}') braceCount--;
                currentArg.append(c);
            }
        }

        if (!currentArg.isEmpty()) {
            arguments.add(parseArgument(sheet,coordinates,  currentArg.toString()));
        }

        return arguments;
    }

    public static Object parseArgument(CoreSheet sheet, CellCoordinates coordinates, String arg) {
        if (arg.startsWith("{") && arg.endsWith("}")) {
            return parseFunctionExpression(sheet, coordinates, arg);
        }
        else if (arg.matches("-?\\d+(\\.\\d+)?")) {
            return Double.parseDouble(arg);
        } else if (arg.equalsIgnoreCase("true") || arg.equalsIgnoreCase("false")) {
            return Boolean.parseBoolean(arg);
        }
        else {
            return arg;
        }
    }

    public static void updateDependencies(CoreSheet sheet, CoreCell coreCell) {
        String originalExpression = coreCell.getOriginalExpression();

        if (originalExpression == null || originalExpression.isEmpty()) {
            return; // No expression to process
        }

        // A simple function to parse the expression and identify dependencies
        if (originalExpression.startsWith("{") && originalExpression.endsWith("}")) {
            // Remove outer braces and trim spaces
            originalExpression = originalExpression.substring(1, originalExpression.length() - 1).trim();

            // Check if the expression starts with "REF,"
            if (originalExpression.startsWith("REF,")) {
                // Extract the referenced cell's ID (skip "REF," which is 4 characters + 1 for the comma)
                String referencedCellID = originalExpression.substring(4).trim();

                // Get the coordinates of the referenced cell
                CoreCell referencedCell = Utils.getCellObjectFromCellID(sheet, referencedCellID);

                if (referencedCell != null) {
                    // Add current cell to referenced cell's affected-by-me list
                    referencedCell.getCellsAffectedByMe().add(coreCell.getCoordinates());

                    // Add referenced cell to current cell's affecting-me list
                    coreCell.getCellsAffectingMe().add(referencedCell.getCoordinates());
                }
            } else {
                // Handle possible nested functions
                // Split the expression by commas to handle possible nested functions
                String[] parts = splitByCommas(originalExpression);

                for (String part : parts) {
                    part = part.trim();
                    if (part.startsWith("{") && part.endsWith("}")) {
                        // Process each nested expression
                        updateDependencies(sheet, coreCell, part);
                    }
                }
            }
        }
    }

    private static void updateDependencies(CoreSheet sheet, CoreCell coreCell, String expression) {
        // Parse nested {REF,****} references
        expression = expression.substring(1, expression.length() - 1).trim();

        if (expression.startsWith("REF,")) {
            String referencedCellID = expression.substring(4).trim();
            CoreCell referencedCell = Utils.getCellObjectFromCellID(sheet, referencedCellID);

            if (referencedCell != null) {
                referencedCell.getCellsAffectedByMe().add(coreCell.getCoordinates());
                coreCell.getCellsAffectingMe().add(referencedCell.getCoordinates());
            }
        }
    }

    private static String[] splitByCommas(String expression) {
        // This method splits the expression by commas outside of curly braces
        List<String> parts = new ArrayList<>();
        StringBuilder currentPart = new StringBuilder();
        int braceCount = 0;
        boolean inString = false;

        for (char c : expression.toCharArray()) {
            if (c == ',' && braceCount == 0 && !inString) {
                parts.add(currentPart.toString().trim());
                currentPart = new StringBuilder();
            } else {
                if (c == '{') braceCount++;
                if (c == '}') braceCount--;
                if (c == '"') inString = !inString;
                currentPart.append(c);
            }
        }

        if (currentPart.length() > 0) {
            parts.add(currentPart.toString().trim());
        }

        return parts.toArray(new String[0]);
    }
}
