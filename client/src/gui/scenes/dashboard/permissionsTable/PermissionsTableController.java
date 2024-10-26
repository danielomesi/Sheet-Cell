package gui.scenes.dashboard.permissionsTable;

import entities.permission.PermissionFactory;
import entities.permission.PermissionRequest;
import entities.permission.PermissionStatus;
import entities.permission.PermissionType;
import entities.sheet.SheetMetaData;
import gui.scenes.dashboard.main.DashboardMainController;
import http.HttpClientMessenger;
import http.MyCallBack;
import constants.Constants;
import http.dtos.RequestPermissionDTO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import okhttp3.HttpUrl;

import java.util.Map;

public class PermissionsTableController {

    private DashboardMainController dashboardMainController;

    @FXML
    private Button denyPermissionButton;

    @FXML
    private Button grantPermissionButton;

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

        bindMethodToRunWhenEntryInTableSelected();
    }

    private void bindMethodToRunWhenEntryInTableSelected() {
        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                PermissionStatus permissionStatus =
                        PermissionFactory.permissionName2PermissionStatus(newValue.getPermissionStatus());
                String ownerUsername = dashboardMainController.getSheetsTableController().getCurrentlySelectedSheetMetaData().getUploaderName();
                if (permissionStatus == PermissionStatus.PENDING) {
                    grantPermissionButton.setDisable(!ownerUsername.equals(dashboardMainController.getUsername()));
                    denyPermissionButton.setDisable(!ownerUsername.equals(dashboardMainController.getUsername()));
                }
                else {
                    grantPermissionButton.setDisable(true);
                    denyPermissionButton.setDisable(true);
                }
                }
            });
    }

    public void populatePermissionsTable(SheetMetaData sheetMetaData) {
        PermissionsTableEntry selectedEntry = tableView.getSelectionModel().getSelectedItem();
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
        for (PermissionRequest permissionRequest : sheetMetaData.getDeniedRequests()) {
            PermissionsTableEntry entry = new PermissionsTableEntry(permissionRequest.getUsername(),
                    permissionRequest.getPermissionType().name(), PermissionStatus.REJECTED.name());
            tableData.add(entry);
        }

        if (selectedEntry != null) {
            for (PermissionsTableEntry entry : tableData) {
                if (entry.getUsername().equals(selectedEntry.getUsername()) && entry.getPermissionType().equals(selectedEntry.getPermissionType())) {
                    tableView.getSelectionModel().select(entry);
                    break;
                }
            }
        }

    }

    @FXML
    void denyPermissionButtonClicked(ActionEvent event) {
        applyPermissionDecision(false);
    }

    @FXML
    void grantPermissionButtonClicked(ActionEvent event) {
        applyPermissionDecision(true);
    }

    private void applyPermissionDecision(boolean isAccessAllowed) {
        PermissionType permissionType = PermissionFactory.permissionName2PermissionType(
                tableView.getSelectionModel().getSelectedItem().getPermissionType());
        String sheetName = dashboardMainController.getSheetsTableController().
                getCurrentlySelectedSheetMetaData().getSheetName();
        String accessor = tableView.getSelectionModel().getSelectedItem().getUsername();
        RequestPermissionDTO requestPermissionDTO = new RequestPermissionDTO(sheetName,permissionType);

        String finalUrl = HttpUrl
                .parse(Constants.DECIDE_PERMISSION_REQUEST)
                .newBuilder()
                .addQueryParameter("access", String.valueOf(isAccessAllowed))
                .addQueryParameter("accessor",accessor)
                .build()
                .toString();

        HttpClientMessenger.sendPostRequestWithBodyAsync(finalUrl, requestPermissionDTO, new MyCallBack(statusLabel,
                (body -> {})));
    }

}

