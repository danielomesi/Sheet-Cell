package gui.builder;

import entities.cell.Cell;
import entities.coordinates.Coordinates;
import entities.sheet.Sheet;
import gui.components.center.cell.CellController;
import gui.utils.Utils;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;

import java.util.Map;

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
        columnConstraints.setPrefWidth(width);
        columnConstraints.setMaxWidth(width);
    }

    public void updateRowHeight(int rowIndex, double height) {
        RowConstraints rowConstraints = rowConstraintsMap.get(rowIndex);
        rowConstraints.setMinHeight(height);
        rowConstraints.setPrefHeight(height);
        rowConstraints.setMaxHeight(height);
    }

    public void updateColumnAlignment(String columnName, String alignment) {
        alignment = alignment.toLowerCase();
        ColumnConstraints columnConstraints = columnConstraintsMap.get(columnName);
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

    }
}
