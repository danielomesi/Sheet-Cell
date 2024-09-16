package gui.components.sheet;

import entities.cell.Cell;
import entities.coordinates.Coordinates;
import entities.range.Range;
import entities.sheet.Sheet;
import gui.builder.DynamicSheetTable;
import gui.components.sheet.cell.CellController;
import gui.components.sheet.cell.TableCellType;
import gui.components.main.MainController;
import gui.core.DataModule;
import gui.builder.DynamicBuilder;
import gui.utils.Utils;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.Map;

public class SheetController {

    private SimpleObjectProperty<CellController> selectedCellController;
    private SimpleObjectProperty<CellController> previousSelectedCellController;
    private DynamicSheetTable dynamicSheetTable;

    @FXML
    private Label currentCellIDLabel;

    @FXML
    private Label lastUpdatedVersionLabel;

    @FXML
    private TextField newValueTextField;

    @FXML
    private Label originalValueLabel;


    @FXML
    private Button selectCellsButton;

    @FXML
    private Label selectedBottomRightCellLabel;

    @FXML
    private Label selectedTopLeftCellLabel;
    @FXML
    private Label cellsSelectErrorMessageLabel;

    @FXML
    private ScrollPane sheetWrapperScrollPane;

    @FXML
    private Button updateButton;

    @FXML
    private ComboBox<Integer> versionComboBox;


    private MainController mainController;
    private final BooleanProperty isSelectingFirstCell = new SimpleBooleanProperty(false);
    private final BooleanProperty isSelectingSecondCell = new SimpleBooleanProperty(false); ;
    private final BooleanProperty is2ValidCellsSelected = new SimpleBooleanProperty(false);

