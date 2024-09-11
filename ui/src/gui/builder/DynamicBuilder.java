package gui.builder;

import entities.coordinates.Coordinates;
import entities.sheet.Sheet;
import gui.components.center.cell.CellController;
import gui.components.center.cell.TableCellType;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DynamicBuilder {
    public static DynamicSheetTable buildDynamicSheetTable(Sheet sheet) {
        GridPane gridPane = new GridPane();
        Map<Coordinates,CellController> coordinates2CellController = new HashMap<>();

        int numRows = sheet.getNumOfRows();
        int numCols = sheet.getNumOfColumns();

        //add the name of the sheet as the top left cell
        CellController sheetNameCellController = createCellController();
        sheetNameCellController.setCellLabel(sheet.getName());
        sheetNameCellController.setTableCellType(TableCellType.HEADER);
        StackPane topLeftCellPane = (StackPane) sheetNameCellController.getCellStackPane();
        gridPane.add(topLeftCellPane, 0, 0);

        for (int col = 1; col <= numCols; col++) {
            String charRepresentingColumn = String.valueOf((char) ('A' + col - 1));
            CellController columnHeaderCellController = createCellController();
            columnHeaderCellController.setCellLabel(charRepresentingColumn);
            columnHeaderCellController.setTableCellType(TableCellType.HEADER);
            StackPane headerPane = (StackPane) columnHeaderCellController.getCellStackPane();
            gridPane.add(headerPane, col, 0);
        }

        for (int row = 1; row <= numRows; row++) {
            String rowNumber = String.valueOf(row);
            CellController rowHeaderCellController = createCellController();
            rowHeaderCellController.setCellLabel(rowNumber);
            rowHeaderCellController.setTableCellType(TableCellType.HEADER);
            StackPane rowPane = (StackPane) rowHeaderCellController.getCellStackPane();
            gridPane.add(rowPane, 0, row);
        }

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                Coordinates coordinates = new Coordinates(row,col);
                CellController cellController = createCellController();
                coordinates2CellController.put(coordinates, cellController);
                cellController.setCellCoordinates(coordinates);
                cellController.setTableCellType(TableCellType.DATA);
                StackPane cellPane = (StackPane) cellController.getCellStackPane();
                gridPane.add(cellPane, col+1, row+1);
            }
        }

        gridPane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        gridPane.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        return new DynamicSheetTable(coordinates2CellController,gridPane);
    }

    public static CellController createCellController() {
        try {
            FXMLLoader loader = new FXMLLoader(DynamicBuilder.class.getResource("/gui/components/center/cell/cell.fxml"));
            StackPane pane = loader.load();
            return loader.getController();
        } catch (IOException e) {
            return null;
        }
    }
}
