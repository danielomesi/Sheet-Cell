package http.dtos;

import java.util.List;

public class FilterRequestDTO {
    private final String sheetName;
    private final String selectedColName;
    private final List<Object> selectedEffectiveValues;
    private final String fromCellID;
    private final String toCellID;
    private final boolean isIncludingEmptyCellsInFiltering;

    public FilterRequestDTO(String sheetName, String selectedColName, List<Object> selectedEffectiveValues, String fromCellID, String toCellID, boolean isIncludingEmptyCellsInFiltering) {
        this.sheetName = sheetName;
        this.selectedColName = selectedColName;
        this.selectedEffectiveValues = selectedEffectiveValues;
        this.fromCellID = fromCellID;
        this.toCellID = toCellID;
        this.isIncludingEmptyCellsInFiltering = isIncludingEmptyCellsInFiltering;
    }
    public String getSheetName() {return sheetName;}
    public String getSelectedColName() {return selectedColName;}
    public List<Object> getSelectedEffectiveValues() {return selectedEffectiveValues;}
    public String getFromCellID() {return fromCellID;}
    public String getToCellID() {return toCellID;}
    public boolean isIncludingEmptyCellsInFiltering() {return isIncludingEmptyCellsInFiltering;}
}
