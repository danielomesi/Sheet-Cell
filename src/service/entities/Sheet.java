package service.entities;

import service.operations.Function;

import java.util.ArrayList;
import java.util.List;

public class Sheet {
    private final Cell[][] cellsTable;
    private final int numOfRows;
    private final int numOfColumns;
    private int version;


    public Cell[][] getCellsTable() {return cellsTable;}
    public int getVersion() {return version;}

    public Sheet(int numOfRows, int numOfColumns) {
        cellsTable = new Cell[numOfRows][numOfColumns];
        this.numOfRows = numOfRows;
        this.numOfColumns = numOfColumns;
        initializeSheet();
    }

    private void initializeSheet() {
        for (int i = 0; i < cellsTable.length; i++) {
            for (int j = 0; j < cellsTable[i].length; j++) {
                Cell cell = new Cell(this, i,j);
                cellsTable[i][j] = cell;
            }
        }
    }



    public Cell getCellFromReference(String cellName) {
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

        if (rowIndex < 0 || rowIndex >= numOfRows ||
                colIndex < 0 || colIndex >= numOfColumns) {
            throw new IllegalArgumentException("Cell out of bounds");
        }

        return cellsTable[rowIndex][colIndex];
    }

    private int convertColumnLettersToIndex(String letters) {
        int columnIndex = 0;
        int length = letters.length();
        for (int i = 0; i < length; i++) {
            char letter = letters.charAt(i);
            columnIndex = columnIndex * 26 + (letter - 'A');
        }
        return columnIndex;
    }

    public Function parseFunctionExpression(String originalExpression) {
        originalExpression = originalExpression.trim();
        if (originalExpression.startsWith("{") && originalExpression.endsWith("}")) {
            originalExpression = originalExpression.substring(1, originalExpression.length() - 1).trim();
        }

        int firstCommaIndex = originalExpression.indexOf(',');
        if (firstCommaIndex == -1) {
            throw new IllegalArgumentException("Invalid function format.");
        }
        String functionName = originalExpression.substring(0, firstCommaIndex).trim();

        String argumentsString = originalExpression.substring(firstCommaIndex + 1).trim();
        List<Object> arguments = parseArguments(argumentsString);

        return Function.createFunctionHandler(functionName, arguments);
    }

    private List<Object> parseArguments(String input) {
        List<Object> arguments = new ArrayList<>();
        StringBuilder currentArg = new StringBuilder();
        int braceCount = 0;
        boolean inString = false;

        for (char c : input.toCharArray()) {
            if (c == ',' && braceCount == 0 && !inString) {
                arguments.add(parseArgument(currentArg.toString().trim()));
                currentArg = new StringBuilder();
            } else {
                if (c == '{') braceCount++;
                if (c == '}') braceCount--;
                if (c == '"') inString = !inString;
                currentArg.append(c);
            }
        }

        if (currentArg.length() > 0) {
            arguments.add(parseArgument(currentArg.toString().trim()));
        }

        return arguments;
    }

    private Object parseArgument(String arg) {
        arg = arg.trim();
        if (arg.startsWith("{") && arg.endsWith("}")) {
            return parseFunctionExpression(arg);
        }
        else if (arg.matches("[A-Z]+\\d+")) {
            // Here we assume you have a method to convert cell references to Cell objects
            return getCellFromReference(arg);
        } else if (arg.matches("-?\\d+(\\.\\d+)?")) {
            if (arg.contains(".")) {
                return Double.parseDouble(arg);
            } else {
                return Integer.parseInt(arg);
            }
        } else if (arg.equalsIgnoreCase("true") || arg.equalsIgnoreCase("false")) {
            return Boolean.parseBoolean(arg);
        } else if (arg.startsWith("\"") && arg.endsWith("\"")) {
            return arg.substring(1, arg.length() - 1);
        } else {
            throw new IllegalArgumentException("Unknown argument format");
        }
    }


}
