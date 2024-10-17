package http.dtos;

public class EffectiveValuesInSpecificColRequestDTO {
    private final String sheetName;
    private final String selectedColName;
    private final String fromCellID;
    private final String toCellID;

    public EffectiveValuesInSpecificColRequestDTO(String sheetName, String selectedColName, String fromCellID, String toCellID) {
        this.sheetName = sheetName;
        this.selectedColName = selectedColName;
        this.fromCellID = fromCellID;
        this.toCellID = toCellID;
    }

    public String getSheetName() {return sheetName;}
    public String getSelectedColName() {return selectedColName;}
    public String getFromCellID() {return fromCellID;}
    public String getToCellID() {return toCellID;}
}
