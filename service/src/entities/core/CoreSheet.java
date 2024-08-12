package entities.core;

import entities.Sheet;
import operations.Operation;

public class CoreSheet implements Sheet,Cloneable {
    private CoreCell[][] cellsTable;
    private final int numOfRows;
    private final int numOfColumns;
    private int version;

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

    public CoreSheet(int numOfRows, int numOfColumns) {
        cellsTable = new CoreCell[numOfRows][numOfColumns];
        this.numOfRows = numOfRows;
        this.numOfColumns = numOfColumns;
        initializeSheet();
    }

    private void initializeSheet() {
        for (int i = 0; i < cellsTable.length; i++) {
            for (int j = 0; j < cellsTable[i].length; j++) {
                CoreCell cell = new CoreCell(this, i,j);
                cellsTable[i][j] = cell;
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

    @Override
    public String toString() {
        return "Rows: " + numOfRows + " Columns: " + numOfColumns + " Version: " + version;
    }
}
