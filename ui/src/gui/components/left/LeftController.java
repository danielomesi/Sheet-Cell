package gui.components.left;

import entities.range.Range;
import gui.components.main.MainController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
import javafx.scene.layout.VBox;

import java.util.Set;

public class LeftController {

    private MainController mainController;

    @FXML
    private VBox leftVBox;
    @FXML
    private ComboBox<String> rangeComboBox;
    @FXML
    private Button removeRangeButton;
    @FXML
    private TextField rangeNameTextField;
    @FXML
    private Button selectCellsButton;
    @FXML
    private Label selectedBottomRightCellLabel;
    @FXML
    private Label selectedTopLeftCellLabel;
    @FXML
    private Button addRangeButton;

    private final BooleanProperty isSelectingFirstCell = new SimpleBooleanProperty(false);
    private final BooleanProperty isSelectingSecondCell = new SimpleBooleanProperty(false); ;

    public BooleanProperty getIsSelectingFirstCell() {return isSelectingFirstCell;}
    public BooleanProperty getIsSelectingSecondCell() {return isSelectingSecondCell;}

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        if (mainController != null) {
            BooleanProperty isSheetLoadedProperty = mainController.getIsSheetLoaded();
            rangeComboBox.disableProperty().bind(isSheetLoadedProperty.not());
            rangeNameTextField.disableProperty().bind(isSheetLoadedProperty.not());
            removeRangeButton.disableProperty().bind(isSheetLoadedProperty.not());
            selectCellsButton.disableProperty().bind(isSheetLoadedProperty.not().or(isSelectingFirstCell).
                    or(isSelectingSecondCell));
            addRangeButton.disableProperty().bind(isSheetLoadedProperty.not().or
                    (selectedTopLeftCellLabel.textProperty().isEmpty()).or
                    (selectedBottomRightCellLabel.textProperty().isEmpty()).or
                    (rangeNameTextField.textProperty().isEmpty()));
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

    public void updateSelectedCellSIDLabel(String topLeftText, String bottomRightText) {
        selectedTopLeftCellLabel.setText(topLeftText);
        selectedBottomRightCellLabel.setText(bottomRightText);
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
    void handleSelectCellsButtonClick(ActionEvent event) {
        isSelectingFirstCell.set(true);
        isSelectingSecondCell.set(true);
    }

    @FXML
    void addRangeButtonOnClick(ActionEvent event) {
        String rangeName = rangeNameTextField.getText();
        String fromCellID = selectedTopLeftCellLabel.getText();
        String toCellID = selectedBottomRightCellLabel.getText();
        Runnable runnable = () -> mainController.addRange(rangeName, fromCellID, toCellID);
        Task<Void> task = mainController.getHeaderController().getTaskFromRunnable(runnable,false);
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public Range getSelectedRange() {
        return new Range(null, mainController.getCurrentLoadedSheet(),
                selectedTopLeftCellLabel.getText(), selectedBottomRightCellLabel.getText());
    }

}
