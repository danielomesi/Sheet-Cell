package gui.builder;

import entities.cell.Cell;
import entities.coordinates.Coordinates;
import entities.sheet.Sheet;
import gui.components.center.cell.CellController;
import gui.utils.Utils;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;

import java.util.Map;

import static gui.utils.Utils.convertColumnCharToIndex;

public class DynamicSheetTable {
    private final Map<Coordinates, CellController> coordinates2CellController;
    private final GridPane gridPane;
    private final Map<String, ColumnConstraints> columnConstraintsMap;
    private final Map<Integer, RowConstraints> rowConstraintsMap;

    public DynamicSheetTable(Map<Coordinates, CellController> coordinates2CellController, GridPane gridPane, Map<String, ColumnConstraints> columnConstraintsMap, Map<Integer, RowConstraints> rowConstraintsMap) {
        this.coordinates2CellController = coordinates2CellController;
        this.gridPane = gridPane;
        this.columnConstraintsMap = columnConstraintsMap;
        this.rowConstraintsMap = rowConstraintsMap;
    }

    public Map<Coordinates, CellController> getCoordinates2CellController() {return coordinates2CellController;}
    public GridPane getGridPane() {return gridPane;}
    public Map<String, ColumnConstraints> getColumnConstraintsMap() {return columnConstraintsMap;}
    public Map<Integer, RowConstraints> getRowConstraintsMap() {return rowConstraintsMap;}

    public void populateSheetWithData(Sheet sheet) {
        int numOfRows = sheet.getNumOfRows();
        int numOfColumns = sheet.getNumOfColumns();
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfColumns; j++) {
                Coordinates coordinates = new Coordinates(i, j);
                CellController cellController = coordinates2CellController.get(coordinates);
                Cell cell = sheet.getCell(i, j);
                Object effectiveValue = cell != null ? cell.getEffectiveValue() : null;
                cellController.setLabelText(Utils.objectToString(effectiveValue));
            }
        }
    }

    public void updateColumnWidth(String columnName, double width) {
        ColumnConstraints columnConstraints = columnConstraintsMap.get(columnName);
        columnConstraints.setMinWidth(width);
    }

    public void updateRowHeight(int rowIndex, double height) {
        RowConstraints rowConstraints = rowConstraintsMap.get(rowIndex);
        rowConstraints.setMinHeight(height);
    }

    public void updateColumnAlignment(String columnName, String alignment) {
        alignment = alignment.toLowerCase();
        ColumnConstraints columnConstraints = columnConstraintsMap.get(columnName);
        int colIndex = convertColumnCharToIndex(columnName.charAt(0));
        switch (alignment) {
            case "left":
                columnConstraints.setHalignment(HPos.LEFT);
                break;
            case "center":
                columnConstraints.setHalignment(HPos.CENTER);
                break;
            case "right":
                columnConstraints.setHalignment(HPos.RIGHT);
                break;
        }

        for (Map.Entry<Coordinates, CellController> entry : coordinates2CellController.entrySet()) {
            Coordinates coordinates = entry.getKey();
            if (coordinates.getCol() == colIndex) {
                CellController cellController = entry.getValue();
                Label label = cellController.getCellLabel();
                switch (alignment) {
                    case "left":
                        label.setAlignment(Pos.CENTER_LEFT);
                        break;
                    case "center":
                        label.setAlignment(Pos.CENTER);
                        break;
                    case "right":
                        label.setAlignment(Pos.CENTER_RIGHT);
                        break;
                }
            }
        }
    }
}
