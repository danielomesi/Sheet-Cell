package gui.components.sort;

import gui.components.main.MainController;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;

public class SortController {

    private MainController mainController;

    @FXML
    private Button RemoveColumnFromSortButton;
    @FXML
    private Button addCollumnToSortButton;
    @FXML
    private Button moveDownButton;
    @FXML
    private Button moveUpButton;
    @FXML
    private ListView<String> selectedColumnsListView;
    @FXML
    private HBox wrapperHbox;
    @FXML
    private ListView<String> columnsListView;
    @FXML
    private ScrollPane tableScrollPane;

    public void initialize() {
        BooleanBinding isSelectionEmptyInAllColsList = Bindings.createBooleanBinding(
                () -> columnsListView.getSelectionModel().getSelectedItem() == null,
                columnsListView.getSelectionModel().selectedItemProperty());
        BooleanBinding isSelectionEmptyInSelectedColsList = Bindings.createBooleanBinding(
                () -> selectedColumnsListView.getSelectionModel().getSelectedItem() == null,
                selectedColumnsListView.getSelectionModel().selectedItemProperty());
        BooleanBinding isSelectionLastInSelectedColsList = Bindings.createBooleanBinding(() -> {
            ObservableList<String> items = selectedColumnsListView.getItems();
            String selectedItem = selectedColumnsListView.getSelectionModel().getSelectedItem();
            return !items.isEmpty() && selectedItem != null && selectedItem.equals(items.getLast());
        }, selectedColumnsListView.getSelectionModel().selectedItemProperty(), selectedColumnsListView.itemsProperty());
        BooleanBinding isSelectionFirstInSelectedColsList = Bindings.createBooleanBinding(() -> {
            ObservableList<String> items = selectedColumnsListView.getItems();
            String selectedItem = selectedColumnsListView.getSelectionModel().getSelectedItem();
            return !items.isEmpty() && selectedItem != null && selectedItem.equals(items.getFirst());
        }, selectedColumnsListView.getSelectionModel().selectedItemProperty(), selectedColumnsListView.itemsProperty());


        addCollumnToSortButton.disableProperty().bind(isSelectionEmptyInAllColsList);
        RemoveColumnFromSortButton.disableProperty().bind(isSelectionEmptyInSelectedColsList);
        moveDownButton.disableProperty().bind(isSelectionLastInSelectedColsList);
        moveUpButton.disableProperty().bind(isSelectionFirstInSelectedColsList);
    }

    public void setMainController(MainController mainController) {this.mainController = mainController;}
    public HBox getWrapper() {return wrapperHbox;}
    public void addTable(GridPane gridPane) {
        tableScrollPane.setContent(gridPane);
    }

    public void populateListViewOfAllCols(List<String> columns) {
        columnsListView.getItems().clear();
        columnsListView.getItems().addAll(columns);
    }

    public void populateListViewOfSelectedCols(List<String> selectedCols) {
        selectedColumnsListView.getItems().clear();
        selectedColumnsListView.getItems().addAll(selectedCols);
    }



    @FXML
    void RemoveColumnFromSortButtonClicked(ActionEvent event) {
        String colToRemove = selectedColumnsListView.getSelectionModel().getSelectedItem();
        selectedColumnsListView.getItems().remove(colToRemove);
        columnsListView.getItems().add(colToRemove);
    }

    @FXML
    void addColumnToSortButtonClicked(ActionEvent event) {
        String selectedCol = columnsListView.getSelectionModel().getSelectedItem();
        selectedColumnsListView.getItems().add(selectedCol);
        columnsListView.getItems().remove(selectedCol);

    }

    @FXML
    void moveDownButtonClicked(ActionEvent event) {
        moveChoiceNSteps(1);
    }

    @FXML
    void moveUpButtonClicked(ActionEvent event) {
        moveChoiceNSteps(-1);
    }

    private void moveChoiceNSteps(int steps) {
        String colToMoveDown = selectedColumnsListView.getSelectionModel().getSelectedItem();
        ObservableList<String> items = selectedColumnsListView.getItems();

        int selectedIndex = items.indexOf(colToMoveDown);
        int nextIndex = selectedIndex + steps;
        String itemBelow = items.get(nextIndex);

        items.set(selectedIndex, itemBelow);
        items.set(nextIndex, colToMoveDown);

        selectedColumnsListView.getSelectionModel().select(nextIndex);
    }

}
