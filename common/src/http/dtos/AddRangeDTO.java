package http.dtos;

public class AddRangeDTO {
    private final String sheetName;
    private final String rangeName;
    private final String bottomRightCellID;
    private final String topLeftCellID;

    public AddRangeDTO(String sheetName, String rangeName, String bottomRightCellID, String topLeftCellID) {
        this.sheetName = sheetName;
        this.rangeName = rangeName;
        this.bottomRightCellID = bottomRightCellID;
        this.topLeftCellID = topLeftCellID;
    }

    public String getSheetName() {return sheetName;}
    public String getRangeName() {return rangeName;}
    public String getBottomRightCellID() {return bottomRightCellID;}
    public String getTopLeftCellID() {return topLeftCellID;}
}
