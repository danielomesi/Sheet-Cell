package entities.dto;

import entities.Sheet;
import entities.core.CoreSheet;

public class DTOSheet implements Sheet {
    private DTOCell[][] cellsTable;
    private final int numOfRows;
    private final int numOfColumns;
    private final int version;

    public DTOSheet(CoreSheet coreSheet) {
        this.numOfRows = coreSheet.getNumOfRows();
        this.numOfColumns = coreSheet.getNumOfColumns();
        this.version = coreSheet.getVersion();

        if (coreSheet.getCellsTable() != null) {
            this.cellsTable = new DTOCell[numOfRows][numOfColumns];
            for (int i = 0; i < coreSheet.getCellsTable().length; i++) {
                for (int j = 0; j < coreSheet.getCellsTable()[i].length; j++) {
                    this.cellsTable[i][j] = new DTOCell(coreSheet.getCellsTable()[i][j]);
                }
            }
        }
    }

    @Override
    public DTOCell[][] getCellsTable() {return this.cellsTable;}
    public int getNumOfRows() {return this.numOfRows;}
    public int getNumOfColumns() {return this.numOfColumns;}
    public int getVersion() {return this.version;}
}
