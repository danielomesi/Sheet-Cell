package entities.cell;

import entities.coordinates.Coordinates;
import entities.coordinates.CoordinateFactory;
import entities.sheet.CoreSheet;
import exceptions.CircleReferenceException;
import operations.core.Operation;
import utils.Utils;

import java.util.HashSet;
import java.util.Set;

import static utils.FunctionParser.parseArgument;
import static utils.FunctionParser.parseFunctionExpression;

public class CoreCell implements Cell {
    public enum Status {
    WHITE, GREY, BLACK
    }

    private CoreSheet sheet;
    private final Coordinates coordinates;
    private int version;
    private Object effectiveValue;
    private String originalExpression;
    private Operation operation;
    private final Set<Coordinates> cellsAffectedByMe = new HashSet<>(0);
    private final Set<Coordinates> cellsAffectingMe = new HashSet<>(0);
    private Status visitColor;

    public CoreCell(CoreSheet sheet, int row, int col)
    {
        this.sheet = sheet;
        this.version = sheet.getVersion();
        Utils.validateInRange(row, 0, sheet.getNumOfRows());
        Utils.validateInRange(col, 0, sheet.getNumOfColumns());
        this.coordinates = new Coordinates(row, col);
        this.visitColor = Status.WHITE;
    }

    public CoreSheet getSheet() {return sheet;}
    public void setSheet(CoreSheet sheet) {this.sheet = sheet;}
    @Override
    public Coordinates getCoordinates() {return coordinates;}
    @Override
    public int getVersion() {return version;}
    @Override
    public String getOriginalExpression() {return originalExpression;}
    public void setOriginalExpression(String originalExpression) {this.originalExpression = originalExpression;}
    public Operation getOperation() {return operation;}
    @Override
    public Set<Coordinates> getCellsAffectedByMe() {return cellsAffectedByMe;}
    @Override
    public Set<Coordinates> getCellsAffectingMe() {return cellsAffectingMe;}
    public void setVisited(Status isVisited) {this.visitColor = isVisited;}
    @Override
    public Object getEffectiveValue() {return effectiveValue;};

    public void executeCalculationProcedure(String expression) {
        resetListOfCellsThatAffectMe();
        originalExpression = expression;
        if (expression.startsWith("{") && expression.endsWith("}")) {
            operation = parseFunctionExpression(sheet,coordinates, expression);

        } else {
            operation = null;
            effectiveValue = parseArgument(sheet, coordinates, expression);
        }
        update();
        sheet.cleanVisits();
    }

    private void notifyAffectedCells()
    {
        visitColor = Status.GREY;
        for (Coordinates coordinates : cellsAffectedByMe)
        {
            CoordinateFactory.getCellObjectFromIndices(sheet, coordinates.getRow(), coordinates.getCol()).update();
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
        for(Coordinates coordinates : cellsAffectingMe)
        {
            CoordinateFactory.getCellObjectFromIndices(sheet, coordinates.getRow(), coordinates.getCol()).cellsAffectedByMe.remove(this.coordinates);
        }
        cellsAffectingMe.clear();
    }

    @Override
    public String toString() {
        return "["+ CoordinateFactory.getCellIDFromIndices(coordinates.getRow(), coordinates.getCol()) +"]: "+effectiveValue.toString();
    }
}
