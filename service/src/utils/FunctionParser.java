package utils;

import entities.cell.CoreCell;
import entities.coordinates.Coordinates;
import entities.sheet.CoreSheet;
import entities.types.BooleanWrapper;
import entities.types.NumberWrapper;
import exceptions.InvalidArgumentException;
import operations.core.Operation;
import operations.core.OperationFactory;
import operations.core.OperationInfo;

import java.util.ArrayList;
import java.util.List;

public class FunctionParser {
    public static Operation parseFunctionExpression(CoreSheet sheet, Coordinates coordinates, String originalExpression) {
        originalExpression = originalExpression.substring(1, originalExpression.length() - 1).trim();

        int firstCommaIndex = originalExpression.indexOf(',');
        if (firstCommaIndex == -1) {
            throw new InvalidArgumentException("Invalid function format",coordinates, originalExpression);
        }
        String functionName = originalExpression.substring(0, firstCommaIndex).trim();
        OperationInfo operationInfo = OperationFactory.getOperationsMap().get(functionName.toUpperCase());
        if (operationInfo == null) {
            throw new InvalidArgumentException("Function name is not recognized",coordinates, functionName);
        }

        String argumentsString = originalExpression.substring(firstCommaIndex + 1);
        List<Object> arguments = parseArguments(sheet, coordinates, argumentsString);
        if (arguments.size() != operationInfo.getNumOfArgsRequired()){
            throw new InvalidArgumentException("Number of arguments given in function "
                    + functionName + " does not match expected number of arguments", coordinates);
        }

        return OperationFactory.createSpecificOperationUsingReflection(sheet,coordinates, operationInfo, arguments);
    }

    private static List<Object> parseArguments(CoreSheet sheet, Coordinates coordinates, String input) {
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

    public static Object parseArgument(CoreSheet sheet, Coordinates coordinates, String arg) {
        if (arg.startsWith("{") && arg.endsWith("}")) {
            return parseFunctionExpression(sheet, coordinates, arg);
        }
        else if (arg.trim().matches("-?\\d+(\\.\\d+)?")) {
            return new NumberWrapper(Double.parseDouble(arg));
        } else if (arg.equalsIgnoreCase("true") || arg.equalsIgnoreCase("false")) {
            return new BooleanWrapper(Boolean.parseBoolean(arg));
        }
        else if (arg.isEmpty()) {
            return null;
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

                Coordinates coordinates = new Coordinates(referencedCellID);
                CoreCell referencedCell = sheet.getCoreCellsMap().get(coordinates);
                if (referencedCell == null) {
                    referencedCell = new CoreCell(sheet, coordinates.getRow(), coordinates.getCol());
                    sheet.getCoreCellsMap().put(coordinates, referencedCell);
                }

                // Add current cell to referenced cell's affected-by-me list
                referencedCell.getCellsAffectedByMe().add(coreCell.getCoordinates());

                // Add referenced cell to current cell's affecting-me list
                coreCell.getCellsAffectingMe().add(referencedCell.getCoordinates());

            } else {
                // Handle possible nested functions
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
        expression = expression.substring(1, expression.length() - 1).trim();

        if (expression.startsWith("REF,")) {
            String referencedCellID = expression.substring(4).trim().toUpperCase();
            Coordinates referencedCoordinates = new Coordinates(referencedCellID);
            CoreCell referencedCell;
            if (sheet.getCoreCellsMap().containsKey(referencedCoordinates)) {
                referencedCell = sheet.getCoreCellsMap().get(referencedCoordinates);
            }
            else {
                referencedCell = new CoreCell(sheet,referencedCoordinates.getRow(),referencedCoordinates.getCol());
                sheet.getCoreCellsMap().put(referencedCoordinates,referencedCell);
            }

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
