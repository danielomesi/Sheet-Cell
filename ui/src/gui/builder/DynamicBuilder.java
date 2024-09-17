package gui.builder;

import entities.coordinates.CoordinateFactory;
import entities.coordinates.Coordinates;
import entities.sheet.Sheet;
import gui.components.sheet.cell.CellController;
import gui.components.sheet.cell.TableCellType;
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
        int numCols = sheet.getNumOfCols();
        int rowHeight = sheet.getLayout().getRowHeightUnits() * FACTOR;
        int colWidth = sheet.getLayout().getColumnWidthUnits() * FACTOR;

        initRowAndColumnHeaders(sheet.getName(), gridPane,numRows,numCols,rowHeight,colWidth,columnConstraintsMap,rowConstraintsMap);

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                Coordinates coordinates = new Coordinates(row,col);
                CellController cellController = createCellController();
                coordinates2CellController.put(coordinates, cellController);
                cellController.setCellCoordinates(coordinates);
                cellController.setTableCellType(TableCellType.DATA);
                Label cellLabel = cellController.getLabel();
                gridPane.add(cellLabel, col+1, row+1);
            }
        }

        gridPane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        gridPane.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        return new DynamicSheetTable(coordinates2CellController,gridPane,columnConstraintsMap,rowConstraintsMap);
    }

    public static DynamicSheetTable cropDynamicSheetTableToANewOne(Sheet originalSheet, DynamicSheetTable originalDynamicSheetTable, String fromCellID, String toCellID) {
        GridPane gridPane = new GridPane();
        Map<Coordinates,CellController> coordinates2CellController = new HashMap<>();
        Map<String, ColumnConstraints> columnConstraintsMap = new HashMap<>();
        Map<Integer, RowConstraints> rowConstraintsMap = new HashMap<>();

        int rowStart = CoordinateFactory.getRowIndexFromCellID(fromCellID);
        int colStart = CoordinateFactory.getColIndexFromCellID(fromCellID);
        int rowEnd = CoordinateFactory.getRowIndexFromCellID(toCellID);
        int colEnd = CoordinateFactory.getColIndexFromCellID(toCellID);
        int numRows = rowEnd - rowStart + 1;
        int numCols = colEnd - colStart + 1;
        int rowHeight = originalSheet.getLayout().getRowHeightUnits() * FACTOR;
        int colWidth = originalSheet.getLayout().getColumnWidthUnits() * FACTOR;

        initRowAndColumnHeaders(originalSheet.getName(), gridPane,numRows,numCols,rowHeight,colWidth,columnConstraintsMap,rowConstraintsMap);

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                Coordinates coordinates = new Coordinates(rowStart + row,colStart + col);
                CellController originalcellController = originalDynamicSheetTable.getCoordinates2CellController().get(coordinates);
                CellController newCellController = createCellController();
                newCellController.copyFrom(originalcellController);
                newCellController.setCellCoordinates(new Coordinates(row,col));
                coordinates2CellController.put(coordinates, newCellController);
                Label cellLabel = newCellController.getLabel();
                gridPane.add(cellLabel, col+1, row+1);
            }
        }

        gridPane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        gridPane.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        return new DynamicSheetTable(coordinates2CellController,gridPane,columnConstraintsMap,rowConstraintsMap);
    }

    private static void initRowAndColumnHeaders(String sheetName, GridPane gridPane,
                                                 int numRows, int numCols,int rowHeight, int colWidth,
                                            Map<String,ColumnConstraints> columnConstraintsMap,
                                                 Map<Integer,RowConstraints> rowConstraintsMap) {
        //add the name of the sheet as the top left cell
        CellController sheetNameCellController = createCellController();
        sheetNameCellController.setLabelText(sheetName);
        sheetNameCellController.setTableCellType(TableCellType.TABLE_NAME);
        Label topLeftCellLabel = sheetNameCellController.getLabel();
        gridPane.add(topLeftCellLabel, 0, 0);
        rowConstraintsMap.put(0,addRowConstraints(gridPane,rowHeight));
        columnConstraintsMap.put(DynamicSheetTable.HEADER,addColumnConstraints(gridPane,colWidth));

        for (int col = 1; col <= numCols; col++) {
            String charRepresentingColumn = String.valueOf((char) ('A' + col - 1));
            CellController columnHeaderCellController = createCellController();
            columnHeaderCellController.setLabelText(charRepresentingColumn);
            columnHeaderCellController.setTableCellType(TableCellType.COL_HEADER);
            Label headerLabel = columnHeaderCellController.getLabel();
            gridPane.add(headerLabel, col, 0);
            columnConstraintsMap.put(charRepresentingColumn, addColumnConstraints(gridPane,colWidth));
        }

        for (int row = 1; row <= numRows; row++) {
            String rowNumber = String.valueOf(row);
            CellController rowHeaderCellController = createCellController();
            rowHeaderCellController.setLabelText(rowNumber);
            rowHeaderCellController.setTableCellType(TableCellType.ROW_HEADER);
            Label rowLabel = rowHeaderCellController.getLabel();
            gridPane.add(rowLabel, 0, row);
            rowConstraintsMap.put(row, addRowConstraints(gridPane,rowHeight));
        }
    }

    public static CellController createCellController() {
        try {
            FXMLLoader loader = new FXMLLoader(DynamicBuilder.class.getResource("/gui/components/sheet/cell/cell.fxml"));
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
