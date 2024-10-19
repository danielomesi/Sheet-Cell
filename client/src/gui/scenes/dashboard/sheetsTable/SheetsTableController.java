package gui.scenes.dashboard.sheetsTable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import entities.cell.DTOCell;
import entities.coordinates.Coordinates;
import entities.sheet.DTOSheet;
import entities.sheet.Sheet;
import entities.sheet.SheetMetaData;
import gui.scenes.dashboard.main.DashboardMainController;
import http.HttpClientMessenger;
import http.MyCallBack;
import http.MyResponseHandler;
import http.constants.Constants;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import json.CellsMapDeserializer;
import json.EffectiveValueDeserializer;
import json.GsonInstance;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class SheetsTableController {

    private DashboardMainController dashboardMainController;

    @FXML
    private TableView<SheetTableEntry> tableView;

    @FXML
    private Button viewSheetButton;

    @FXML
    private ScrollPane wrapper;

    private final ObservableList<SheetTableEntry> tableData = FXCollections.observableArrayList();

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
            // Enable the button if a row is selected, otherwise disable it
            viewSheetButton.setDisable(newValue == null);
        });

        startRefreshingTableData();
    }

    private void startRefreshingTableData() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> fetchSheetData()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void fetchSheetData() {
        String url = HttpUrl
                .parse(Constants.GET_SHEETS_META_DATA) // Update with your actual endpoint
                .toString();

        HttpClientMessenger.sendGetRequestWithoutBodyAsync(url, new MyCallBack(
                dashboardMainController.getHeaderController().getTaskStatusLabel(),
                this::handleSheetDataResponse
        ));
    }

    private void handleSheetDataResponse(String body) {
        try {
            Gson gson = GsonInstance.getGson();
            Type listType = new TypeToken<List<SheetMetaData>>() {}.getType();
            List<SheetMetaData> sheetMetaDataList = gson.fromJson(body, listType);

            // Update the table view on the JavaFX application thread
            Platform.runLater(() -> updateTableData(sheetMetaDataList));

        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void updateTableData(List<SheetMetaData> sheetMetaDataList) {
        SheetTableEntry selectedEntry = tableView.getSelectionModel().getSelectedItem();
        tableData.clear();
        for (SheetMetaData sheetMetaData : sheetMetaDataList) {
            String sheetSize = sheetMetaData.getNumberOfRows() + " x " + sheetMetaData.getNumberOfCols();
            SheetTableEntry entry = new SheetTableEntry(sheetMetaData.getUploaderName(),
                    sheetMetaData.getSheetName(), sheetSize, "OWNER");
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

//    public void addTableEntry(SheetMetaData sheetMetaData) {
//        String sheetSize = sheetMetaData.getNumberOfRows() + " x " + sheetMetaData.getNumberOfCols();
//        SheetTableEntry tableEntry = new SheetTableEntry(sheetMetaData.getUploaderName(),sheetMetaData.getSheetName(),
//                sheetSize,"OWNER");
//        tableView.getItems().add(tableEntry);
//    }

    @FXML
    void viewSheetButtonClicked(ActionEvent event) {
        String sheetName = tableView.getSelectionModel().getSelectedItem().getSheetName();
        Label taskLabel = dashboardMainController.getHeaderController().getTaskStatusLabel();

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
                        switchSceneToWorkspace(sheet);
                    } catch (Exception e) {
                        taskLabel.setText(e.getMessage());
                    }
                })));
    }

    public void  switchSceneToWorkspace(Sheet sheet) throws IOException {
        dashboardMainController.getClientApp().switchSceneToWorkspace(sheet);
    }
}
