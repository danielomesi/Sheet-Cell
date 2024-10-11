package gui.scenes.dashboard.sheetsTable;

import com.google.gson.Gson;
import entities.sheet.Sheet;
import entities.sheet.SheetMetaData;
import gui.scenes.dashboard.main.DashboardMainController;
import http.HttpClientMessenger;
import http.constants.Constants;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class SheetsTableController {

    private DashboardMainController dashboardMainController;

    @FXML
    private TableView<SheetTableEntry> tableView;

    @FXML
    private Button viewSheetButton;

    @FXML
    private ScrollPane wrapper;

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

        tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Enable the button if a row is selected, otherwise disable it
            viewSheetButton.setDisable(newValue == null);
        });
    }

    public void addTableEntry(SheetMetaData sheetMetaData) {
        String sheetSize = sheetMetaData.getNumberOfRows() + " x " + sheetMetaData.getNumberOfCols();
        SheetTableEntry tableEntry = new SheetTableEntry(sheetMetaData.getUploaderName(),sheetMetaData.getSheetName(),
                sheetSize,"OWNER");
        tableView.getItems().add(tableEntry);
    }

    @FXML
    void viewSheetButtonClicked(ActionEvent event) {
        String sheetName = tableView.getSelectionModel().getSelectedItem().getSheetName();

        String finalUrl = HttpUrl
                .parse(Constants.GET_SHEET)
                .newBuilder()
                .addQueryParameter("name", sheetName)
                .build()
                .toString();

        HttpClientMessenger.sendGetRequestWithoutBodyAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        dashboardMainController.getHeaderController().getTaskStatusLabel().setText("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                HttpClientMessenger.genericOnResponseHandler(
                        () -> {
                            try {
                                ResponseBody responseBody = response.body();
                                Gson gson = new Gson();
                                Sheet sheet = gson.fromJson(responseBody.string(), Sheet.class);
                                switchSceneToWorkspace(sheet);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        },
                        response, dashboardMainController.getHeaderController().getTaskStatusLabel()
                );
            }
        });
    }

    public void  switchSceneToWorkspace(Sheet sheet) throws IOException {
        dashboardMainController.getClientApp().switchSceneToWorkspace(sheet);
    }
}
