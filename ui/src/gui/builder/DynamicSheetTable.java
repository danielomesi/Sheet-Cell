package gui.builder;

import entities.cell.Cell;
import entities.coordinates.Coordinates;
import entities.sheet.Sheet;
import gui.components.center.cell.CellController;
import gui.utils.Utils;
import javafx.scene.layout.GridPane;

import java.util.HashMap;
import java.util.Map;

public class DynamicSheetTable {
    private Map<Coordinates, CellController> coordinates2CellController;
    private GridPane gridPane;

    public DynamicSheetTable(Map<Coordinates, CellController> coordinates2CellController, GridPane gridPane) {
        this.coordinates2CellController = coordinates2CellController;
        this.gridPane = gridPane;
    }

    public Map<Coordinates, CellController> getCoordinates2CellController() {return coordinates2CellController;}
    public GridPane getGridPane() {return gridPane;}

    public void populateSheetWithData(Sheet sheet) {
        int numOfRows = sheet.getNumOfRows();
        int numOfColumns = sheet.getNumOfColumns();
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfColumns; j++) {
                Coordinates coordinates = new Coordinates(i, j);
                CellController cellController = coordinates2CellController.get(coordinates);
                Cell cell = sheet.getCell(i, j);
                Object effectiveValue = cell != null ? cell.getEffectiveValue() : null;
                cellController.setCellLabel(Utils.objectToString(effectiveValue));
            }
        }
    }
}
