package http.dtos;

import java.util.List;

public class SortRequestDTO {
    private final String sheetName;
    private final List<String> colsToSort;
    private final String fromCellID;
    private final String toCellID;
    private final boolean isFirstRowSelected;

    public SortRequestDTO(String sheetName, List<String> colsToSort, String fromCellID, String toCellID, boolean isFirstRowSelected) {
        this.sheetName = sheetName;
        this.colsToSort = colsToSort;
        this.fromCellID = fromCellID;
        this.toCellID = toCellID;
        this.isFirstRowSelected = isFirstRowSelected;
    }
    public String getSheetName() {return sheetName;}
    public List<String> getColsToSort() {return colsToSort;}
    public String getFromCellID() {return fromCellID;}
    public String getToCellID() {return toCellID;}
    public boolean isFirstRowSelected() {return isFirstRowSelected;}
}
