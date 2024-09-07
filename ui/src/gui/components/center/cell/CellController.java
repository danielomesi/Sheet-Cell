package gui.components.center.cell;

import entities.coordinates.Coordinates;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class CellController {

    @FXML
    private Label cellLabel;

    @FXML
    private StackPane cellStackPane;

    private Coordinates coordinates;
    private TableCellType tableCellType;

    public void setCellCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
    public void setTableCellType(TableCellType tableCellType) {this.tableCellType = tableCellType;}

    public TableCellType getTableCellType() {return tableCellType;}

    public Coordinates getCoordinates() {return coordinates;}

    public void setCellLabel(String label) {
        cellLabel.setText(label);
    }

    public void setBackGroundColor(String color) {
        setStyle("-fx-background-color: " + color + ";");
    }

    public void setStyle(String style) {
        cellStackPane.setStyle(style);
    }

    public StackPane getCellStackPane() {return cellStackPane;}
}
