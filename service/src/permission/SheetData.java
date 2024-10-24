package permission;

import entities.permission.PermissionRequest;
import entities.permission.PermissionType;
import entities.sheet.CoreSheet;

import java.util.*;

public class SheetData {
    private final String name;
    private final Map<String, PermissionType> username2Permission;
    private final List<PermissionRequest> pendingRequests;
    private final List<PermissionRequest> deniedRequests;
    private List<CoreSheet> sheetVersions;

    public SheetData(String sheetName,String username) {
        this.name = sheetName;
        this.sheetVersions = new ArrayList<CoreSheet>();
        this.username2Permission = new HashMap<>();
        this.pendingRequests = new ArrayList<>();
        this.deniedRequests = new ArrayList<>();
        username2Permission.put(username,PermissionType.OWNER);
    }
    public String getName() {return name;}

    //getters
    public void ApplyPermissionAccessDecision(String username, PermissionType permission,boolean isAccessAllowed) {
        PermissionRequest decidedRequest = null;
        for (PermissionRequest permissionRequest : pendingRequests) {
            if (permissionRequest.getPermissionType() == permission && Objects.equals(permissionRequest.getUsername(), username)) {
                decidedRequest = permissionRequest;
                if (isAccessAllowed) {
                    username2Permission.put(username,permission);
                }
                else {
                    deniedRequests.add(permissionRequest);
                }
                break;
            }
        }
        pendingRequests.remove(decidedRequest);
    }
    public String getOwnerUsername() {
        for (Map.Entry<String, PermissionType> entry : username2Permission.entrySet()) {
            if (entry.getValue().equals(PermissionType.OWNER)) {
                return entry.getKey();
            }
        }
        return null; // Return null if no OWNER permission is found
    }
    public PermissionType getPermission(String username) {return username2Permission.get(username);}
    public List<CoreSheet> getSheetVersions() {return sheetVersions;}
    public List<PermissionRequest> getPendingRequests() {return pendingRequests;}
    public List<PermissionRequest> getDeniedRequests() {return deniedRequests;}
    //setters
    public Map<String, PermissionType> getPermissions() {return username2Permission;}

    public void setSheetVersions(List<CoreSheet> sheetVersions) {this.sheetVersions = sheetVersions;}
    public void addSheetVersion(CoreSheet sheetVersion) {this.sheetVersions.add(sheetVersion);}
    public void addPermissionRequest(PermissionRequest permissionRequest) {this.pendingRequests.add(permissionRequest);}
    }


