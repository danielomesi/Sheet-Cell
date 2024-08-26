package entities.cell;

import entities.coordinates.CellCoordinates;
import entities.coordinates.CoordinateFactory;
import entities.sheet.CoreSheet;
import exceptions.CircleReferenceException;
import operations.core.Operation;
import utils.FunctionParser;

import java.util.HashSet;
import java.util.Set;

import static utils.FunctionParser.parseArgument;
import static utils.FunctionParser.parseFunctionExpression;

public class CoreCell implements Cell {
    public enum Status {
    WHITE, GREY, BLACK
    }

    private CoreSheet sheet;
    private final CellCoordinates coordinates;
    private int version;
    private Object effectiveValue;
    private String originalExpression;
    private Operation operation;
    private final Set<CellCoordinates> cellsAffectedByMe = new HashSet<>(0);
    private final Set<CellCoordinates> cellsAffectingMe = new HashSet<>(0);
    private Status visitColor;

    public CoreCell(CoreSheet sheet, int row, int col)
    {
        this.sheet = sheet;
        this.version = sheet.getVersion();
        FunctionParser.validateInRange(row, 0, sheet.getNumOfRows());
        FunctionParser.validateInRange(col, 0, sheet.getNumOfColumns());
        this.coordinates = new CellCoordinates(row, col);
        this.visitColor = Status.WHITE;
    }

    public CoreSheet getSheet() {return sheet;}
    public void setSheet(CoreSheet sheet) {this.sheet = sheet;}
    @Override
    public CellCoordinates getCoordinates() {return coordinates;}
    @Override
    public int getVersion() {return version;}
    @Override
    public String getOriginalExpression() {return originalExpression;}
    public void setOriginalExpression(String originalExpression) {this.originalExpression = originalExpression;}
    public Operation getOperation() {return operation;}
    @Override
    public Set<CellCoordinates> getCellsAffectedByMe() {return cellsAffectedByMe;}
    @Override
    public Set<CellCoordinates> getCellsAffectingMe() {return cellsAffectingMe;}
    public void setVisited(Status isVisited) {this.visitColor = isVisited;}
    @Override
    public Object getEffectiveValue() {return effectiveValue;};

    public void executeCalculationProcedure(String expression) {
        resetListOfCellsThatAffectMe();
        expression = expression.trim();
        originalExpression = expression;
        if (expression.startsWith("{") && expression.endsWith("}")) {
            operation = parseFunctionExpression(sheet,coordinates, expression);

        } else
        {
            operation = null;
            effectiveValue = parseArgument(sheet, coordinates, expression);
        }
        update();
        sheet.cleanVisits();
    }

    private void notifyAffectedCells()
    {
        visitColor = Status.GREY;
        for (CellCoordinates cellCoordinates : cellsAffectedByMe)
        {
            CoordinateFactory.getCellObjectFromIndices(sheet, cellCoordinates.getRow(), cellCoordinates.getCol()).update();
        }
        visitColor = Status.BLACK;
    }

    private void update()
    {
        if (visitColor != Status.GREY)
        {
            sheet.incrementNumOfCellsChanged();
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
            CoordinateFactory.getCellObjectFromIndices(sheet, cellCoordinates.getRow(), cellCoordinates.getCol()).cellsAffectedByMe.remove(this.coordinates);
        }
        cellsAffectingMe.clear();
    }

    @Override
    public String toString() {
        return "["+ CoordinateFactory.getCellIDFromIndices(coordinates.getRow(), coordinates.getCol()) +"]: "+effectiveValue.toString();
    }
}
