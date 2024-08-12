package entities;

public interface Sheet {
    public Cell[][] getCellsTable();
    public int getNumOfRows();
    public int getNumOfColumns();
    public int getVersion();
}
