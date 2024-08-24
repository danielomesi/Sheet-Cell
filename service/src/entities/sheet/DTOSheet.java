package entities.sheet;

import entities.cell.DTOCell;

public class DTOSheet implements Sheet {
    private DTOCell[][] cellsTable;
    private final int numOfRows;
    private final int numOfColumns;
    private final int version;
    private final int numOfCellsChanged;
    private final Layout layout;
    private final String name;

    public DTOSheet(CoreSheet coreSheet) {
        this.numOfRows = coreSheet.getNumOfRows();
        this.numOfColumns = coreSheet.getNumOfColumns();
        this.version = coreSheet.getVersion();
        this.layout = coreSheet.getLayout();
        this.name = coreSheet.getName();
        this.numOfCellsChanged = coreSheet.getNumOfCellsChanged();
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
    @Override
    public Layout getLayout() {return layout;}
    @Override
    public String getName() {return name;}
    public int getNumOfCellsChanged() {return this.numOfCellsChanged;}
}
