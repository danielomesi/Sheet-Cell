package gui.components.center;

import entities.cell.Cell;
import entities.coordinates.Coordinates;
import entities.sheet.Sheet;
import gui.components.center.cell.CellController;
import gui.components.center.cell.TableCellType;
import gui.components.main.MainController;
import gui.utils.Utils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CenterController {

    @FXML
    private AnchorPane centerAnchorPane;
    private MainController mainController;
    private Map<Coordinates, CellController> cellControllersMap;

    public void initialize() {
        cellControllersMap = new HashMap<>();
    }

    public void setMainController(MainController mainController) {this.mainController = mainController;}

    public void buildCellsTableDynamically(Sheet sheet) {
        GridPane gridPane = new GridPane();

        int numRows = sheet.getNumOfRows();
        int numCols = sheet.getNumOfColumns();

        // Set column headers (A, B, C, ...)
        for (int col = 1; col <= numCols; col++) {
            String charRepresentingColumn = String.valueOf((char) ('A' + col - 1)); // Convert to A, B, C...
            CellController columnHeaderCellController = createCellController();
            columnHeaderCellController.setCellLabel(charRepresentingColumn);
            columnHeaderCellController.setTableCellType(TableCellType.ROW_HEAD);
            StackPane headerPane = (StackPane) columnHeaderCellController.getCellStackPane();
            gridPane.add(headerPane, col, 0); // Add to the first row (row 0)

        }

        // Set row headers (1, 2, 3, ...)
        for (int row = 1; row <= numRows; row++) {
            String rowNumber = String.valueOf(row);
            CellController rowHeaderCellController = createCellController();
            rowHeaderCellController.setCellLabel(rowNumber);
            rowHeaderCellController.setTableCellType(TableCellType.ROW_HEAD);
            StackPane rowPane = (StackPane) rowHeaderCellController.getCellStackPane();
            gridPane.add(rowPane, 0, row); // Add to the first column (col 0)
        }

        // Populate the cells with data
        for (int row = 1; row <= numRows; row++) {
            for (int col = 1; col <= numCols; col++) {
                int zeroIndexBasedRow = row-1;
                int zeroIndexBasedCol = col-1;
                Coordinates coordinates = new Coordinates(zeroIndexBasedRow,zeroIndexBasedCol);

                Cell cell = sheet.getCell(zeroIndexBasedRow, zeroIndexBasedCol); // Retrieve the Cell object
                String valueInCell = cell!=null? Utils.objectToString(cell.getEffectiveValue()) : null;
                CellController cellController = createCellController();
                cellControllersMap.put(coordinates, cellController);
                cellController.setCellLabel(valueInCell);
                cellController.setCellCoordinates(coordinates);
                cellController.setTableCellType(TableCellType.DATA);
                StackPane cellPane = (StackPane) cellController.getCellStackPane();
                cellPane.setOnMouseClicked(event -> handleCellClick(coordinates));
                gridPane.add(cellPane, col, row); // Add cell at col, row (1-based)
            }
        }

        // Clear the centerAnchorPane and add the newly created gridPane to it
        centerAnchorPane.getChildren().clear();
        centerAnchorPane.getChildren().add(gridPane);

        // Optionally, anchor the gridPane to all sides of the AnchorPane
        AnchorPane.setTopAnchor(gridPane, 0.0);
        AnchorPane.setBottomAnchor(gridPane, 0.0);
        AnchorPane.setLeftAnchor(gridPane, 0.0);
        AnchorPane.setRightAnchor(gridPane, 0.0);

        gridPane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        gridPane.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    }


    private CellController createCellController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/components/center/cell/cell.fxml"));
            StackPane pane = loader.load();
            CellController controller = loader.getController();
            return controller;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void handleCellClick(Coordinates clickedCellCoordinates) {
        resetStyles();
        CellController cellController = cellControllersMap.get(clickedCellCoordinates);
        if (cellController.getTableCellType() == TableCellType.DATA) {
            Cell clickedCell = mainController.getCurrentLoadedSheet().getCell(clickedCellCoordinates.getRow(), clickedCellCoordinates.getCol());
            if (clickedCell!= null) {
                // Highlight cells that this cell depends on (light blue)
                clickedCell.getCellsAffectingMe().forEach(dependentCell ->
                        cellControllersMap.get(dependentCell).setBackGroundColor("lightblue"));


                // Highlight cells that depend on this cell (light green)
                clickedCell.getCellsAffectedByMe().forEach(affectedCell ->
                        cellControllersMap.get(affectedCell).setBackGroundColor("lightgreen"));
                mainController.populateChosenCellDataInHeader(clickedCell);
            }
        }
    }

    private void resetStyles() {
        cellControllersMap.forEach((c, cellController) -> {cellController.setStyle("");});
    }
}
