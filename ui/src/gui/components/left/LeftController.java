package gui.components.left;

import com.sun.tools.javac.Main;
import entities.range.Range;
import gui.components.main.MainController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SetProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
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
    private ComboBox<?> rangeComboBox;
    @FXML
    private Button removeRangeButton;
    @FXML
    private TextField rangeNameTextField;
    @FXML
    private Button selectTopLeftCellButton;
    @FXML
    private Button selectBottomRightCellButton;
    @FXML
    private Label selectedBottomRightCellLabel;
    @FXML
    private Label selectedTopLeftCellLabel;
    @FXML
    private Button addRangeButton;

    private final BooleanProperty isSelectingTopLeftCell = new SimpleBooleanProperty(false);
    private final BooleanProperty isSelectingBottomRightCell = new SimpleBooleanProperty(false); ;

    public BooleanProperty getIsSelectingTopLeftCell() {return isSelectingTopLeftCell;}
    public BooleanProperty getIsSelectingBottomRightCell() {return isSelectingBottomRightCell;}

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        if (mainController != null) {
            BooleanProperty isSheetLoadedProperty = mainController.getIsSheetLoaded();
            rangeComboBox.disableProperty().bind(isSheetLoadedProperty.not());
            removeRangeButton.disableProperty().bind(isSheetLoadedProperty.not());
            selectTopLeftCellButton.disableProperty().bind(isSheetLoadedProperty.not().or(isSelectingTopLeftCell));
            selectBottomRightCellButton.disableProperty().bind(isSheetLoadedProperty.not().or(isSelectingBottomRightCell));
        }
    }

    public void updateMyControlsOnFileLoad() {
        ComboBox<String> comboBox = (ComboBox<String>) rangeComboBox;

        SetProperty<String> rangesNamesSetProperty = mainController.getDataModule().getRangesNames();

        Runnable updateComboBoxItems = () -> {
            Set<String> names = rangesNamesSetProperty.get();
            EventHandler<ActionEvent> originalOnAction = comboBox.getOnAction();
            comboBox.setOnAction(null);
            ObservableList<String> observableList = FXCollections.observableArrayList(names);
            comboBox.setItems(observableList);
            //unfortunately, the "setItems" method invokes the on action method of the version combo box,
            //so the current solution is to nullify the on action before calling "setItems" and then returning its original value
            comboBox.setOnAction(originalOnAction);
        };

        rangesNamesSetProperty.addListener((observable, oldValue, newValue) -> updateComboBoxItems.run());
    }

    public void updateTopLeftCellIDLabel(String text) {
        selectedTopLeftCellLabel.setText(text);
    }
    public void updateBottomRightCellIDLabel(String text) {
        selectedBottomRightCellLabel.setText(text);
    }



    @FXML
    void handleOnRangeSelect(ActionEvent event) {
        String rangeName = rangeComboBox.getSelectionModel().getSelectedItem().toString();
        Range range = mainController.getCurrentLoadedSheet().getRange(rangeName);
        mainController.getCenterController().highlightChosenRangeCells(range);
    }

    @FXML
    void handleOnRemoveRangeButtonClick(ActionEvent event) {

    }

    @FXML
    void handleSelectBottomRightCellButtonClick(ActionEvent event) {
        isSelectingBottomRightCell.set(true);

    }

    @FXML
    void handleSelectTopLeftCellButtonClick(ActionEvent event) {
        isSelectingTopLeftCell.set(true);
    }

    @FXML
    void addRangeButtonOnClick(ActionEvent event) {

    }

}
