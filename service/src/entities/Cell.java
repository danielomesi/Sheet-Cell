package entities;

import operations.Operation;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class Cell implements Cloneable {
    public enum Status {
    WHITE, GREY, BLACK
    }

    private Sheet sheet;
    private int row;
    private int col;
    private int version;
    private Object effectiveValue;
    private String originalExpression;
    private Operation operation;
    private List<CellCoordinates> cellsAffectedByMe = new ArrayList<>(0);
    private List<CellCoordinates> cellsAffectingMe = new ArrayList<>(0);
    private Status visitColor;

    public Cell(Sheet sheet, int row, int col)
    {
        this.sheet = sheet;
        this.row = row;
        this.col = col;
        this.visitColor = Status.WHITE;
    }

    @Override
    public Cell clone() throws CloneNotSupportedException {
        Cell cloned = (Cell) super.clone();
        cloned.operation = this.operation;
        cloned.cellsAffectingMe = new ArrayList<>(cellsAffectingMe);
        cloned.cellsAffectedByMe = new ArrayList<>(cellsAffectedByMe);

        return cloned;
    }

    public Sheet getSheet() {return sheet;}
    public void setSheet(Sheet sheet) {this.sheet = sheet;}
    public int getRow() {return row;}
    public int getCol() {return col;}
    public Object getEffectiveValue() {return effectiveValue;}
    public String getOriginalExpression() {return originalExpression;}
    public Operation getOperation() {return operation;}
    public List<CellCoordinates> getCellsAffectedByMe() {return cellsAffectedByMe;}
    public List<CellCoordinates> getCellsAffectingMe() {return cellsAffectingMe;}
    public void setVisited(Status isVisited) {this.visitColor = Status.WHITE;}

    public void setOriginalAndEffectiveValues(String expression) {
        resetListOfCellsThatAffectMe();
        expression = expression.trim();
        if (expression.startsWith("{") && expression.endsWith("}")) {
            operation = parseFunctionExpression(expression);
            effectiveValue = operation.execute();

        } else
        {
            effectiveValue = parseArgument(expression);
        }
        originalExpression = expression;
        notifyAffectedCells();
        sheet.cleanVisits();
    }

    private void notifyAffectedCells()
    {
        visitColor = Status.GREY;
        for (CellCoordinates cellCoordinates : cellsAffectedByMe)
        {
            Utils.getCellObjectFromIndices(sheet, cellCoordinates.getRow(), cellCoordinates.getCol()).update();
        }
        visitColor = Status.BLACK;
    }

    private void update()
    {
        if (visitColor != Status.GREY)
        {
            this.effectiveValue = operation.execute();
            notifyAffectedCells();
        }
        else {
            throw new IllegalStateException("Circular reference identified");
        }

    }

    private void resetListOfCellsThatAffectMe()
    {
        for(CellCoordinates cellCoordinates : cellsAffectingMe)
        {
            Utils.getCellObjectFromIndices(sheet, cellCoordinates.getRow(), cellCoordinates.getCol()).cellsAffectedByMe.remove(this);
        }
        cellsAffectingMe.clear();
    }

    public Operation parseFunctionExpression(String originalExpression) {
        originalExpression = originalExpression.trim();
        if (originalExpression.startsWith("{") && originalExpression.endsWith("}")) {
            originalExpression = originalExpression.substring(1, originalExpression.length() - 1).trim();
        }

        int firstCommaIndex = originalExpression.indexOf(',');
        if (firstCommaIndex == -1) {
            throw new IllegalArgumentException("Invalid function format");
        }
        String functionName = originalExpression.substring(0, firstCommaIndex).trim();

        String argumentsString = originalExpression.substring(firstCommaIndex + 1).trim();
        List<Object> arguments = parseArguments(argumentsString);

        return Operation.createFunctionHandler(sheet, functionName, arguments);
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

        if (!currentArg.isEmpty()) {
            arguments.add(parseArgument(currentArg.toString().trim()));
        }

        return arguments;
    }

    public Object parseArgument(String arg) {
        arg = arg.trim();
        if (arg.startsWith("{") && arg.endsWith("}")) {
            return parseFunctionExpression(arg);
        }
        else if (arg.matches("[A-Z]+\\d+")) {
            Cell cell = Utils.getCellObjectFromCellName(sheet, arg);
            CellCoordinates cellCoordinates = new CellCoordinates(cell);
            this.cellsAffectingMe.add(cellCoordinates);
            cell.cellsAffectedByMe.add(new CellCoordinates(this));
            return cellCoordinates;
        } else if (arg.matches("-?\\d+(\\.\\d+)?")) {
            if (arg.contains(".")) {
                return Double.parseDouble(arg);
            } else {
                return Integer.parseInt(arg);
            }
        } else if (arg.equalsIgnoreCase("true") || arg.equalsIgnoreCase("false")) {
            return Boolean.parseBoolean(arg);
        }
        else {
            return arg;
        }
//        } else if (arg.startsWith("\"") && arg.endsWith("\"")) {
//            return arg.substring(1, arg.length() - 1);
//        } else {
//            throw new IllegalArgumentException("Unknown argument format");
//        }
    }

    @Override
    public String toString() {
        return "["+ CellCoordinates.getCellNameFromIndices(row,col) +"]: "+effectiveValue.toString();
    }
}
