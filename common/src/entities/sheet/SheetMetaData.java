package entities.sheet;

import entities.permission.PermissionType;

import java.util.Map;

public class SheetMetaData {
    private final String sheetName;
    private final String uploaderName;
    private final int numberOfRows;
    private final int numberOfCols;
    private final Map<String, PermissionType> username2Permission;

    public SheetMetaData(String sheetName, String uploaderName, int numberOfRows, int numberOfCols, Map<String, PermissionType> username2Permission) {
        this.sheetName = sheetName;
        this.uploaderName = uploaderName;
        this.numberOfRows = numberOfRows;
        this.numberOfCols = numberOfCols;
        this.username2Permission = username2Permission;
    }

    public String getSheetName() {return sheetName;}
    public String getUploaderName() {return uploaderName;}
    public int getNumberOfRows() {return numberOfRows;}
    public int getNumberOfCols() {return numberOfCols;}
    public Map<String, PermissionType> getUsername2Permission() {return username2Permission;}
}
