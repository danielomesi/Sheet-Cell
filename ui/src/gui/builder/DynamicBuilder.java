package gui.builder;

import entities.coordinates.Coordinates;
import entities.sheet.Sheet;
import gui.components.center.cell.CellController;
import gui.components.center.cell.TableCellType;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static gui.builder.DynamicSheetTable.FACTOR;

public class DynamicBuilder {
    public static DynamicSheetTable buildDynamicSheetTable(Sheet sheet) {
        GridPane gridPane = new GridPane();
        Map<Coordinates,CellController> coordinates2CellController = new HashMap<>();
        Map<String, ColumnConstraints> columnConstraintsMap = new HashMap<>();
        Map<Integer, RowConstraints> rowConstraintsMap = new HashMap<>();

        int numRows = sheet.getNumOfRows();
        int numCols = sheet.getNumOfColumns();
        int rowHeight = sheet.getLayout().getRowHeightUnits() * FACTOR;
        int colWidth = sheet.getLayout().getColumnWidthUnits() * FACTOR;

        //add the name of the sheet as the top left cell
        CellController sheetNameCellController = createCellController();
        sheetNameCellController.setLabelText(sheet.getName());
        sheetNameCellController.setTableCellType(TableCellType.TABLE_NAME);
        Label topLeftCellLabel = sheetNameCellController.getCellLabel();
        gridPane.add(topLeftCellLabel, 0, 0);
        addRowConstraints(gridPane,rowHeight); //adding blank row constraint for the row presenting the colum names
        addColumnConstraints(gridPane,colWidth); //adding blank column constraint for the column presenting the colum names

        for (int col = 1; col <= numCols; col++) {
            String charRepresentingColumn = String.valueOf((char) ('A' + col - 1));
            CellController columnHeaderCellController = createCellController();
            columnHeaderCellController.setLabelText(charRepresentingColumn);
            columnHeaderCellController.setTableCellType(TableCellType.COL_HEADER);
            Label headerLabel = columnHeaderCellController.getCellLabel();
            gridPane.add(headerLabel, col, 0);
            columnConstraintsMap.put(charRepresentingColumn, addColumnConstraints(gridPane,colWidth));
        }

        for (int row = 1; row <= numRows; row++) {
            String rowNumber = String.valueOf(row);
            CellController rowHeaderCellController = createCellController();
            rowHeaderCellController.setLabelText(rowNumber);
            rowHeaderCellController.setTableCellType(TableCellType.ROW_HEADER);
            Label rowLabel = rowHeaderCellController.getCellLabel();
            gridPane.add(rowLabel, 0, row);
            rowConstraintsMap.put(row, addRowConstraints(gridPane,rowHeight));
        }

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                Coordinates coordinates = new Coordinates(row,col);
                CellController cellController = createCellController();
                coordinates2CellController.put(coordinates, cellController);
                cellController.setCellCoordinates(coordinates);
                cellController.setTableCellType(TableCellType.DATA);
                Label cellLabel = cellController.getCellLabel();
                gridPane.add(cellLabel, col+1, row+1);
            }
        }

        gridPane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        gridPane.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        return new DynamicSheetTable(coordinates2CellController,gridPane,columnConstraintsMap,rowConstraintsMap);
    }

    public static CellController createCellController() {
        try {
            FXMLLoader loader = new FXMLLoader(DynamicBuilder.class.getResource("/gui/components/center/cell/cell.fxml"));
            Label label = loader.load();
            label.setAlignment(Pos.CENTER);
            return loader.getController();
        } catch (IOException e) {
            return null;
        }
    }

    private static ColumnConstraints addColumnConstraints(GridPane gridPane, int width) {
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setMinWidth(width);
        columnConstraints.setPrefWidth(width);
        columnConstraints.setMaxWidth(width);
        gridPane.getColumnConstraints().add(columnConstraints);

        return columnConstraints;
    }

    private static RowConstraints addRowConstraints(GridPane gridPane, int height) {
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setMinHeight(height);
        rowConstraints.setPrefHeight(height);
        rowConstraints.setMaxHeight(height);
        gridPane.getRowConstraints().add(rowConstraints);

        return rowConstraints;
    }
}
