package entities.permission;

public class PermissionRequest {
    private final String username;
    private final PermissionType permissionType;

    public PermissionRequest(String username, PermissionType permissionType) {
        this.username = username;
        this.permissionType = permissionType;
    }

    //getters
    public String getUsername() {return username;}
    public PermissionType getPermissionType() {return permissionType;}
}
