package gui.components.center;

import entities.cell.Cell;
import entities.coordinates.Coordinates;
import entities.range.Range;
import entities.sheet.Sheet;
import gui.builder.DynamicSheetTable;
import gui.components.center.cell.CellController;
import gui.components.center.cell.TableCellType;
import gui.components.main.MainController;
import gui.core.DataModule;
import gui.builder.DynamicBuilder;
import gui.utils.Utils;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.HashMap;
import java.util.Map;

public class CenterController {

    private SimpleObjectProperty<CellController> selectedCellController;
    private SimpleObjectProperty<CellController> previousSelectedCellController;

    @FXML
    private ScrollPane centerScrollPane;
    private MainController mainController;
    private Map<Coordinates, CellController> cellControllersMap;

    public void initialize() {
        cellControllersMap = new HashMap<>();
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

    public void setMainController(MainController mainController) {this.mainController = mainController;}

    public void buildMainCellsTableDynamically(Sheet sheet) {
        DynamicSheetTable dynamicSheetTable = DynamicBuilder.buildDynamicSheetTable(sheet);
        GridPane gridPane = dynamicSheetTable.getGridPane();
        cellControllersMap = dynamicSheetTable.getCoordinates2CellController();
        DataModule dataModule = mainController.getDataModule();

        int numRows = sheet.getNumOfRows();
        int numCols = sheet.getNumOfColumns();

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                Coordinates coordinates = new Coordinates(row,col);
                Cell cell = sheet.getCell(row, col);
                CellController cellController = cellControllersMap.get(coordinates);
                cellController.bindToModule(dataModule.getCoordinates2EffectiveValues().get(coordinates));
                StackPane cellPane = (StackPane) cellController.getCellStackPane();
                cellPane.setOnMouseClicked(event -> handleCellClick(coordinates));
            }
        }

        centerScrollPane.setContent(gridPane);
    }

    public Coordinates getSelectedCellCoordinates() {return selectedCellController.get() == null ? null : selectedCellController.get().getCoordinates();}

    public SimpleObjectProperty<CellController> getSelectedCellController() {return selectedCellController;}

    private void handleCellClick(Coordinates clickedCellCoordinates) {
        CellController cellController = cellControllersMap.get(clickedCellCoordinates);
        if (cellController != selectedCellController.get()) {
            resetStyles();
            selectedCellController.set(cellController);
            mainController.getHeaderController().populateHeaderControlsOnCellChoose(clickedCellCoordinates);
            if (cellController.getTableCellType() == TableCellType.DATA && !isRangeChoice(clickedCellCoordinates)) {
                Cell clickedCell = mainController.getCurrentLoadedSheet().
                        getCell(clickedCellCoordinates.getRow(), clickedCellCoordinates.getCol());
                if (clickedCell!= null) {

                    clickedCell.getCellsAffectingMe().forEach(dependentCell ->
                            cellControllersMap.get(dependentCell).setColorStyle("affecting-cell"));

                    clickedCell.getCellsAffectedByMe().forEach(affectedCell ->
                            cellControllersMap.get(affectedCell).setColorStyle("affected-cell"));
                }
            }
        }

    }

    private boolean isRangeChoice(Coordinates clickedCellCoordinates) {
        BooleanProperty isFirstCellSelected = mainController.getLeftController().getIsSelectingFirstCell();
        BooleanProperty isSecondCellSelected = mainController.getLeftController().getIsSelectingSecondCell();
        String topLeftCellID, bottomRightCellID;
        boolean res = isFirstCellSelected.get() || isSecondCellSelected.get();
        if (isFirstCellSelected.get()) {
            isFirstCellSelected.set(false);
        }
        else if (isSecondCellSelected.get()) {
            if (Utils.compareCoordinates(previousSelectedCellController.get().getCoordinates(), clickedCellCoordinates)) {
                topLeftCellID = previousSelectedCellController.get().getCoordinates().getCellID();
                bottomRightCellID = clickedCellCoordinates.getCellID();
            }
            else {
                topLeftCellID = clickedCellCoordinates.getCellID();
                bottomRightCellID = previousSelectedCellController.get().getCoordinates().getCellID();
            }
            mainController.getLeftController().updateSelectedCellSIDLabel(topLeftCellID, bottomRightCellID);
            highlightChosenRangeCells(mainController.getLeftController().getSelectedRange());
            isSecondCellSelected.set(false);
        }
        return res;
    }

    public void highlightChosenRangeCells(Range range) {
        resetStyles();
        for(Coordinates coordinates : range.getCells()) {
            CellController cellController = cellControllersMap.get(coordinates);
            cellController.setColorStyle("range-cell");
        }
    }

    private void resetStyles() {
        cellControllersMap.forEach((c, cellController) -> {cellController.setColorStyle("default-cell");});
    }
}
