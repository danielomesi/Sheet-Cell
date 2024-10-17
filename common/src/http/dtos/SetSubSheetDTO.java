package http.dtos;

public class SetSubSheetDTO {
    private final String sheetName;
    private final String topLeftCellID;
    private final String bottomRightCellID;

    public SetSubSheetDTO(String sheetName, String topLeftCellID, String bottomRightCellID) {
        this.sheetName = sheetName;
        this.bottomRightCellID = bottomRightCellID;
        this.topLeftCellID = topLeftCellID;
    }

    public String getSheetName() {return sheetName;}
    public String getBottomRightCellID() {return bottomRightCellID;}
    public String getTopLeftCellID() {return topLeftCellID;}
}

