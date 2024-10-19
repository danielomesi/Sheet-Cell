package permission;

import entities.permission.PermissionType;
import entities.sheet.CoreSheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SheetData {
    private final String name;
    private final Map<String, PermissionType> username2Permission;
    private List<CoreSheet> sheetVersions;

    public SheetData(String sheetName,String username) {
        this.name = sheetName;
        this.sheetVersions = new ArrayList<CoreSheet>();
        this.username2Permission = new HashMap<>();
        username2Permission.put(username,PermissionType.OWNER);
    }
    public String getName() {return name;}

    //getters
    public void setPermission(String username, PermissionType permission) {username2Permission.put(username,permission);}
    public String getOwnerUsername() {
        for (Map.Entry<String, PermissionType> entry : username2Permission.entrySet()) {
            if (entry.getValue().equals(PermissionType.OWNER)) {
                return entry.getKey();
            }
        }
        return null; // Return null if no OWNER permission is found
    }
    //setters
    public Map<String, PermissionType> getPermissions() {return username2Permission;}
    public PermissionType getPermission(String username) {return username2Permission.get(username);}
    public List<CoreSheet> getSheetVersions() {return sheetVersions;}
    public void setSheetVersions(List<CoreSheet> sheetVersions) {this.sheetVersions = sheetVersions;}
    public void addSheetVersion(CoreSheet sheetVersion) {this.sheetVersions.add(sheetVersion);}


    }


