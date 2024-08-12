package entities;

import operations.Operation;

public class Sheet implements Cloneable {
    private Cell[][] cellsTable;
    private int numOfRows;
    private int numOfColumns;
    private int version;

    @Override
    public Sheet clone() throws CloneNotSupportedException {
        Sheet cloned = (Sheet) super.clone();
        cloned.cellsTable = new Cell[numOfRows][numOfColumns];
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


    public Cell[][] getCellsTable() {return cellsTable;}
    public int getVersion() {return version;}
    public int getNumOfRows() {return numOfRows;}
    public int getNumOfColumns() {return numOfColumns;}

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

    //Similar to DFS, this is a method that makes all the cells "WHITE"
    public void cleanVisits() {
        for (int i = 0; i < cellsTable.length; i++) {
            for (int j = 0; j < cellsTable[i].length; j++) {
                cellsTable[i][j].setVisited(Cell.Status.WHITE);
            }
        }
    }

    @Override
    public String toString() {
        return "Rows: " + numOfRows + " Columns: " + numOfColumns + " Version: " + version;
    }
}
