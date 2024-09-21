package gui.builder;

import entities.cell.Cell;
import entities.coordinates.CoordinateFactory;
import entities.coordinates.Coordinates;
import entities.sheet.Sheet;
import gui.components.sheet.cell.CellController;
import gui.components.sheet.cell.TableCellType;
import gui.utils.Utils;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.*;

import static gui.builder.DynamicSheetBuilder.*;
import static gui.utils.Utils.convertColumnCharToIndex;

public class DynamicSheet {
    private final Map<Coordinates, CellController> coordinates2CellController = new HashMap<>();
    private final GridPane gridPane = new GridPane();
    private final Map<String, ColumnConstraints> columnConstraintsMap  = new HashMap<>();
    private final Map<Integer, RowConstraints> rowConstraintsMap  = new HashMap<>();
    private final Map<Integer, CellController> integer2RowCellController = new HashMap<>();
    private final Map<String, CellController> string2ColCellController = new HashMap<>();
    private double initialRowHeight;
    private double initialColWidth;
    public static final int FACTOR = 2;
    public static final String HEADER = "FIRST_COL";

    public Map<Coordinates, CellController> getCoordinates2CellController() {return coordinates2CellController;}
    public GridPane getGridPane() {return gridPane;}

    //setters
    public void setInitialRowAndColLayout(int rowHeight, int colWidth) {
        initialRowHeight = rowHeight;
        initialColWidth = colWidth;
    }

    public void initRowAndColumnHeaders(String sheetName, int numRows, int numCols) {
        CellController sheetNameCellController = createCellController();
        sheetNameCellController.setLabelText(sheetName);
        sheetNameCellController.setTableCellType(TableCellType.TABLE_NAME);
        Label topLeftCellLabel = sheetNameCellController.getLabel();
        gridPane.add(topLeftCellLabel, 0, 0);
        rowConstraintsMap.put(0, createAddAndGetRowConstraints(gridPane,initialRowHeight));
        columnConstraintsMap.put(DynamicSheet.HEADER, createAddAndGetColumnConstraints(gridPane,initialColWidth));


        for (int col = 1; col <= numCols; col++) {
            String charRepresentingColumn = String.valueOf((char) ('A' + col - 1));
            CellController columnHeaderCellController = createCellController();
            columnHeaderCellController.setLabelText(charRepresentingColumn);
            columnHeaderCellController.setTableCellType(TableCellType.COL_HEADER);
            Label headerLabel = columnHeaderCellController.getLabel();
            gridPane.add(headerLabel, col, 0);
            columnConstraintsMap.put(charRepresentingColumn, createAddAndGetColumnConstraints(gridPane,initialColWidth));
            string2ColCellController.put(charRepresentingColumn, columnHeaderCellController);
        }

        for (int row = 1; row <= numRows; row++) {
            String rowNumber = String.valueOf(row);
            CellController rowHeaderCellController = createCellController();
            rowHeaderCellController.setLabelText(rowNumber);
            rowHeaderCellController.setTableCellType(TableCellType.ROW_HEADER);
            Label rowLabel = rowHeaderCellController.getLabel();
            gridPane.add(rowLabel, 0, row);
            rowConstraintsMap.put(row, createAddAndGetRowConstraints(gridPane,initialRowHeight));
            integer2RowCellController.put(row - 1,rowHeaderCellController);
        }
    }

    public void addSuffixToHeaders(int colLenBetweenSubSheetToMasterSheet, int rowLenBetweenSubSheetAndMasterSheet) {
        string2ColCellController.forEach((colNumber1Indexed, cellController)->{
            int colNum = CoordinateFactory.getColIndexFromCellID(colNumber1Indexed) - 1; //make zero-indexed
            int suffixedColIndex = colNum + colLenBetweenSubSheetToMasterSheet;
            String suffixedCol = CoordinateFactory.numberToLetter(suffixedColIndex);
            String originalCol = cellController.getLabelText();
            String newDisplayedColName = String.format("%s [%s]", originalCol, suffixedCol);
            cellController.setLabelText(newDisplayedColName);
        });
        integer2RowCellController.forEach((rowNumber0Indexed,cellController)->{
            int originalRowIndex = rowNumber0Indexed + 1;
            int suffixedRow = rowNumber0Indexed + rowLenBetweenSubSheetAndMasterSheet;
            String newDisplayedRowName = String.format("%d [%d]", originalRowIndex, suffixedRow);
            cellController.setLabelText(newDisplayedRowName);
        });
    }

    public void populateSheetWithData(Sheet sheet) {
        int numOfRows = sheet.getNumOfRows();
        int numOfColumns = sheet.getNumOfCols();
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
        if (columnConstraints!=null) {
            columnConstraints.setMinWidth(width);
            columnConstraints.setPrefWidth(width);
            columnConstraints.setMaxWidth(width);
        }
    }

    public void updateRowHeight(int rowIndex, double height) {
        RowConstraints rowConstraints = rowConstraintsMap.get(rowIndex);
        if (rowConstraints!=null) {
            rowConstraints.setMinHeight(height);
            rowConstraints.setPrefHeight(height);
            rowConstraints.setMaxHeight(height);
        }
    }

    public void scaleColumnWidth(String columnName, double number) {
        ColumnConstraints columnConstraints = columnConstraintsMap.get(columnName);
        if (columnConstraints!=null) {
            columnConstraints.setMinWidth(initialColWidth*number);
            columnConstraints.setPrefWidth(initialColWidth*number);
            columnConstraints.setMaxWidth(initialColWidth*number);
        }
    }

    public void scaleRowHeight(int rowIndex, double height) {
        RowConstraints rowConstraints = rowConstraintsMap.get(rowIndex);
        if (rowConstraints!=null) {
            rowConstraints.setMinHeight(initialRowHeight*height);
            rowConstraints.setPrefHeight(initialRowHeight*height);
            rowConstraints.setMaxHeight(initialRowHeight*height);
        }
    }

    public void updateSheetScale(double number) {
        columnConstraintsMap.forEach((columnName, columnConstraints) -> {scaleColumnWidth(columnName, number);});
        rowConstraintsMap.forEach((rowIndex, rowConstraints) -> {scaleRowHeight(rowIndex, number);});
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
                Label label = cellController.getLabel();
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
