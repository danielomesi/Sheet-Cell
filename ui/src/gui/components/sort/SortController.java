package gui.components.sort;

import gui.builder.DynamicBuilder;
import gui.builder.DynamicSheetTable;
import gui.components.main.MainController;
import gui.utils.Utils;
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

import java.util.List;

public class SortController {

    private MainController mainController;
    private String fromCellID;
    private String toCellID;
    private DynamicSheetTable dynamicSheetTable;
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
    private HBox wrapperHbox;
    @FXML
    private ListView<String> allColsListView;
    @FXML
    private ScrollPane tableScrollPane;
    @FXML
    private Button sortButton;

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
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void setDynamicSheetTable(DynamicSheetTable dynamicSheetTable) {
        this.dynamicSheetTable = dynamicSheetTable;
    }

    public void setFromCellID(String fromCellID) {
        this.fromCellID = fromCellID;
    }

    public void setToCellID(String toCellID) {
        this.toCellID = toCellID;
    }

    public HBox getWrapper() {
        return wrapperHbox;
    }

    public void setTable(GridPane gridPane) {
        tableScrollPane.setContent(gridPane);
    }

    public void populateListViewOfAllCols(List<String> columns) {
        allColsListView.getItems().clear();
        allColsListView.getItems().addAll(columns);
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
        moveChoiceNSteps(1);
    }

    @FXML
    void moveUpButtonClicked(ActionEvent event) {
        moveChoiceNSteps(-1);
    }

    @FXML
    void sortButtonClicked(ActionEvent event) {
        List<String> colsToSortBy = selectedColsListView.getItems();
        Runnable sort = () -> {
            List<Integer> sortedRowsOrder = mainController.getEngine().sort(colsToSortBy, fromCellID, toCellID);
            Platform.runLater(() -> dynamicSheetTable.changeRowsOrder(sortedRowsOrder));};

        Task<Void> sortTask =  mainController.getHeaderController().getTaskFromRunnable(sort, false);
        Utils.runTaskInADaemonThread(sortTask);
    }

    private void moveChoiceNSteps(int steps) {
        String colToMoveDown = selectedColsListView.getSelectionModel().getSelectedItem();
        ObservableList<String> items = selectedColsListView.getItems();

        int selectedIndex = items.indexOf(colToMoveDown);
        int nextIndex = selectedIndex + steps;
        String itemBelow = items.get(nextIndex);

        items.set(selectedIndex, itemBelow);
        items.set(nextIndex, colToMoveDown);

        selectedColsListView.getSelectionModel().select(nextIndex);
    }

}
