package gui.components.appearance;

import gui.components.sheet.cell.CellController;
import gui.components.main.MainController;
import gui.utils.Utils;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.util.List;

public class AppearanceController {

    private MainController mainController;
    private final int CELL_SIZE_SCALER = 10;
    private final List<String> ALLIGNMENT_OPTIONS = List.of("Center", "Left", "Right");
    private static final double MIN_SCALE = 0.1;
    private static final double MAX_SCALE = 5;


    @FXML
    private ToggleButton animationsToggleButton;
    @FXML
    private Slider sheetScalerSlider;
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

    //getters
    public boolean isAnimationsEnabled() {return animationsToggleButton.isSelected();}

    //setters
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        if (mainController != null) {
            BooleanProperty isSheetLoadedProperty = mainController.getIsSheetLoaded();
            BooleanBinding isSelectedCell = mainController.getSheetController().getSelectedCellController().isNull();
            sheetScalerSlider.disableProperty().bind(isSheetLoadedProperty.not());
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

        animationsToggleButton.setSelected(false);
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
        int numOfCols = mainController.getCurrentLoadedSheet().getNumOfCols();

        //populate combo boxes of row and column
        List<Integer> rowNumbers = Utils.getNumbersFrom1ToNNumber(numOfRows);
        List<String> colNames = Utils.getLettersFromAToTheNLetter(numOfCols);
        selectedRowComboBox.getItems().clear();
        selectedRowComboBox.getItems().addAll(rowNumbers);
        selectedColComboBox.getItems().clear();
        selectedColComboBox.getItems().addAll(colNames);

        setRangesForSliders();
        bindAlignmentComboBoxToColComboBox();

        sheetScalerSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            mainController.getSheetController().getDynamicSheetTable().updateSheetScale((Double) newValue);
        });
        rowHeightSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int rowToUpdate = selectedRowComboBox.getValue();
            mainController.getSheetController().getDynamicSheetTable().updateRowHeight(rowToUpdate, (Double) newValue);});

        colWidthSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            String colToUpdate = selectedColComboBox.getValue();
            mainController.getSheetController().getDynamicSheetTable().updateColumnWidth(colToUpdate, (Double) newValue);});
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
        sheetScalerSlider.setValue(1);
        sheetScalerSlider.setMin(MIN_SCALE);
        sheetScalerSlider.setMax(MAX_SCALE);

    }

    private void bindAlignmentComboBoxToColComboBox() {
        selectedAlignmentComboBox.disableProperty().bind(Bindings.isNull(selectedColComboBox.getSelectionModel().selectedItemProperty()));
    }

    @FXML
    void animationsToggleButtonClicked(ActionEvent event) {
        if (animationsToggleButton.isSelected()) {
            animationsToggleButton.setText("ON");
        }
        else {
            animationsToggleButton.setText("OFF");
        }
    }

    @FXML
    public void handleAlignmentOnSelect(ActionEvent event) {
        String alignment = selectedAlignmentComboBox.getValue();
        String columnName = selectedColComboBox.getSelectionModel().getSelectedItem();
        mainController.getSheetController().getDynamicSheetTable().updateColumnAlignment(columnName, alignment);
    }

    @FXML
    void handleCellFontOnColorPick(ActionEvent event) {
        CellController selectedCell = mainController.getSheetController().getSelectedCellController().get();
        Color selectedColor = cellFontColorPicker.getValue();
        selectedCell.setColorStyles(selectedCell.getCustomizedBackgroundColor(), selectedColor);
    }

    @FXML
    void handleCellBackgroundOnColorPick(ActionEvent event) {
        CellController selectedCell = mainController.getSheetController().getSelectedCellController().get();
        Color selectedColor = cellBackgroundColorPicker.getValue();
        selectedCell.setColorStyles(selectedColor, selectedCell.getCustomizedTextColor());
    }

    @FXML
    void handleResetCellColorsOnClick(ActionEvent event) {
        CellController selectedCell = mainController.getSheetController().getSelectedCellController().get();
        selectedCell.resetColorStyles();
    }

}
