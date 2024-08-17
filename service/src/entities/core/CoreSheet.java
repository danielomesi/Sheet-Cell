package entities.core;

import entities.Cell;
import entities.CellCoordinates;
import entities.Sheet;
import entities.stl.STLCell;
import entities.stl.STLSheet;
import operations.Operation;
import utils.TopologicalSorter;
import utils.Utils;

import java.util.List;

public class CoreSheet implements Sheet,Cloneable {
    private CoreCell[][] cellsTable;
    private final int numOfRows;
    private final int numOfColumns;
    private int version = 1;

    public CoreSheet(int numOfRows, int numOfColumns) {
        cellsTable = new CoreCell[numOfRows][numOfColumns];
        this.numOfRows = numOfRows;
        this.numOfColumns = numOfColumns;
        initializeSheet();
    }

    public CoreSheet(STLSheet stlSheet) {
        this.numOfRows = stlSheet.getSTLLayout().getRows();
        this.numOfColumns = stlSheet.getSTLLayout().getColumns();
        cellsTable = new CoreCell[numOfRows][numOfColumns];
        List<STLCell> STLCells = stlSheet.getSTLCells().getSTLCell();
        initializeSheet();
        for (STLCell stlCell : STLCells) {
            int i = stlCell.getRow() - 1;
            int j = Utils.convertColumnLettersToIndex(stlCell.getColumn());
            cellsTable[i][j].setOriginalExpression(stlCell.getSTLOriginalValue());
            Utils.updateDependencies(this, cellsTable[i][j]);
        }
        List<CellCoordinates> topologicalSort = TopologicalSorter.topologicalSort(this);
        cleanDependencies();
        executeSheet(topologicalSort);
    }

    @Override
    public CoreSheet clone() throws CloneNotSupportedException {
        CoreSheet cloned = (CoreSheet) super.clone();
        cloned.cellsTable = new CoreCell[numOfRows][numOfColumns];
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfColumns; j++) {
                cloned.cellsTable[i][j] = cellsTable[i][j].clone();
                cloned.cellsTable[i][j].setSheet(cloned);
                Operation op = this.cellsTable[i][j].getOperation();
                if (op != null) {
                    op.setSheet(cloned);
                }
            }
        }

        return cloned;
    }

    public CoreCell[][] getCellsTable() {return cellsTable;}
    //public CellInterface[][] getCellsTable() {return cellsTable;}
    public int getVersion() {return version;}
    public int getNumOfRows() {return numOfRows;}
    public int getNumOfColumns() {return numOfColumns;}
    public void incrementVersion() {version++;}

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
            if ( (i == stlCell.getRow()-1) && (j == Utils.convertColumnLettersToIndex(stlCell.getColumn())) ) {
                return true;
            }
        }

        return false;
    }

    private void executeSheet(List<CellCoordinates> topologicalSort) {
        for (CellCoordinates coordinates : topologicalSort) {
            CoreCell cell = Utils.getCellObjectFromCellID(this, coordinates.getCellID());
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
