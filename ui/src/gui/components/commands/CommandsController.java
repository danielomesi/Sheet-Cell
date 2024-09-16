package gui.components.commands;

import entities.range.Range;
import gui.components.main.MainController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SetProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.Set;

public class CommandsController {

    private MainController mainController;

    @FXML
    private ComboBox<String> rangeComboBox;
    @FXML
    private Button removeRangeButton;
    @FXML
    private TextField rangeNameTextField;
    @FXML
    private Button addRangeButton;



    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        if (mainController != null) {
            BooleanProperty isSheetLoadedProperty = mainController.getIsSheetLoaded();
            rangeComboBox.disableProperty().bind(isSheetLoadedProperty.not());
            rangeNameTextField.disableProperty().bind(isSheetLoadedProperty.not());
            removeRangeButton.disableProperty().bind(isSheetLoadedProperty.not());
        }
    }

    public void updateMyControlsOnFileLoad() {
        SetProperty<String> rangesNamesSetProperty = mainController.getDataModule().getRangesNames();

        Runnable updateComboBoxItems = () -> {
            Set<String> names = rangesNamesSetProperty.get();
            EventHandler<ActionEvent> originalOnAction = rangeComboBox.getOnAction();
            rangeComboBox.setOnAction(null);
            ObservableList<String> observableList = FXCollections.observableArrayList(names);
            rangeComboBox.setItems(observableList);
            rangeComboBox.setOnAction(originalOnAction);
        };

        rangesNamesSetProperty.addListener((observable, oldValue, newValue) -> updateComboBoxItems.run());
    }



    @FXML
    void handleOnRangeSelect(ActionEvent event) {
        String rangeName = rangeComboBox.getSelectionModel().getSelectedItem().toString();
        Range range = mainController.getCurrentLoadedSheet().getRange(rangeName);
        mainController.getCenterController().highlightChosenRangeCells(range);
    }

    @FXML
    void handleOnRemoveRangeButtonClick(ActionEvent event) {
        String rangeToRemove = rangeComboBox.getSelectionModel().getSelectedItem().toString();
        Runnable runnable = () -> mainController.deleteRange(rangeToRemove);
        Task<Void> task = mainController.getHeaderController().getTaskFromRunnable(runnable,false);
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }



    @FXML
    void addRangeButtonOnClick(ActionEvent event) {
        String rangeName = rangeNameTextField.getText();
        String fromCellID = mainController.getCenterController().getSelectedTopLeftCellID();
        String toCellID = mainController.getCenterController().getSelectedBottomRightCellID();
        Runnable runnable = () -> mainController.addRange(rangeName, fromCellID, toCellID);
        Task<Void> task = mainController.getHeaderController().getTaskFromRunnable(runnable,false);
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public Range getSelectedRange() {
        Range selectedRange = null;
        try {
            selectedRange = new Range(null, mainController.getCurrentLoadedSheet(),
                    mainController.getCenterController().getSelectedTopLeftCellID(),
                    mainController.getCenterController().getSelectedBottomRightCellID());
        }
        catch (Exception ignored) {

        }
        return selectedRange;

    }

}
