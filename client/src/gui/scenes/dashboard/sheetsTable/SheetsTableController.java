package gui.scenes.dashboard.sheetsTable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entities.permission.PermissionType;
import entities.permission.PermissionFactory;
import entities.sheet.DTOSheet;
import entities.sheet.Sheet;
import entities.sheet.SheetMetaData;
import gui.scenes.dashboard.main.DashboardMainController;
import http.HttpClientMessenger;
import http.MyCallBack;
import http.RequestScheduler;
import http.constants.Constants;
import http.dtos.RequestPermissionDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import json.GsonInstance;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class SheetsTableController {

    private DashboardMainController dashboardMainController;

    @FXML
    private TableView<SheetTableEntry> tableView;

    @FXML
    private Label statusLabel;
    @FXML
    private Button requestReadAccessButton;

    @FXML
    private Button requestWriteAccessButton;

    @FXML
    private Button viewSheetButton;

    @FXML
    private ScrollPane wrapper;

    private final ObservableList<SheetTableEntry> tableData = FXCollections.observableArrayList();

    private List<SheetMetaData> sheetMetaDataList;

    public Label getStatusLabel() {return statusLabel;}


    public void setDashboardMainController(DashboardMainController DashboardMainController) {this.dashboardMainController = DashboardMainController;}

    @FXML
    public void initialize() {
        // Bind the columns to the Sheet properties
        TableColumn<SheetTableEntry, String> uploaderColumn = (TableColumn<SheetTableEntry, String>) tableView.getColumns().get(0);
        uploaderColumn.setCellValueFactory(new PropertyValueFactory<>("uploader"));

        TableColumn<SheetTableEntry, String> sheetNameColumn = (TableColumn<SheetTableEntry, String>) tableView.getColumns().get(1);
        sheetNameColumn.setCellValueFactory(new PropertyValueFactory<>("sheetName"));

        TableColumn<SheetTableEntry, String> sheetSizeColumn = (TableColumn<SheetTableEntry, String>) tableView.getColumns().get(2);
        sheetSizeColumn.setCellValueFactory(new PropertyValueFactory<>("sheetSize"));

        TableColumn<SheetTableEntry, String> accessLevelColumn = (TableColumn<SheetTableEntry, String>) tableView.getColumns().get(3);
        accessLevelColumn.setCellValueFactory(new PropertyValueFactory<>("accessLevel"));

        tableView.setItems(tableData);

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                String accessLevel = newValue.getAccessLevel();
                PermissionType permissionType = PermissionFactory.permissionName2PermissionType(accessLevel);
                viewSheetButton.setDisable(permissionType.ordinal() < PermissionType.READ.ordinal());
                requestReadAccessButton.setDisable(permissionType.ordinal() >= PermissionType.READ.ordinal());
                requestWriteAccessButton.setDisable(permissionType.ordinal() >= PermissionType.WRITE.ordinal());
                dashboardMainController.getPermissionsTableController().
                        populatePermissionsTable(getSheetMetaDataByName(newValue.getSheetName()));
            }
            else {
                viewSheetButton.setDisable(false);
            }
        });

        startRefreshingTableData();
    }

    private SheetMetaData getSheetMetaDataByName(String sheetName) {
        SheetMetaData res = null;
        for (SheetMetaData sheetMetaData : sheetMetaDataList) {
            if (sheetMetaData.getSheetName().equals(sheetName)) {
                res = sheetMetaData;
            }
        }

        return res;
    }

    public void startRefreshingTableData() {
        String url = HttpUrl
                .parse(Constants.GET_SHEETS_META_DATA) // Update with your actual endpoint
                .toString();

        RequestScheduler.startHttpRequestScheduler(url, new MyCallBack(
                        statusLabel, (this::handleSheetDataResponse)));
    }

    private void handleSheetDataResponse(String body) {
        Gson gson = GsonInstance.getGson();
        Type listType = new TypeToken<List<SheetMetaData>>() {}.getType();
        sheetMetaDataList = gson.fromJson(body, listType);

        Platform.runLater(() -> updateTableData(sheetMetaDataList));
    }

    private void updateTableData(List<SheetMetaData> sheetMetaDataList) {
        SheetTableEntry selectedEntry = tableView.getSelectionModel().getSelectedItem();
        tableData.clear();
        for (SheetMetaData sheetMetaData : sheetMetaDataList) {
            String sheetSize = sheetMetaData.getNumberOfRows() + " x " + sheetMetaData.getNumberOfCols();
            SheetTableEntry entry = new SheetTableEntry(sheetMetaData.getUploaderName(),
                    sheetMetaData.getSheetName(), sheetSize, getPermissionType(sheetMetaData).name());
            tableData.add(entry);
        }

        if (selectedEntry != null) {
            for (SheetTableEntry entry : tableData) {
                if (entry.getSheetName().equals(selectedEntry.getSheetName())) {
                    tableView.getSelectionModel().select(entry);
                    break;
                }
            }
        }
    }

    private PermissionType getPermissionType(SheetMetaData sheetMetaData) {
        String username = dashboardMainController.getUsername();

        return sheetMetaData.getUsername2Permission().getOrDefault(username, PermissionType.NONE);
    }

    @FXML
    void viewSheetButtonClicked(ActionEvent event) {
        String sheetName = tableView.getSelectionModel().getSelectedItem().getSheetName();
        Label taskLabel = dashboardMainController.getHeaderController().getTaskStatusLabel();
        String permissionTypeAsString = tableView.getSelectionModel().getSelectedItem().getAccessLevel();
        PermissionType permissionType = PermissionFactory.permissionName2PermissionType(permissionTypeAsString);

        String finalUrl = HttpUrl
                .parse(Constants.GET_SHEET)
                .newBuilder()
                .addQueryParameter("name", sheetName)
                .build()
                .toString();

        HttpClientMessenger.sendGetRequestWithoutBodyAsync(finalUrl, new MyCallBack(taskLabel
                ,
                (body -> {
                    try {
                        System.out.println(body);
                        DTOSheet sheet = GsonInstance.getGson().fromJson(body, DTOSheet.class);
                        switchSceneToWorkspace(sheet,permissionType);
                    } catch (Exception e) {
                        taskLabel.setText(e.getMessage());
                    }
                })));
    }

    @FXML
    void requestReadAccessButtonClicked(ActionEvent event) {
        requestPermission(PermissionType.READ);
    }

    @FXML
    void requestWriteAccessButtonClicked(ActionEvent event) {
        requestPermission(PermissionType.WRITE);
    }

    private void requestPermission(PermissionType permissionType) {
        String sheetName = tableView.getSelectionModel().getSelectedItem().getSheetName();
        RequestPermissionDTO requestPermissionDTO = new RequestPermissionDTO(sheetName, permissionType);

        String finalUrl = HttpUrl
                .parse(Constants.ADD_PERMISSION_REQUEST)
                .newBuilder()
                .build()
                .toString();

        HttpClientMessenger.sendPostRequestWithBodyAsync(finalUrl, requestPermissionDTO, new MyCallBack(statusLabel,
                (body -> {})));
    }

    public void  switchSceneToWorkspace(Sheet sheet, PermissionType permissionType) throws IOException {
        dashboardMainController.getClientApp().switchSceneToWorkspace(sheet,permissionType);
    }

    public SheetMetaData getCurrentlySelectedSheetMetaData() {
        String sheetName = tableView.getSelectionModel().getSelectedItem().getSheetName();

        return getSheetMetaDataByName(sheetName);
    }
}
