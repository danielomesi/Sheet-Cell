package entities.core;

import entities.CellCoordinates;
import entities.Cell;
import entities.stl.STLCell;
import exceptions.CircleReferenceException;
import exceptions.InvalidArgumentException;
import operations.Operation;
import utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static utils.Utils.parseArgument;
import static utils.Utils.parseFunctionExpression;

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
    public void setOriginalExpression(String originalExpression) {this.originalExpression = originalExpression;}
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

    @Override
    public String toString() {
        return "["+ Utils.getCellIDFromIndices(coordinates.getRow(), coordinates.getCol()) +"]: "+effectiveValue.toString();
    }
}
