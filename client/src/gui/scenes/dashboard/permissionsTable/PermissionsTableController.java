package gui.scenes.dashboard.permissionsTable;

import entities.permission.PermissionRequest;
import entities.permission.PermissionStatus;
import entities.permission.PermissionType;
import entities.sheet.SheetMetaData;
import gui.scenes.dashboard.main.DashboardMainController;
import gui.scenes.dashboard.sheetsTable.SheetTableEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Map;

public class PermissionsTableController {

    private DashboardMainController dashboardMainController;

    @FXML
    private Button denyPermissionButton;

    @FXML
    private Button grantPermissionButton;

    @FXML
    private Button refreshPermissionsTableButton;

    @FXML
    private Label statusLabel;

    @FXML
    private TableView<PermissionsTableEntry> tableView;

    @FXML
    private ScrollPane wrapper;

    private final ObservableList<PermissionsTableEntry> tableData = FXCollections.observableArrayList();

    //setters
    public void setDashboardMainController(DashboardMainController dashboardMainController) {this.dashboardMainController = dashboardMainController;}

    //getters
    public DashboardMainController getDashboardMainController() {return this.dashboardMainController;}

    public void initialize() {
        // Bind the columns to the Sheet properties
        TableColumn<PermissionsTableEntry, String> uploaderColumn = (TableColumn<PermissionsTableEntry, String>) tableView.getColumns().get(0);
        uploaderColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<PermissionsTableEntry, String> sheetNameColumn = (TableColumn<PermissionsTableEntry, String>) tableView.getColumns().get(1);
        sheetNameColumn.setCellValueFactory(new PropertyValueFactory<>("permissionType"));

        TableColumn<PermissionsTableEntry, String> sheetSizeColumn = (TableColumn<PermissionsTableEntry, String>) tableView.getColumns().get(2);
        sheetSizeColumn.setCellValueFactory(new PropertyValueFactory<>("permissionStatus"));

        tableView.setItems(tableData);
    }

    public void populatePermissionsTable(SheetMetaData sheetMetaData) {
        if (sheetMetaData == null) return;
        tableData.clear();
        for (PermissionRequest permissionRequest : sheetMetaData.getPendingRequests()) {
            PermissionsTableEntry entry = new PermissionsTableEntry(permissionRequest.getUsername(),
                    permissionRequest.getPermissionType().name(), PermissionStatus.PENDING.name());
            tableData.add(entry);
        }
        for (Map.Entry<String, PermissionType> pair : sheetMetaData.getUsername2Permission().entrySet()) {
            PermissionsTableEntry entry = new PermissionsTableEntry(pair.getKey(),
                    pair.getValue().name(), PermissionStatus.APPROVED.name());
            tableData.add(entry);
        }

    }

    @FXML
    void denyPermissionButtonClicked(ActionEvent event) {

    }

    @FXML
    void grantPermissionButtonClicked(ActionEvent event) {

    }

}

