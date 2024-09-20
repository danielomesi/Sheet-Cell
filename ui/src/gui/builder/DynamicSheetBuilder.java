package gui.builder;

import entities.coordinates.CoordinateFactory;
import entities.coordinates.Coordinates;
import entities.sheet.Sheet;
import gui.components.sheet.cell.CellController;
import gui.components.sheet.cell.TableCellType;
import gui.exceptions.RowOutOfBoundsException;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static gui.builder.DynamicSheet.FACTOR;

public class DynamicSheetBuilder {
    public static DynamicSheet buildDynamicSheet(Sheet sheet) {
        GridPane gridPane = new GridPane();
        Map<Coordinates,CellController> coordinates2CellController = new HashMap<>();
        Map<String, ColumnConstraints> columnConstraintsMap = new HashMap<>();
        Map<Integer, RowConstraints> rowConstraintsMap = new HashMap<>();
        Map<Integer, CellController> integer2RowCellController = new HashMap<>();
        Map<String, CellController> string2ColCellController = new HashMap<>();

        int numRows = sheet.getNumOfRows();
        int numCols = sheet.getNumOfCols();
        int rowHeight = sheet.getLayout().getRowHeightUnits() * FACTOR;
        int colWidth = sheet.getLayout().getColumnWidthUnits() * FACTOR;

        initRowAndColumnHeaders(sheet.getName(), gridPane,numRows,numCols,rowHeight,colWidth,columnConstraintsMap,
                rowConstraintsMap,integer2RowCellController,string2ColCellController);

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

        setGridPaneSize(gridPane);

        return new DynamicSheet(coordinates2CellController,gridPane,columnConstraintsMap,rowConstraintsMap,
                integer2RowCellController,string2ColCellController);
    }

    public static DynamicSheet buildSubDynamicSheetFromMainSheet(Sheet mainSheet, DynamicSheet mainDynamicSheet, String fromCellID, String toCellID) {
        GridPane gridPane = new GridPane();
        Map<Coordinates,CellController> coordinates2CellController = new HashMap<>();
        Map<String, ColumnConstraints> columnConstraintsMap = new HashMap<>();
        Map<Integer, RowConstraints> rowConstraintsMap = new HashMap<>();
        Map<Integer, CellController> integer2RowCellController = new HashMap<>();
        Map<String, CellController> string2ColCellController = new HashMap<>();

        int rowStart = CoordinateFactory.getRowIndexFromCellID(fromCellID);
        int colStart = CoordinateFactory.getColIndexFromCellID(fromCellID);
        int rowEnd = CoordinateFactory.getRowIndexFromCellID(toCellID);
        int colEnd = CoordinateFactory.getColIndexFromCellID(toCellID);
        int numRows = rowEnd - rowStart + 1;
        int numCols = colEnd - colStart + 1;
        int rowHeight = mainSheet.getLayout().getRowHeightUnits() * FACTOR;
        int colWidth = mainSheet.getLayout().getColumnWidthUnits() * FACTOR;

        initRowAndColumnHeaders(mainSheet.getName(), gridPane,numRows,numCols,rowHeight,colWidth,columnConstraintsMap,
                rowConstraintsMap,integer2RowCellController,string2ColCellController);
        addSuffixToColNames(string2ColCellController,colStart + 1);

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

        setGridPaneSize(gridPane);

        return new DynamicSheet(coordinates2CellController,gridPane,columnConstraintsMap,rowConstraintsMap,
               integer2RowCellController,string2ColCellController );
    }

    public static DynamicSheet buildFilteredDynamicSheetFromMainSheetAndSubDynamicSheet(Sheet mainSheet, DynamicSheet subDynamicSheet, String fromCellID, String toCellID, Set<Integer> rowsToInclude) {
        GridPane gridPane = new GridPane();
        Map<Coordinates,CellController> coordinates2CellController = new HashMap<>();
        Map<String, ColumnConstraints> columnConstraintsMap = new HashMap<>();
        Map<Integer, RowConstraints> rowConstraintsMap = new HashMap<>();
        Map<Integer, CellController> integer2RowCellController = new HashMap<>();
        Map<String, CellController> string2ColCellController = new HashMap<>();
        int numOfRowsAdded = 0;

        int colStart = CoordinateFactory.getColIndexFromCellID(fromCellID);
        int numRows = rowsToInclude.size();
        int numCols = subDynamicSheet.getGridPane().getColumnCount()-1;
        int rowHeight = mainSheet.getLayout().getRowHeightUnits() * FACTOR;
        int colWidth = mainSheet.getLayout().getColumnWidthUnits() * FACTOR;

        initRowAndColumnHeaders(mainSheet.getName(), gridPane,numRows,numCols,rowHeight,colWidth,columnConstraintsMap,
                rowConstraintsMap,integer2RowCellController,string2ColCellController);
        addSuffixToColNames(string2ColCellController,colStart + 1);

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

        setGridPaneSize(gridPane);

        return new DynamicSheet(coordinates2CellController,gridPane,columnConstraintsMap,rowConstraintsMap,
                integer2RowCellController,string2ColCellController );
    }

