package gui.components.center.cell;

import entities.coordinates.Coordinates;
import javafx.beans.property.SimpleStringProperty;
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

    public void bindToModule(SimpleStringProperty value) {
        cellLabel.textProperty().bind(value);
    }

    public void setCellLabel(String text) {
        cellLabel.setText(text);
    }

    public void addStyleClass(String styleClass) {
        if (!cellStackPane.getStyleClass().contains(styleClass)) {
            cellStackPane.getStyleClass().add(styleClass);
        }
    }

    public void removeStyleClass(String styleClass) {
        cellStackPane.getStyleClass().remove(styleClass);
    }

    public void resetStyleClass() {
        cellStackPane.getStyleClass().clear();
    }

    public void replaceStyleClass(String oldStyleClass, String newStyleClass) {
        removeStyleClass(oldStyleClass);
        addStyleClass(newStyleClass);
    }


    public StackPane getCellStackPane() {return cellStackPane;}
}
