package gui.scenes.workspace.commands;

import entities.range.RangeDTO;
import entities.range.RangeInterface;
import gui.scenes.workspace.main.MainController;
import gui.utils.Utils;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Set;

import static gui.utils.Utils.getTaskFromRunnable;

public class CommandsController {

    private MainController mainController;
    @FXML
    private ProgressIndicator commandsProgressIndicator;
    @FXML
    private Label commandsStatusLabel;
    @FXML
    private ComboBox<String> rangeComboBox;
    @FXML
    private Button removeRangeButton;
    @FXML
    private TextField rangeNameTextField;
    @FXML
    private Button addRangeButton;
    @FXML
    private Button openDynamicAnalyzeDialogButton;
    @FXML
    private Button openFilterDialogButton;

    @FXML
    private Button openSortDialogButton;



    public void setMainController(MainController mainController) {
        this.mainController = mainController;

        if (mainController != null) {
            BooleanProperty isSheetLoadedProperty = mainController.getIsSheetLoaded();
            BooleanProperty is2validCellsSelected = mainController.getSheetController().getIs2ValidCellsSelected();
            BooleanProperty isSheetSynced = mainController.getSheetController().getIsSheetSynced();
            rangeComboBox.disableProperty().bind(isSheetLoadedProperty.not());
            rangeNameTextField.disableProperty().bind(isSheetLoadedProperty.not().or(is2validCellsSelected.not().
                    or(mainController.getIsWriteAccessAllowed().not()).or(isSheetSynced.not())));
            removeRangeButton.disableProperty().bind(isSheetLoadedProperty.not().
                    or(rangeComboBox.getSelectionModel().selectedItemProperty().isNull()).
                    or(mainController.getIsWriteAccessAllowed().not()).or(isSheetSynced.not()));
            addRangeButton.disableProperty().bind(is2validCellsSelected.not().
                    or(mainController.getIsWriteAccessAllowed().not()).or(isSheetSynced.not()));
            openDynamicAnalyzeDialogButton.disableProperty().bind(mainController.getSheetController().getSelectedCellController().isNull().
                    or(mainController.getIsWriteAccessAllowed().not()).or(isSheetSynced.not()));;
            openFilterDialogButton.disableProperty().bind(is2validCellsSelected.not());
            openSortDialogButton.disableProperty().bind(is2validCellsSelected.not());
        }
    }

    public RangeInterface getSelectedRange() {
        RangeInterface selectedRange = null;
        try {
            selectedRange = new RangeDTO(mainController.getSheetController().getSelectedTopLeftCellID(),
                    mainController.getSheetController().getSelectedBottomRightCellID());
        }
        catch (Exception ignored) {

        }
        return selectedRange;
    }

    public void updateMyControlsOnFileLoad() {
        rangeComboBox.getSelectionModel().clearSelection();
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
        String rangeName = rangeComboBox.getSelectionModel().getSelectedItem();
        RangeInterface range = mainController.getCurrentLoadedSheet().getRange(rangeName);
        mainController.getSheetController().highlightChosenRangeCells(range);
    }

    @FXML
    void handleOnRemoveRangeButtonClick(ActionEvent event) {
        String rangeToRemove = rangeComboBox.getSelectionModel().getSelectedItem();
        Runnable runnable = () -> mainController.deleteRange(rangeToRemove);
        boolean isAnimationsEnabled = mainController.getAppearanceController().isAnimationsEnabled();
        Task<Void> task = getTaskFromRunnable(runnable, commandsProgressIndicator, isAnimationsEnabled);
        Utils.runTaskInADaemonThread(task);
    }



    @FXML
    void addRangeButtonOnClick(ActionEvent event) {
        String rangeName = rangeNameTextField.getText();
        String fromCellID = mainController.getSheetController().getSelectedTopLeftCellID();
        String toCellID = mainController.getSheetController().getSelectedBottomRightCellID();
        Runnable runnable = () -> mainController.addRange(rangeName, fromCellID, toCellID);
        boolean isAnimationsEnabled = mainController.getAppearanceController().isAnimationsEnabled();
        Task<Void> task = Utils.getTaskFromRunnable(runnable, commandsProgressIndicator,isAnimationsEnabled);
        Utils.runTaskInADaemonThread(task);
    }

    @FXML
    void openDynamicAnalyzeDialogButtonClicked(ActionEvent event) {
        String cellID = mainController.getSheetController().getSelectedCellCoordinates().getCellID();
        mainController.openAnalyzeWindow(cellID);
    }

    @FXML
    void openSortDialogButtonClicked(ActionEvent event) {
        String fromCellID = mainController.getSheetController().getSelectedTopLeftCellID();
        String toCellID = mainController.getSheetController().getSelectedBottomRightCellID();
        Runnable runnable = () -> mainController.openSortDialog(fromCellID, toCellID);
        boolean isAnimationsEnabled = mainController.getAppearanceController().isAnimationsEnabled();
        Task<Void> task = Utils.getTaskFromRunnable(runnable, commandsProgressIndicator,isAnimationsEnabled);
        Utils.runTaskInADaemonThread(task);
    }

    @FXML
    void openFilterDialogClicked(ActionEvent event) {
        String fromCellID = mainController.getSheetController().getSelectedTopLeftCellID();
        String toCellID = mainController.getSheetController().getSelectedBottomRightCellID();
        Runnable runnable = () -> mainController.openFilterDialog(fromCellID, toCellID);
        boolean isAnimationsEnabled = mainController.getAppearanceController().isAnimationsEnabled();
        Task<Void> task = Utils.getTaskFromRunnable(runnable, commandsProgressIndicator,isAnimationsEnabled);
        Utils.runTaskInADaemonThread(task);
    }

    @FXML
    void backToDashboardButtonClicked(ActionEvent event) {
        mainController.getClientApp().switchSceneBackToDashboardFromWorkspace();
    }


}
