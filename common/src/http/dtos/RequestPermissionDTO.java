package http.dtos;

import entities.permission.PermissionType;

public class RequestPermissionDTO {
    private final String sheetName;
    private final PermissionType permissionType;

    public RequestPermissionDTO(String sheetName, PermissionType permissionType) {
        this.sheetName = sheetName;
        this.permissionType = permissionType;
    }

    //getters
    public String getSheetName() {return sheetName;}
    public PermissionType getPermissionType() {return permissionType;}
}
