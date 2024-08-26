package entities.sheet;

import entities.coordinates.CellCoordinates;
import entities.cell.CoreCell;
import entities.stl.STLCell;
import entities.stl.STLSheet;
import exceptions.CloneFailureException;
import utils.TopologicalSorter;
import utils.FunctionParser;

import java.util.List;
import java.io.*;

public class CoreSheet implements Sheet {
    private final CoreCell[][] cellsTable;
    private final int numOfRows;
    private final int numOfColumns;
    private int version = 1;
    private int numOfCellsChanged;
    private final Layout layout;
    private final String name;

    public CoreSheet(int numOfRows, int numOfColumns, Layout layout, String name) {
        cellsTable = new CoreCell[numOfRows][numOfColumns];
        this.numOfRows = numOfRows;
        this.numOfColumns = numOfColumns;
        this.layout = layout;
        this.name = name;
        initializeSheet();
    }

    public CoreSheet(STLSheet stlSheet) {
        this.numOfRows = stlSheet.getSTLLayout().getRows();
        this.numOfColumns = stlSheet.getSTLLayout().getColumns();
        this.layout = new Layout(stlSheet.getSTLLayout().getSTLSize().getRowsHeightUnits(),
                stlSheet.getSTLLayout().getSTLSize().getColumnWidthUnits());
        this.name = stlSheet.getName();
        this.numOfCellsChanged = 0;
        cellsTable = new CoreCell[numOfRows][numOfColumns];
        List<STLCell> STLCells = stlSheet.getSTLCells().getSTLCell();
        initializeSheet();
        for (STLCell stlCell : STLCells) {
            int i = stlCell.getRow() - 1;
            int j = CellCoordinates.convertColumnLettersToIndex(stlCell.getColumn());
            FunctionParser.validateInRange(i, 0, numOfRows);
            FunctionParser.validateInRange(j, 0, numOfColumns);
            cellsTable[i][j].setOriginalExpression(stlCell.getSTLOriginalValue());
            FunctionParser.updateDependencies(this, cellsTable[i][j]);
        }
        List<CellCoordinates> topologicalSort = TopologicalSorter.topologicalSort(this);
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

    public CoreCell[][] getCellsTable() {return cellsTable;}
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

    private void initializeSheet() {
        for (int i = 0; i < cellsTable.length; i++) {
            for (int j = 0; j < cellsTable[i].length; j++) {
                CoreCell cell = new CoreCell(this, i,j);
                cellsTable[i][j] = cell;
            }
        }
    }


    private boolean isCellInsideSTLList(int i, int j, List<STLCell> stlCells) {
        for (STLCell stlCell : stlCells) {
            if ( (i == stlCell.getRow()-1) && (j == CellCoordinates.convertColumnLettersToIndex(stlCell.getColumn())) ) {
                return true;
            }
        }

        return false;
    }

    private void executeSheet(List<CellCoordinates> topologicalSort) {
        for (CellCoordinates coordinates : topologicalSort) {
            CoreCell cell = CellCoordinates.getCellObjectFromCellID(this, coordinates.getCellID());
            String originalExpression = cell.getOriginalExpression();
            if (originalExpression != null) {
                cell.executeCalculationProcedure(originalExpression);
            }
        }
    }

    //Similar to DFS, this is a method that makes all the cells "WHITE"
    public void cleanVisits() {
        for (int i = 0; i < cellsTable.length; i++) {
            for (int j = 0; j < cellsTable[i].length; j++) {
                cellsTable[i][j].setVisited(CoreCell.Status.WHITE);
            }
        }
    }

    public void cleanDependencies() {
        for (int i = 0; i < cellsTable.length; i++) {
            for (int j = 0; j < cellsTable[i].length; j++) {
                cellsTable[i][j].getCellsAffectedByMe().clear();
                cellsTable[i][j].getCellsAffectingMe().clear();
            }
        }
    }

    @Override
    public String toString() {
        return "Rows: " + numOfRows + " Columns: " + numOfColumns + " Version: " + version;
    }
}
