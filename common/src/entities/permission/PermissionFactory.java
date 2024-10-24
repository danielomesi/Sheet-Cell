package entities.permission;

public class PermissionFactory {
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

    public static PermissionStatus permissionName2PermissionStatus(String permissionName) {
       PermissionStatus permissionStatus = PermissionStatus.PENDING;

        for(PermissionStatus permission : PermissionStatus.values()) {
            if (permission.name().equalsIgnoreCase(permissionName)) {
                permissionStatus = permission;
                break;
            }
        }
        return permissionStatus;
    }
}