    //getters
    public Coordinates getSelectedCellCoordinates() {return selectedCellController.get() == null ? null : selectedCellController.get().getCoordinates();}
    public SimpleObjectProperty<CellController> getSelectedCellController() {return selectedCellController;}
    public DynamicSheetTable getDynamicSheetTable() {return dynamicSheetTable;}
    public String getSelectedTopLeftCellID() {return selectedTopLeftCellLabel.getText();}
    public String getSelectedBottomRightCellID() {return selectedBottomRightCellLabel.getText();}
    //setters
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        if (mainController != null) {
            SheetController sheetController = mainController.getCenterController();
            BooleanProperty isSheetLoadedProperty = mainController.getIsSheetLoaded();
            updateButton.disableProperty().bind(isSheetLoadedProperty.not().or(sheetController.getSelectedCellController().isNull()));
            versionComboBox.disableProperty().bind(isSheetLoadedProperty.not());
            newValueTextField.disableProperty().bind(isSheetLoadedProperty.not());
            selectCellsButton.disableProperty().bind(isSheetLoadedProperty.not().or(isSelectingFirstCell).
                    or(isSelectingSecondCell));
        }
    }
    public void resetVersionComboBoxChoice() {
        versionComboBox.getSelectionModel().clearSelection();
    }

    public void initialize() {
        selectedCellController = new SimpleObjectProperty<>();
        previousSelectedCellController = new SimpleObjectProperty<>();

        selectedCellController.addListener((observable, oldCellController, newCellController) -> {
            previousSelectedCellController.set(oldCellController);
            if (oldCellController != null) {
                oldCellController.setColorStyle("default-cell");
            }
            if (newCellController != null) {
                newCellController.setColorStyle("selected-cell");
            }
        });
    }

    public void buildMainCellsTableDynamically(Sheet sheet) {
        dynamicSheetTable = DynamicBuilder.buildDynamicSheetTable(sheet);
        GridPane gridPane = dynamicSheetTable.getGridPane();
        Map<Coordinates,CellController> cellControllersMap = dynamicSheetTable.getCoordinates2CellController();
        DataModule dataModule = mainController.getDataModule();

        int numRows = sheet.getNumOfRows();
        int numCols = sheet.getNumOfCols();
        int colWidth = sheet.getLayout().getColumnWidthUnits();
        int rowHeight = sheet.getLayout().getRowHeightUnits();

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                Coordinates coordinates = new Coordinates(row,col);
                Cell cell = sheet.getCell(row, col);
                CellController cellController = cellControllersMap.get(coordinates);
                cellController.bindToModule(dataModule.getCoordinates2EffectiveValues().get(coordinates));
                Label cellLabel = cellController.getCellLabel();
                cellLabel.setOnMouseClicked(event -> handleCellClick(coordinates));
            }
        }


        sheetWrapperScrollPane.setContent(gridPane);
    }



    private void handleCellClick(Coordinates clickedCellCoordinates) {
        CellController cellController = dynamicSheetTable.getCoordinates2CellController().get(clickedCellCoordinates);
        if (cellController != selectedCellController.get()) {
            resetStyles();
            selectedCellController.set(cellController);
            populateActionLineControlsOnCellChoose(clickedCellCoordinates);
            if (cellController.getTableCellType() == TableCellType.DATA && !isRangeChoice(clickedCellCoordinates)) {
                Cell clickedCell = mainController.getCurrentLoadedSheet().
                        getCell(clickedCellCoordinates.getRow(), clickedCellCoordinates.getCol());
                if (clickedCell!= null) {
                    clickedCell.getCellsAffectingMe().forEach(dependentCell ->
                            dynamicSheetTable.getCoordinates2CellController().get(dependentCell).setColorStyle("affecting-cell"));

                    clickedCell.getCellsAffectedByMe().forEach(affectedCell ->
                            dynamicSheetTable.getCoordinates2CellController().get(affectedCell).setColorStyle("affected-cell"));
                }
            }
        }

    }

    private boolean isRangeChoice(Coordinates clickedCellCoordinates) {
        String topLeftCellID, bottomRightCellID;
        boolean res = isSelectingFirstCell.get() || isSelectingSecondCell.get();
        if (isSelectingFirstCell.get()) {
            isSelectingFirstCell.set(false);
        }
        else if (isSelectingSecondCell.get()) {
            if (Utils.compareCoordinates(previousSelectedCellController.get().getCoordinates(), clickedCellCoordinates)) {
                topLeftCellID = previousSelectedCellController.get().getCoordinates().getCellID();
                bottomRightCellID = clickedCellCoordinates.getCellID();
                updateSelectedCellSIDLabel(topLeftCellID, bottomRightCellID);
                highlightChosenRangeCells(mainController.getLeftController().getSelectedRange());
                isSelectingSecondCell.set(false);
                is2ValidCellsSelected.set(true);
            }
            else {
                cellsSelectErrorMessageLabel.setTextFill(Color.RED);
                cellsSelectErrorMessageLabel.setText("A valid choice select is one where the first cell is prior (row-wise and column-wise) from the second");
            }

        }
        return res;
    }

    public void highlightChosenRangeCells(Range range) {
        if (range != null) {
            resetStyles();
            for(Coordinates coordinates : range.getCells()) {
                CellController cellController = dynamicSheetTable.getCoordinates2CellController().get(coordinates);
                cellController.setColorStyle("range-cell");
            }
        }
    }

    private void resetStyles() {
        dynamicSheetTable.getCoordinates2CellController().forEach((c, cellController) -> {cellController.setColorStyle("default-cell");});
    }

    @FXML
    void handleUpdateOnClick(ActionEvent event) {
        Coordinates coordinates = getSelectedCellCoordinates();
        Runnable runnable = () -> {
            mainController.calculateCellUpdate(coordinates,newValueTextField.getText());
            newValueTextField.clear();
        };
        Task<Void> task = mainController.getHeaderController().getTaskFromRunnable(runnable, false);
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    void handleVersionOnChoose(ActionEvent event) {
        int chosenVersion = versionComboBox.getSelectionModel().getSelectedIndex();
        if (chosenVersion >= 0 && chosenVersion<versionComboBox.getItems().size() - 1) {
            mainController.generateVersionWindow(chosenVersion);
        }

    }

    public void updateMyControlsOnFileLoad() {
        ComboBox<Integer> integerComboBox = (ComboBox<Integer>) versionComboBox;
        SimpleIntegerProperty simpleIntegerProperty = mainController.getDataModule().getVersionNumber();

        Runnable updateComboBoxItems = () -> {
            int maxValue = simpleIntegerProperty.get();
            EventHandler<ActionEvent> originalOnAction = integerComboBox.getOnAction();
            integerComboBox.setOnAction(null);
            integerComboBox.setItems(FXCollections.observableArrayList(
                    java.util.stream.IntStream.rangeClosed(1, maxValue).boxed().toList()));
            //unfortunately, the "setItems" method invokes the on action method of the version combo box,
            //so the current solution is to nullify the on action before calling "setItems" and then returning its original value
            integerComboBox.setOnAction(originalOnAction);
        };

        simpleIntegerProperty.addListener((observable, oldValue, newValue) -> updateComboBoxItems.run());
    }

    public void populateActionLineControlsOnCellChoose(Coordinates cellCoordinates) {
        String cellID = cellCoordinates.getCellID();
        currentCellIDLabel.setText(cellID);
        Cell cell = mainController.getCurrentLoadedSheet().getCell(cellCoordinates.getRow(), cellCoordinates.getCol());
        String originalExpression = cell != null ? cell.getOriginalExpression() : "[EMPTY-CELL]";
        int lastUpdatedVersion = cell != null ? cell.getVersion() : 0;
        originalValueLabel.setText(originalExpression);
        lastUpdatedVersionLabel.setText(String.valueOf(lastUpdatedVersion));
    }

    @FXML
    void handleSelectCellsButtonClick(ActionEvent event) {
        is2ValidCellsSelected.set(false);
        isSelectingFirstCell.set(true);
        isSelectingSecondCell.set(true);
    }

    public void updateSelectedCellSIDLabel(String topLeftText, String bottomRightText) {
        selectedTopLeftCellLabel.setText(topLeftText);
        selectedBottomRightCellLabel.setText(bottomRightText);
    }
}