    public static DynamicSheet buildSortedDynamicSheetFromMainSheetAndSubDynamicSheet(Sheet mainSheet, DynamicSheet subDynamicSheet, String fromCellID, String toCellID, List<Integer> rowOrder) {
        GridPane gridPane = new GridPane();
        Map<Coordinates,CellController> coordinates2CellController = new HashMap<>();
        Map<String, ColumnConstraints> columnConstraintsMap = new HashMap<>();
        Map<Integer, RowConstraints> rowConstraintsMap = new HashMap<>();
        Map<Integer, CellController> integer2RowCellController = new HashMap<>();
        Map<String, CellController> string2ColCellController = new HashMap<>();

        int colStart = CoordinateFactory.getColIndexFromCellID(fromCellID);
        int numRows = subDynamicSheet.getGridPane().getRowCount()-1;
        int numCols = subDynamicSheet.getGridPane().getColumnCount()-1;
        int rowHeight = mainSheet.getLayout().getRowHeightUnits() * FACTOR;
        int colWidth = mainSheet.getLayout().getColumnWidthUnits() * FACTOR;

        initRowAndColumnHeaders(mainSheet.getName(), gridPane,numRows,numCols,rowHeight,colWidth,columnConstraintsMap,
                rowConstraintsMap,integer2RowCellController,string2ColCellController);
        addSuffixToColNames(string2ColCellController,colStart + 1);

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

        setGridPaneSize(gridPane);

        return new DynamicSheet(coordinates2CellController,gridPane,columnConstraintsMap,rowConstraintsMap,
                integer2RowCellController,string2ColCellController );
    }

    private static void initRowAndColumnHeaders(String sheetName, GridPane gridPane,
                                                int numRows, int numCols, int rowHeight, int colWidth,
                                                Map<String,ColumnConstraints> columnConstraintsMap,
                                                Map<Integer,RowConstraints> rowConstraintsMap,
                                                Map<Integer,CellController> integer2RowCellController,
                                                Map<String, CellController> string2ColCellController) {
        //add the name of the sheet as the top left cell
        CellController sheetNameCellController = createCellController();
        sheetNameCellController.setLabelText(sheetName);
        sheetNameCellController.setTableCellType(TableCellType.TABLE_NAME);
        Label topLeftCellLabel = sheetNameCellController.getLabel();
        gridPane.add(topLeftCellLabel, 0, 0);
        rowConstraintsMap.put(0,addRowConstraints(gridPane,rowHeight));
        columnConstraintsMap.put(DynamicSheet.HEADER,addColumnConstraints(gridPane,colWidth));


        for (int col = 1; col <= numCols; col++) {
            String charRepresentingColumn = String.valueOf((char) ('A' + col - 1));
            CellController columnHeaderCellController = createCellController();
            columnHeaderCellController.setLabelText(charRepresentingColumn);
            columnHeaderCellController.setTableCellType(TableCellType.COL_HEADER);
            Label headerLabel = columnHeaderCellController.getLabel();
            gridPane.add(headerLabel, col, 0);
            columnConstraintsMap.put(charRepresentingColumn, addColumnConstraints(gridPane,colWidth));
            string2ColCellController.put(charRepresentingColumn, columnHeaderCellController);
        }

        for (int row = 1; row <= numRows; row++) {
            String rowNumber = String.valueOf(row);
            CellController rowHeaderCellController = createCellController();
            rowHeaderCellController.setLabelText(rowNumber);
            rowHeaderCellController.setTableCellType(TableCellType.ROW_HEADER);
            Label rowLabel = rowHeaderCellController.getLabel();
            gridPane.add(rowLabel, 0, row);
            rowConstraintsMap.put(row, addRowConstraints(gridPane,rowHeight));
            integer2RowCellController.put(row - 1,rowHeaderCellController);
        }
    }

    private static void addSuffixToColNames(Map<String,CellController> colHeadersCellControllers, int colLenBetweenSubSheetToMasterSheet) {
        colHeadersCellControllers.forEach((rowNumber1Indexed,cellController)->{
            int colNum = CoordinateFactory.getColIndexFromCellID(rowNumber1Indexed) - 1; //make zero-indexed
            int suffixedColIndex = colNum + colLenBetweenSubSheetToMasterSheet;
            String suffixedCol = CoordinateFactory.numberToLetter(suffixedColIndex);
            String originalCol = cellController.getLabelText();
            String newDisplayedColName = String.format("%s [%s]", originalCol, suffixedCol);
            cellController.setLabelText(newDisplayedColName);
        });
    }


    public static CellController createCellController() {
        try {
            FXMLLoader loader = new FXMLLoader(DynamicSheetBuilder.class.getResource("/gui/components/sheet/cell/cell.fxml"));
            Label label = loader.load();
            label.setWrapText(true);
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

    private static void setGridPaneSize(GridPane gridPane) {
        gridPane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        gridPane.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
    }

    private static int getIndexOfTheRowInITHPlace(List<Integer> rowsOrder, int i) {
        for (int row = 0; row < rowsOrder.size(); row++) {
            if (rowsOrder.get(row) == i) {
                return row;
            }
        }
        throw new RowOutOfBoundsException("Row out of bounds");
    }


}
