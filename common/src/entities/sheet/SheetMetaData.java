package entities.sheet;

import entities.permission.PermissionRequest;
import entities.permission.PermissionType;

import java.util.List;
import java.util.Map;

public class SheetMetaData {
    private final String sheetName;
    private final String uploaderName;
    private final int numberOfRows;
    private final int numberOfCols;
    private final Map<String, PermissionType> username2Permission;
    private final List<PermissionRequest> pendingRequests;
    private final List<PermissionRequest> deniedRequests;

    public SheetMetaData(String sheetName, String uploaderName, int numberOfRows, int numberOfCols,
                         Map<String, PermissionType> username2Permission, List<PermissionRequest> pendingRequests,
                         List<PermissionRequest> deniedRequests) {
        this.sheetName = sheetName;
        this.uploaderName = uploaderName;
        this.numberOfRows = numberOfRows;
        this.numberOfCols = numberOfCols;
        this.username2Permission = username2Permission;
        this.pendingRequests = pendingRequests;
        this.deniedRequests = deniedRequests;
    }

    public String getSheetName() {return sheetName;}
    public String getUploaderName() {return uploaderName;}
    public int getNumberOfRows() {return numberOfRows;}
    public int getNumberOfCols() {return numberOfCols;}
    public Map<String, PermissionType> getUsername2Permission() {return username2Permission;}
    public List<PermissionRequest> getPendingRequests() {return pendingRequests;}
    public List<PermissionRequest> getDeniedRequests() {return deniedRequests;}
}
