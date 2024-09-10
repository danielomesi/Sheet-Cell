package gui.components.center;

import entities.cell.Cell;
import entities.coordinates.Coordinates;
import entities.sheet.Sheet;
import gui.builder.DynamicSheetTable;
import gui.components.center.cell.CellController;
import gui.components.center.cell.TableCellType;
import gui.components.main.MainController;
import gui.core.DataModule;
import gui.builder.DynamicBuilder;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.util.HashMap;
import java.util.Map;

public class CenterController {

    private BooleanProperty isSelectedCell;
    private Coordinates selectedCell;

    @FXML
    private ScrollPane centerScrollPane;
    private MainController mainController;
    private Map<Coordinates, CellController> cellControllersMap;

    public void initialize() {
        cellControllersMap = new HashMap<>();
        isSelectedCell = new SimpleBooleanProperty(false);
    }

    public BooleanProperty isSelectedCellProperty() {return isSelectedCell;}
    public Coordinates getSelectedCell() {return selectedCell;}

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


    private void handleCellClick(Coordinates clickedCellCoordinates) {
        isSelectedCell.setValue(true);
        selectedCell = clickedCellCoordinates;
        resetStyles();
        CellController cellController = cellControllersMap.get(clickedCellCoordinates);
        cellController.replaceStyleClass("default-cell","selected-cell");
        if (cellController.getTableCellType() == TableCellType.DATA) {
            Cell clickedCell = mainController.getCurrentLoadedSheet().getCell(clickedCellCoordinates.getRow(), clickedCellCoordinates.getCol());
            if (clickedCell!= null) {

                clickedCell.getCellsAffectingMe().forEach(dependentCell ->
                        cellControllersMap.get(dependentCell).replaceStyleClass("default-cell","affecting-cell"));

                clickedCell.getCellsAffectedByMe().forEach(affectedCell ->
                        cellControllersMap.get(affectedCell).replaceStyleClass("default-cell","affected-cell"));
                mainController.populateChosenCellDataInHeader(clickedCell);
            }
        }
    }

    private void resetStyles() {
        cellControllersMap.forEach((c, cellController) -> {
            cellController.removeStyleClass("selected-cell");
            cellController.removeStyleClass("affecting-cell");
            cellController.removeStyleClass("affected-cell");
            cellController.addStyleClass("default-cell");
        });
    }
}
