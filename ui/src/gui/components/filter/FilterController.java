package gui.components.filter;

import gui.builder.DynamicSheetTable;
import gui.components.main.MainController;
import gui.utils.Utils;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FilterController {

    private MainController mainController;
    private String fromCellID;
    private String toCellID;
    private DynamicSheetTable dynamicSheetTable;

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


        addValueToFilterButton.disableProperty().bind(isSelectionEmptyInAllColsList);
        removeValueFromSelectedButton.disableProperty().bind(isSelectionEmptyInSelectedColsList);
        filterButton.setDisable(true);



    }

    public void populateColComboBox(int numOfCols) {
        List<String> colNames = Utils.getLettersFromAToTheNLetter(numOfCols);
        ObservableList<String> observableColNames = FXCollections.observableArrayList(colNames);
        colsComboBox.setItems(observableColNames);
    }

    public void populateAllDistinctValuesListView(List<Object> effectiveValues) {
        Set<String> valuesAsStrings = new HashSet<>();
        effectiveValues.forEach((effectiveValue) -> {valuesAsStrings.add(Utils.objectToString(effectiveValue));});
        valuesAsStrings.add(Utils.NON_EXISTING_CELL_NAME);
        ObservableList<String> observableValues = FXCollections.observableArrayList(valuesAsStrings);
        allValuesListView.setItems(observableValues);

    }

    @FXML
    void addValueToFilterButtonClicked(ActionEvent event) {
        String selectedCol = allValuesListView.getSelectionModel().getSelectedItem();
        selectedValuesListView.getItems().add(selectedCol);
        allValuesListView.getItems().remove(selectedCol);
        if (filterButton.isDisabled()) {
            filterButton.setDisable(false);
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
    }

    @FXML
    void removeValueFromSelectedButtonClicked(ActionEvent event) {
        String colToRemove = selectedValuesListView.getSelectionModel().getSelectedItem();
        selectedValuesListView.getItems().remove(colToRemove);
        allValuesListView.getItems().add(colToRemove);
        if (selectedValuesListView.getItems().isEmpty()) {
            filterButton.setDisable(true);
        }
    }

    @FXML
    void sortFirstRowToggleButtonClicked(ActionEvent event) {

    }

}
