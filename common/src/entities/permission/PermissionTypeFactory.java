package entities.permission;

public class PermissionTypeFactory {
    public static PermissionType permissionName2PermissionType(String permissionName) {
        PermissionType permissionType = PermissionType.NONE;

        for(PermissionType permission : PermissionType.values()) {
            if (permission.name().equalsIgnoreCase(permissionName)) {
                permissionType = permission;
                break;
            }
        }

        return permissionType;
    }
}
