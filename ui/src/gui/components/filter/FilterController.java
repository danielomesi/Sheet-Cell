package gui.components.filter;

import gui.builder.DynamicSheetTable;
import gui.components.main.MainController;
import gui.utils.Utils;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.*;

public class FilterController {

    private MainController mainController;
    private String fromCellID;
    private String toCellID;
    private DynamicSheetTable dynamicSheetTable;
    private final Map<String,Object> str2EffectiveValueMap = new HashMap<>();
    BooleanProperty isFilteringActive = new SimpleBooleanProperty(true);
    BooleanProperty isExistValueToFilter = new SimpleBooleanProperty(false);

    @FXML
    private Button addValueToFilterButton;
    @FXML
    private ListView<String> allValuesListView;
    @FXML
    private ComboBox<String> colsComboBox;
    @FXML
    private Button filterButton;
    @FXML
    private Button removeValueFromSelectedButton;
    @FXML
    private ListView<String> selectedValuesListView;
    @FXML
    private ScrollPane tableScrollPane;
    @FXML
    private ProgressBar taskProgressBar;
    @FXML
    private Label taskStatus;
    @FXML
    private HBox wrapperHbox;
    @FXML
    private ToggleButton includeEmptyCellsInFilterButton;
    @FXML
    private Button resetButton;

    //setters
    public void setMainController(MainController mainController) {this.mainController = mainController;}
    public void setDynamicSheetTable(DynamicSheetTable dynamicSheetTable) {this.dynamicSheetTable = dynamicSheetTable;}
    public void setFromCellID(String fromCellID) {this.fromCellID = fromCellID;}
    public void setToCellID(String toCellID) {this.toCellID = toCellID;}

    public HBox getWrapper() {
        return wrapperHbox;
    }

    public void setTable(GridPane gridPane) {
        tableScrollPane.setContent(gridPane);
    }

    public void initialize() {
        BooleanBinding isColSelected = colsComboBox.valueProperty().isNull();
        BooleanBinding isSelectionEmptyInAllColsList = Bindings.createBooleanBinding(
                () -> allValuesListView.getSelectionModel().getSelectedItem() == null,
                allValuesListView.getSelectionModel().selectedItemProperty());

        BooleanBinding isSelectionEmptyInSelectedColsList = Bindings.createBooleanBinding(
                () -> selectedValuesListView.getSelectionModel().getSelectedItem() == null,
                selectedValuesListView.getSelectionModel().selectedItemProperty());


        addValueToFilterButton.disableProperty().bind(isSelectionEmptyInAllColsList.or(isFilteringActive.not()));
        removeValueFromSelectedButton.disableProperty().bind(isSelectionEmptyInSelectedColsList.or(isFilteringActive.not()));
        colsComboBox.disableProperty().bind(isFilteringActive.not());
        filterButton.disableProperty().bind(isFilteringActive.not().or(isExistValueToFilter.not()));
        resetButton.disableProperty().bind(isFilteringActive);
        includeEmptyCellsInFilterButton.setSelected(false);

    }

    public void populateColComboBox(int numOfCols) {
        List<String> colNames = Utils.getLettersFromAToTheNLetter(numOfCols);
        ObservableList<String> observableColNames = FXCollections.observableArrayList(colNames);
        colsComboBox.setItems(observableColNames);
    }

    public void populateAllDistinctValuesListView(List<Object> effectiveValues) {
        str2EffectiveValueMap.clear();
        Set<String> valuesAsStrings = new HashSet<>();
        effectiveValues.forEach((effectiveValue) -> {
            String str = Utils.objectToString(effectiveValue);
            valuesAsStrings.add(str);
            str2EffectiveValueMap.put(str, effectiveValue);
        });
        ObservableList<String> observableValues = FXCollections.observableArrayList(valuesAsStrings);
        allValuesListView.setItems(observableValues);

    }

    @FXML
    void addValueToFilterButtonClicked(ActionEvent event) {
        String selectedCol = allValuesListView.getSelectionModel().getSelectedItem();
        selectedValuesListView.getItems().add(selectedCol);
        allValuesListView.getItems().remove(selectedCol);
        if (!isExistValueToFilter.getValue()) {
            isExistValueToFilter.setValue(true);
        }
    }

    @FXML
    void columnInComboBoxSelected(ActionEvent event) {
        selectedValuesListView.getItems().clear();
        String selectedColName = colsComboBox.getSelectionModel().getSelectedItem();
        Runnable runnable = () -> {
            List<Object> effectiveValuesOfSelectedCol = mainController.getEngine().
                    getEffectiveValuesInSpecificCol(selectedColName, fromCellID, toCellID);
            Platform.runLater(() -> populateAllDistinctValuesListView(effectiveValuesOfSelectedCol));
        };
        Task<Void> task = Utils.getTaskFromRunnable(runnable,taskStatus,taskProgressBar,false);
        Utils.runTaskInADaemonThread(task);
    }

    @FXML
    void filterButtonClicked(ActionEvent event) {
        List<Object> selectedEffectiveValues = new ArrayList<>();
        String selectedColName = colsComboBox.getSelectionModel().getSelectedItem();
        selectedValuesListView.getItems().forEach((effectiveValue) -> {
            selectedEffectiveValues.add(str2EffectiveValueMap.get(effectiveValue));
        });
        Runnable filter = () -> {
            Set<Integer> rowsToDelete = mainController.getEngine().filter(selectedColName,selectedEffectiveValues,fromCellID,toCellID,
                    includeEmptyCellsInFilterButton.isSelected());
            Platform.runLater(() -> {
                dynamicSheetTable.removeRows(rowsToDelete);
                isFilteringActive.setValue(false);
            });
        };
        boolean isAnimationsEnabled = mainController.getAppearanceController().isAnimationsEnabled();
        Task<Void> task = Utils.getTaskFromRunnable(filter,taskStatus,taskProgressBar,isAnimationsEnabled);
        Utils.runTaskInADaemonThread(task);
    }

    @FXML
    void removeValueFromSelectedButtonClicked(ActionEvent event) {
        String colToRemove = selectedValuesListView.getSelectionModel().getSelectedItem();
        selectedValuesListView.getItems().remove(colToRemove);
        allValuesListView.getItems().add(colToRemove);
        if (selectedValuesListView.getItems().isEmpty()) {
            isExistValueToFilter.setValue(false);
        }
    }

    @FXML
    void includeEmptyCellsInFilterButtonClicked(ActionEvent event) {
        if (includeEmptyCellsInFilterButton.isSelected()) {
            includeEmptyCellsInFilterButton.setText("ON");
        } else {
            includeEmptyCellsInFilterButton.setText("OFF");
        }
    }

    @FXML
    void resetButtonClicked(ActionEvent event) {
        isFilteringActive.setValue(true);
    }

}
