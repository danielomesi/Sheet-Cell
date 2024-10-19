package gui.scenes.workspace.sort;

import entities.sheet.DTOSheet;
import entities.sheet.Sheet;
import gui.builder.ControllersBuilder;
import gui.builder.DynamicSheet;
import gui.builder.DynamicSheetBuilder;
import gui.scenes.workspace.main.MainController;
import gui.utils.Utils;
import http.HttpClientMessenger;
import http.MyCallBack;
import http.MyResponseHandler;
import http.constants.Constants;
import http.dtos.SortRequestDTO;
import http.dtos.SortResponseDTO;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import json.GsonInstance;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class SortController {

    private MainController mainController;
    private String fromCellID;
    private String toCellID;
    private DynamicSheet dynamicSheet;
    @FXML
    private Button RemoveColumnFromSortButton;
    @FXML
    private Button addCollumnToSortButton;
    @FXML
    private Button moveDownButton;
    @FXML
    private Button moveUpButton;
    @FXML
    private ListView<String> selectedColsListView;
    @FXML
    private ScrollPane wrapperScrollPane;
    @FXML
    private HBox containerHBox;
    @FXML
    private ListView<String> allColsListView;
    @FXML
    private ScrollPane tableScrollPane;
    @FXML
    private Button sortButton;
    @FXML
    private ToggleButton sortFirstRowToggleButton;
    @FXML
    private ProgressIndicator taskProgressIndicator;

    @FXML
    private Label taskStatus;

    //setters
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        if (mainController != null) {
            setStyle(mainController.getAppearanceController().getSelectedStyle());
        }
    }
    public void setDynamicSheetTable(DynamicSheet dynamicSheet) {
        this.dynamicSheet = dynamicSheet;
    }
    public void setFromCellID(String fromCellID) {
        this.fromCellID = fromCellID;
    }
    public void setToCellID(String toCellID) {
        this.toCellID = toCellID;
    }
    public void setTable(GridPane gridPane) {
        tableScrollPane.setContent(gridPane);
    }
    public void setStyle(String styleFileName) {
        Utils.setStyle(wrapperScrollPane,styleFileName);
    }

    //getters
    public ScrollPane getWrapper() {
        return wrapperScrollPane;
    }

    public void initialize() {
        BooleanBinding isSelectionEmptyInAllColsList = Bindings.createBooleanBinding(
                () -> allColsListView.getSelectionModel().getSelectedItem() == null,
                allColsListView.getSelectionModel().selectedItemProperty());

        BooleanBinding isSelectionEmptyInSelectedColsList = Bindings.createBooleanBinding(
                () -> selectedColsListView.getSelectionModel().getSelectedItem() == null,
                selectedColsListView.getSelectionModel().selectedItemProperty());

        BooleanBinding isSelectionLastInSelectedColsList = Bindings.createBooleanBinding(() -> {
                    ObservableList<String> items = selectedColsListView.getItems();
                    String selectedItem = selectedColsListView.getSelectionModel().getSelectedItem();
                    return !items.isEmpty() && selectedItem != null && selectedItem.equals(items.getLast());
                },
                selectedColsListView.getSelectionModel().selectedItemProperty(), selectedColsListView.itemsProperty());

        BooleanBinding isSelectionFirstInSelectedColsList = Bindings.createBooleanBinding(() -> {
                    ObservableList<String> items = selectedColsListView.getItems();
                    String selectedItem = selectedColsListView.getSelectionModel().getSelectedItem();
                    return !items.isEmpty() && selectedItem != null && selectedItem.equals(items.getFirst());
                },
                selectedColsListView.getSelectionModel().selectedItemProperty(), selectedColsListView.itemsProperty());

        sortButton.setDisable(true);
        addCollumnToSortButton.disableProperty().bind(isSelectionEmptyInAllColsList);
        RemoveColumnFromSortButton.disableProperty().bind(isSelectionEmptyInSelectedColsList);
        moveDownButton.disableProperty().bind(isSelectionLastInSelectedColsList);
        moveUpButton.disableProperty().bind(isSelectionFirstInSelectedColsList);
        sortFirstRowToggleButton.setSelected(true);
    }

    public void populateListViewOfAllCols(List<String> columns) {
        allColsListView.getItems().clear();
        allColsListView.getItems().addAll(columns);
    }

    private void moveChoiceNStepsInListView(int steps) {
        String colToMoveDown = selectedColsListView.getSelectionModel().getSelectedItem();
        ObservableList<String> items = selectedColsListView.getItems();

        int selectedIndex = items.indexOf(colToMoveDown);
        int nextIndex = selectedIndex + steps;
        String itemBelow = items.get(nextIndex);

        items.set(selectedIndex, itemBelow);
        items.set(nextIndex, colToMoveDown);

        selectedColsListView.getSelectionModel().select(nextIndex);
    }

    @FXML
    void RemoveColumnFromSortButtonClicked(ActionEvent event) {
        String colToRemove = selectedColsListView.getSelectionModel().getSelectedItem();
        selectedColsListView.getItems().remove(colToRemove);
        allColsListView.getItems().add(colToRemove);
        if (selectedColsListView.getItems().isEmpty()) {
            sortButton.setDisable(true);
        }
    }

    @FXML
    void addColumnToSortButtonClicked(ActionEvent event) {
        String selectedCol = allColsListView.getSelectionModel().getSelectedItem();
        selectedColsListView.getItems().add(selectedCol);
        allColsListView.getItems().remove(selectedCol);
        if (sortButton.isDisabled()) {
            sortButton.setDisable(false);
        }
    }

    @FXML
    void moveDownButtonClicked(ActionEvent event) {
        moveChoiceNStepsInListView(1);
    }

    @FXML
    void moveUpButtonClicked(ActionEvent event) {
        moveChoiceNStepsInListView(-1);
    }

    @FXML
    void sortButtonClicked(ActionEvent event) {
        boolean isAnimationsEnabled = mainController.getAppearanceController().isAnimationsEnabled();
        Task<Void> sortTask =  Utils.getTaskFromRunnable(this::sort,taskStatus, taskProgressIndicator, isAnimationsEnabled);
        Utils.runTaskInADaemonThread(sortTask);
    }

    private void sort() {
        List<String> colsToSortBy = selectedColsListView.getItems();
        String finalUrl = HttpUrl
                .parse(Constants.SORT)
                .newBuilder()
                .build()
                .toString();

        SortRequestDTO sortRequestDTO = new SortRequestDTO(mainController.getCurrentSheetName(),colsToSortBy,
                fromCellID, toCellID,sortFirstRowToggleButton.isSelected());
        HttpClientMessenger.sendPostRequestWithBodyAsync(finalUrl, sortRequestDTO, new MyCallBack(taskStatus,
                (body -> {
                    SortResponseDTO sortResponseDTO = GsonInstance.getGson().fromJson(body, SortResponseDTO.class);
                    List<Integer> sortedRowsOrder = sortResponseDTO.getSortedRowsOrder();
                    Platform.runLater(() -> {
                                setTable(DynamicSheetBuilder.buildSortedDynamicSheetFromMainSheetAndSubDynamicSheet(
                                                mainController.getCurrentLoadedSheet(),dynamicSheet,fromCellID,toCellID,sortedRowsOrder)
                                        .getGridPane());
                            }
                    );
                })));

    }

    @FXML
    void sortFirstRowToggleButtonClicked(ActionEvent event) {
        if (sortFirstRowToggleButton.isSelected()) {
            sortFirstRowToggleButton.setText("ON");
        } else {
            sortFirstRowToggleButton.setText("OFF");
        }
    }

}
