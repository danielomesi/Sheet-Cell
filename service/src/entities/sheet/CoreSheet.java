package entities.sheet;

import entities.cell.Cell;
import entities.coordinates.Coordinates;
import entities.cell.CoreCell;
import entities.coordinates.CoordinateFactory;
import entities.stl.STLCell;
import entities.stl.STLSheet;
import exceptions.CloneFailureException;
import utils.TopologicalSorter;
import utils.FunctionParser;
import utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.io.*;
import java.util.Map;

public class CoreSheet implements Sheet {
    private final Map<Coordinates, CoreCell> cellsMap;
    private final int numOfRows;
    private final int numOfColumns;
    private int version = 1;
    private int numOfCellsChanged;
    private final Layout layout;
    private final String name;

    public CoreSheet(int numOfRows, int numOfColumns, Layout layout, String name) {
        cellsMap = new HashMap<>();
        this.numOfRows = numOfRows;
        this.numOfColumns = numOfColumns;
        this.layout = layout;
        this.name = name;
    }

    public CoreSheet(STLSheet stlSheet) {
        this.numOfRows = stlSheet.getSTLLayout().getRows();
        this.numOfColumns = stlSheet.getSTLLayout().getColumns();
        this.layout = new Layout(stlSheet.getSTLLayout().getSTLSize().getRowsHeightUnits(),
                stlSheet.getSTLLayout().getSTLSize().getColumnWidthUnits());
        this.name = stlSheet.getName();
        this.numOfCellsChanged = 0;
        cellsMap = new HashMap<>();
        CoreCell coreCell;
        List<STLCell> STLCells = stlSheet.getSTLCells().getSTLCell();
        for (STLCell stlCell : STLCells) {
            int i = stlCell.getRow() - 1;
            int j = CoordinateFactory.convertColumnLettersToIndex(stlCell.getColumn());
            Utils.validateInRange(i, 0, numOfRows);
            Utils.validateInRange(j, 0, numOfColumns);
            Coordinates coordinates = new Coordinates(i, j);
            if (cellsMap.containsKey(coordinates)) {
                coreCell = cellsMap.get(coordinates);
            }
            else {
                coreCell = new CoreCell(this,i,j);
                cellsMap.put(coordinates, coreCell);
            }
            coreCell.setOriginalExpression(stlCell.getSTLOriginalValue());
            FunctionParser.updateDependencies(this, coreCell);
            //this function might create itself a core cell in the sheet so it will update its dependencies
        }
        List<Coordinates> topologicalSort = TopologicalSorter.topologicalSort(this);
        cleanDependencies();
        executeSheet(topologicalSort);
    }

    public CoreSheet cloneWithSerialization() {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            try (ObjectOutputStream out = new ObjectOutputStream(byteOut)) {
                out.writeObject(this);
            }
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            try (ObjectInputStream in = new ObjectInputStream(byteIn)) {
                return (CoreSheet) in.readObject();
            }
        } catch (Exception e) {
            throw new CloneFailureException("Failed to clone sheet: " + this.name);
        }
    }

    public Map<Coordinates, CoreCell> getCoreCellsMap() {return cellsMap;}
    public int getVersion() {return version;}
    public int getNumOfRows() {return numOfRows;}
    public int getNumOfColumns() {return numOfColumns;}
    public void incrementVersion() {version++;}
    @Override
    public Layout getLayout() {return layout;}
    @Override
    public String getName() {return name;}
    public int getNumOfCellsChanged() {return numOfCellsChanged;}
    public void incrementNumOfCellsChanged() {numOfCellsChanged++;}
    public void initializeNumOfCellsChanged() {numOfCellsChanged = 0;}
    @Override
    public Cell getCell(int row, int col) {return CoordinateFactory.getCellObjectFromIndices(this, row, col);}

    private boolean isCellInsideSTLList(int i, int j, List<STLCell> stlCells) {
        for (STLCell stlCell : stlCells) {
            if ( (i == stlCell.getRow()-1) && (j == CoordinateFactory.convertColumnLettersToIndex(stlCell.getColumn())) ) {
                return true;
            }
        }

        return false;
    }

    private void executeSheet(List<Coordinates> topologicalSort) {
        for (Coordinates coordinates : topologicalSort) {
            CoreCell cell = CoordinateFactory.getCellObjectFromCellID(this, coordinates.getCellID());
            String originalExpression = cell.getOriginalExpression();
            if (originalExpression != null) {
                cell.executeCalculationProcedure(originalExpression);
            }
        }
    }

    //Similar to DFS, this is a method that makes all the cells "WHITE"
    public void cleanVisits() {
        cellsMap.forEach((coordinates,cell) -> cell.setVisited(CoreCell.Status.WHITE));
    }

    public void cleanDependencies() {
        cellsMap.forEach((coordinates,cell) -> {
            cell.getCellsAffectedByMe().clear();
            cell.getCellsAffectingMe().clear();
        });
    }

    @Override
    public String toString() {
        return "Rows: " + numOfRows + " Columns: " + numOfColumns + " Version: " + version;
    }
}
