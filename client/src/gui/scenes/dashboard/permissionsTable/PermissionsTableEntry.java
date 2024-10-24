package gui.scenes.dashboard.permissionsTable;

import entities.permission.PermissionStatus;
import entities.permission.PermissionType;
import javafx.beans.property.SimpleStringProperty;

public class PermissionsTableEntry {
    private final SimpleStringProperty username;
    private final SimpleStringProperty permissionType;
    private final SimpleStringProperty permissionStatus;

    public PermissionsTableEntry(String username, String permissionType, String permissionStatus) {
        this.username = new SimpleStringProperty(username);
        this.permissionType = new SimpleStringProperty(permissionType);
        this.permissionStatus = new SimpleStringProperty(permissionStatus);
    }

    //getters
    public String getUsername() {return this.username.get();}
    public String getPermissionType() {return this.permissionType.get();}
    public String getPermissionStatus() {return this.permissionStatus.get();}

    //setters
    public void setPermissionStatus(String status) {this.permissionStatus.set(status);}
    public void setPermissionType(String type) {this.permissionType.set(type);}
    public void setUsername(String username) {this.username.set(username);}
}
