package gui.components.right;

import gui.components.center.cell.CellController;
import gui.components.main.MainController;
import gui.utils.Utils;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;

import java.util.List;

public class RightController {

    private MainController mainController;
    private final int CELL_SIZE_SCALER = 10;
    private final List<String> ALLIGNMENT_OPTIONS = List.of("Center", "Left", "Right");

    @FXML
    private Slider colWidthSlider;
    @FXML
    private Slider rowHeightSlider;
    @FXML
    private ComboBox<String> selectedColComboBox;
    @FXML
    private ComboBox<Integer> selectedRowComboBox;
    @FXML
    private ComboBox<String> selectedAlignmentComboBox;
    @FXML
    private ColorPicker cellBackgroundColorPicker;
    @FXML
    private ColorPicker cellFontColorPicker;
    @FXML
    private Button resetCellColorsButtons;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        if (mainController != null) {
            BooleanProperty isSheetLoadedProperty = mainController.getIsSheetLoaded();
            BooleanBinding isSelectedCell = mainController.getCenterController().getSelectedCellController().isNull();
            selectedRowComboBox.disableProperty().bind(isSheetLoadedProperty.not());
            selectedColComboBox.disableProperty().bind(isSheetLoadedProperty.not());
            selectedAlignmentComboBox.disableProperty().bind(isSheetLoadedProperty.not());
            cellBackgroundColorPicker.disableProperty().bind(isSelectedCell);
            cellFontColorPicker.disableProperty().bind(isSelectedCell);
            resetCellColorsButtons.disableProperty().bind(isSelectedCell);
        }
    }

    public void initialize() {
        BooleanBinding isRowComboBoxEmpty = Bindings.createBooleanBinding(
                () -> selectedRowComboBox.getSelectionModel().isEmpty(),
                selectedRowComboBox.getSelectionModel().selectedItemProperty());
        BooleanBinding isColComboBoxEmpty = Bindings.createBooleanBinding(
                () -> selectedColComboBox.getSelectionModel().isEmpty(),
                selectedColComboBox.getSelectionModel().selectedItemProperty());

        rowHeightSlider.disableProperty().bind(isRowComboBoxEmpty);
        colWidthSlider.disableProperty().bind(isColComboBoxEmpty);
        ObservableList<String> observableList = FXCollections.observableArrayList(ALLIGNMENT_OPTIONS);
        selectedAlignmentComboBox.setItems(observableList);
    }

    public void updateMyControlsOnFileLoad() {
       updateSlidersOnFileLoad();
    }

    private void updateSlidersOnFileLoad() {
        int numOfRows = mainController.getCurrentLoadedSheet().getNumOfRows();
        int numOfCols = mainController.getCurrentLoadedSheet().getNumOfColumns();

        //populate combo boxes of row and column
        List<Integer> rowNumbers = Utils.getNumbersFrom1ToNNumber(numOfRows);
        List<String> colNames = Utils.getLettersFromAToTheNLetter(numOfCols);
        selectedRowComboBox.getItems().clear();
        selectedRowComboBox.getItems().addAll(rowNumbers);
        selectedColComboBox.getItems().clear();
        selectedColComboBox.getItems().addAll(colNames);

        setRangesForSliders();
        bindAlignmentComboBoxToColComboBox();


        //add listener to the row height slider
        rowHeightSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int rowToUpdate = selectedRowComboBox.getValue();
            mainController.getCenterController().getDynamicSheetTable().updateRowHeight(rowToUpdate, (Double) newValue);});

        colWidthSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            String colToUpdate = selectedColComboBox.getValue();
            mainController.getCenterController().getDynamicSheetTable().updateColumnWidth(colToUpdate, (Double) newValue);});
    }

    private void setRangesForSliders() {
        double rowHeight = mainController.getCurrentLoadedSheet().getLayout().getRowHeightUnits();
        double colWidth = mainController.getCurrentLoadedSheet().getLayout().getColumnWidthUnits();

        rowHeightSlider.setValue(rowHeight);
        rowHeightSlider.setMin(rowHeight/CELL_SIZE_SCALER);
        rowHeightSlider.setMax(rowHeight*CELL_SIZE_SCALER);
        colWidthSlider.setValue(colWidth);
        colWidthSlider.setMin(colWidth/CELL_SIZE_SCALER);
        colWidthSlider.setMax(colWidth*CELL_SIZE_SCALER);

    }

    private void bindAlignmentComboBoxToColComboBox() {
        selectedAlignmentComboBox.disableProperty().bind(Bindings.isNull(selectedColComboBox.getSelectionModel().selectedItemProperty()));
    }

    @FXML
    public void handleAlignmentOnSelect(ActionEvent event) {
        String alignment = selectedAlignmentComboBox.getValue();
        String columnName = selectedColComboBox.getSelectionModel().getSelectedItem();
        mainController.getCenterController().getDynamicSheetTable().updateColumnAlignment(columnName, alignment);
    }

    @FXML
    void handleCellFontOnColorPick(ActionEvent event) {
        CellController selectedCell = mainController.getCenterController().getSelectedCellController().get();
        Color selectedColor = cellFontColorPicker.getValue();
        selectedCell.setColorStyles(selectedCell.getCustomizedBackgroundColor(), selectedColor);
    }

    @FXML
    void handleCellBackgroundOnColorPick(ActionEvent event) {
        CellController selectedCell = mainController.getCenterController().getSelectedCellController().get();
        Color selectedColor = cellBackgroundColorPicker.getValue();
        selectedCell.setColorStyles(selectedColor, selectedCell.getCustomizedTextColor());
    }

    @FXML
    void handleResetCellColorsOnClick(ActionEvent event) {
        CellController selectedCell = mainController.getCenterController().getSelectedCellController().get();
        selectedCell.resetColorStyles();
    }

}
