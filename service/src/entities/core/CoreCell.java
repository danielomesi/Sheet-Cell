package entities.core;

import entities.CellCoordinates;
import entities.Cell;
import exceptions.CircleReferenceException;
import exceptions.InvalidArgumentException;
import operations.Operation;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class CoreCell implements Cell,Cloneable {
    public enum Status {
    WHITE, GREY, BLACK
    }

    private CoreSheet sheet;
    private final CellCoordinates coordinates;
    private int version;
    private Object effectiveValue;
    private String originalExpression;
    private Operation operation;
    private List<CellCoordinates> cellsAffectedByMe = new ArrayList<>(0);
    private List<CellCoordinates> cellsAffectingMe = new ArrayList<>(0);
    private Status visitColor;

    public CoreCell(CoreSheet sheet, int row, int col)
    {
        this.sheet = sheet;
        this.version = sheet.getVersion();
        this.coordinates = new CellCoordinates(row, col);
        this.visitColor = Status.WHITE;
    }

    @Override
    public CoreCell clone() throws CloneNotSupportedException {
        CoreCell cloned = (CoreCell) super.clone();
        cloned.operation = this.operation;
        cloned.cellsAffectingMe = new ArrayList<>(cellsAffectingMe);
        cloned.cellsAffectedByMe = new ArrayList<>(cellsAffectedByMe);

        return cloned;
    }

    public CoreSheet getSheet() {return sheet;}
    public void setSheet(CoreSheet sheet) {this.sheet = sheet;}
    @Override
    public CellCoordinates getCoordinates() {return coordinates;}
    @Override
    public int getVersion() {return version;}
    @Override
    public String getOriginalExpression() {return originalExpression;}
    public Operation getOperation() {return operation;}
    @Override
    public List<CellCoordinates> getCellsAffectedByMe() {return cellsAffectedByMe;}
    @Override
    public List<CellCoordinates> getCellsAffectingMe() {return cellsAffectingMe;}
    public void setVisited(Status isVisited) {this.visitColor = isVisited;}
    @Override
    public Object getEffectiveValue() {return effectiveValue;};

    public void executeCalculationProcedure(String expression) {
        resetListOfCellsThatAffectMe();
        expression = expression.trim();
        originalExpression = expression;
        if (expression.startsWith("{") && expression.endsWith("}")) {
            operation = parseFunctionExpression(expression);

        } else
        {
            operation = null;
            effectiveValue = parseArgument(expression);
        }
        update();
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
            if (operation!=null) {
                this.effectiveValue = operation.execute();
            }
            version = sheet.getVersion();
            notifyAffectedCells();
        }
        else {
            throw new CircleReferenceException("Circular reference identified", coordinates);
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
            throw new InvalidArgumentException("Invalid function format",coordinates, originalExpression);
        }
        String functionName = originalExpression.substring(0, firstCommaIndex).trim();

        String argumentsString = originalExpression.substring(firstCommaIndex + 1).trim();
        List<Object> arguments = parseArguments(argumentsString);
        if (arguments.size() != Operation.getOperationsMap().get(functionName).getNumOfArgsRequired()){
            throw new InvalidArgumentException("Number of arguments given in function "
                    + functionName + " does not match expected number of arguments", coordinates);
        }

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
        else if (arg.matches("-?\\d+(\\.\\d+)?")) {
            return Double.parseDouble(arg);
        } else if (arg.equalsIgnoreCase("true") || arg.equalsIgnoreCase("false")) {
            return Boolean.parseBoolean(arg);
        }
        else {
            return arg;
        }
    }

    @Override
    public String toString() {
        return "["+ Utils.getCellIDFromIndices(coordinates.getRow(), coordinates.getCol()) +"]: "+effectiveValue.toString();
    }
}
