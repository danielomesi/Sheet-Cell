package gui.builder;

import entities.coordinates.CoordinateFactory;
import entities.coordinates.Coordinates;
import entities.sheet.Sheet;
import gui.components.sheet.cell.TableCellType;
import gui.exceptions.RowOutOfBoundsException;
import gui.scenes.workspace.sheet.cell.CellController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static gui.builder.DynamicSheet.FACTOR;

public class DynamicSheetBuilder {
    public static DynamicSheet buildDynamicSheet(Sheet sheet) {
        DynamicSheet newDynamicSheet = new DynamicSheet();
        GridPane gridPane = newDynamicSheet.getGridPane();
        Map<Coordinates, CellController> coordinates2CellController = newDynamicSheet.getCoordinates2CellController();

        int numRows = sheet.getNumOfRows();
        int numCols = sheet.getNumOfCols();
        int rowHeight = sheet.getLayout().getRowHeightUnits() * FACTOR;
        int colWidth = sheet.getLayout().getColumnWidthUnits() * FACTOR;

        newDynamicSheet.setInitialRowAndColLayout(rowHeight,colWidth);
        newDynamicSheet.initRowAndColumnHeaders(sheet.getName(),numRows,numCols);

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

        return newDynamicSheet;
    }

    public static DynamicSheet buildSubDynamicSheetFromMainSheet(Sheet mainSheet, DynamicSheet mainDynamicSheet, String fromCellID, String toCellID) {
        DynamicSheet newDynamicSheet = new DynamicSheet();
        GridPane gridPane = newDynamicSheet.getGridPane();
        Map<Coordinates,CellController> coordinates2CellController = newDynamicSheet.getCoordinates2CellController();

        int rowStart = Coordinates.getRowIndexFromCellID(fromCellID);
        int colStart = Coordinates.getColIndexFromCellID(fromCellID);
        int rowEnd = Coordinates.getRowIndexFromCellID(toCellID);
        int colEnd = Coordinates.getColIndexFromCellID(toCellID);
        int numRows = rowEnd - rowStart + 1;
        int numCols = colEnd - colStart + 1;
        int rowHeight = mainSheet.getLayout().getRowHeightUnits() * FACTOR;
        int colWidth = mainSheet.getLayout().getColumnWidthUnits() * FACTOR;

        newDynamicSheet.setInitialRowAndColLayout(rowHeight,colWidth);

        newDynamicSheet.initRowAndColumnHeaders(mainSheet.getName(),numRows,numCols);
        newDynamicSheet.addSuffixToHeaders(colStart+1,rowStart+1);

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                Coordinates coordinatesInMainSheet = new Coordinates(rowStart + row,colStart + col);
                Coordinates coordinatesInSubSheet = new Coordinates(row,col);
                CellController cellControllerInMainSheet = mainDynamicSheet.getCoordinates2CellController().get(coordinatesInMainSheet);
                CellController newCellController = createCellController();
                newCellController.copyFrom(cellControllerInMainSheet,coordinatesInSubSheet);
                coordinates2CellController.put(coordinatesInSubSheet, newCellController);
                Label cellLabel = newCellController.getLabel();
                gridPane.add(cellLabel, col+1, row+1);
            }
        }

        return newDynamicSheet;
    }

    public static DynamicSheet buildFilteredDynamicSheetFromMainSheetAndSubDynamicSheet(Sheet mainSheet, DynamicSheet subDynamicSheet, String fromCellID, String toCellID, Set<Integer> rowsToInclude) {
        DynamicSheet newDynamicSheet = new DynamicSheet();
        GridPane gridPane = newDynamicSheet.getGridPane();
        Map<Coordinates,CellController> coordinates2CellController = newDynamicSheet.getCoordinates2CellController();

        int numOfRowsAdded = 0;
        int numRows = rowsToInclude.size();
        int numCols = subDynamicSheet.getGridPane().getColumnCount()-1;
        int rowHeight = mainSheet.getLayout().getRowHeightUnits() * FACTOR;
        int colWidth = mainSheet.getLayout().getColumnWidthUnits() * FACTOR;

        newDynamicSheet.setInitialRowAndColLayout(rowHeight,colWidth);

        newDynamicSheet.initRowAndColumnHeaders(mainSheet.getName(),numRows,numCols);

        for (int row : rowsToInclude) {
            for (int col = 0; col < numCols; col++) {
                Coordinates coordinatesInSubSheet = new Coordinates(row,col);
                CellController cellControllerInSubSheet = subDynamicSheet.getCoordinates2CellController().get(coordinatesInSubSheet);
                CellController newCellController = createCellController();
                newCellController.copyFrom(cellControllerInSubSheet,coordinatesInSubSheet);
                coordinates2CellController.put(coordinatesInSubSheet, newCellController);
                Label cellLabel = newCellController.getLabel();
                gridPane.add(cellLabel, col+1, numOfRowsAdded+1);
            }
            numOfRowsAdded++;
        }

        return newDynamicSheet;
    }

    public static DynamicSheet buildSortedDynamicSheetFromMainSheetAndSubDynamicSheet(Sheet mainSheet, DynamicSheet subDynamicSheet, String fromCellID, String toCellID, List<Integer> rowOrder) {
        DynamicSheet newDynamicSheet = new DynamicSheet();
        GridPane gridPane = newDynamicSheet.getGridPane();
        Map<Coordinates,CellController> coordinates2CellController = newDynamicSheet.getCoordinates2CellController();

        int rowStart = Coordinates.getRowIndexFromCellID(fromCellID);
        int colStart = Coordinates.getColIndexFromCellID(fromCellID);
        int numRows = subDynamicSheet.getGridPane().getRowCount()-1;
        int numCols = subDynamicSheet.getGridPane().getColumnCount()-1;
        int rowHeight = mainSheet.getLayout().getRowHeightUnits() * FACTOR;
        int colWidth = mainSheet.getLayout().getColumnWidthUnits() * FACTOR;

        newDynamicSheet.setInitialRowAndColLayout(rowHeight,colWidth);

        newDynamicSheet.initRowAndColumnHeaders(mainSheet.getName(),numRows,numCols);

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                int matchingRowInSubSheet = getIndexOfTheRowInITHPlace(rowOrder,row);
                Coordinates coordinatesInSubSheet = new Coordinates(matchingRowInSubSheet,col);
                CellController cellControllerInSubSheet = subDynamicSheet.getCoordinates2CellController().get(coordinatesInSubSheet);
                CellController newCellController = createCellController();
                newCellController.copyFrom(cellControllerInSubSheet,coordinatesInSubSheet);
                coordinates2CellController.put(coordinatesInSubSheet, newCellController);
                Label cellLabel = newCellController.getLabel();
                gridPane.add(cellLabel, col+1, row+1);
            }
        }

        return newDynamicSheet;
    }

    public static CellController createCellController() {
        try {
            FXMLLoader loader = new FXMLLoader(DynamicSheetBuilder.class.getResource("/gui/scenes/workspace/sheet/cell/cell.fxml"));
            Label label = loader.load();
            label.setWrapText(true);
            label.setAlignment(Pos.CENTER);
            return loader.getController();
        } catch (IOException e) {
            return null;
        }
    }

    public static ColumnConstraints createAddAndGetColumnConstraints(GridPane gridPane, double width) {
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setMinWidth(width);
        columnConstraints.setPrefWidth(width);
        columnConstraints.setMaxWidth(width);
        gridPane.getColumnConstraints().add(columnConstraints);

        return columnConstraints;
    }

    public static RowConstraints createAddAndGetRowConstraints(GridPane gridPane, double height) {
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setMinHeight(height);
        rowConstraints.setPrefHeight(height);
        rowConstraints.setMaxHeight(height);
        gridPane.getRowConstraints().add(rowConstraints);

        return rowConstraints;
    }

    private static int getIndexOfTheRowInITHPlace(List<Integer> rowsOrder, int i) {
        for (int row = 0; row < rowsOrder.size(); row++) {
            if (rowsOrder.get(row) == i)  return row;
        }
        throw new RowOutOfBoundsException("Row out of bounds");
    }
}
