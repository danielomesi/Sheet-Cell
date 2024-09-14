package gui.components.center.cell;

import entities.coordinates.Coordinates;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CellController {

    private final static Set<String> colorStyles = new HashSet<>();

    @FXML
    private Label cellLabel;

    @FXML
    private StackPane cellStackPane;

    private Coordinates coordinates;
    private TableCellType tableCellType;

    static {
        colorStyles.add("default-cell");
        colorStyles.add("range-cell");
        colorStyles.add("affected-cell");
        colorStyles.add("affecting-cell");
        colorStyles.add("selected-cell");
    }

    public void setCellCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
    public void setTableCellType(TableCellType tableCellType) {this.tableCellType = tableCellType;}

    public TableCellType getTableCellType() {return tableCellType;}

    public Coordinates getCoordinates() {return coordinates;}

    public void bindToModule(SimpleStringProperty value) {
        cellLabel.textProperty().bind(value);
    }

    public void setLabelText(String text) {
        cellLabel.setText(text);
    }

    public String getLabelText() {
        return cellLabel.getText();
    }

    public void addStyleClass(String styleClass) {
        if (!cellStackPane.getStyleClass().contains(styleClass)) {
            cellStackPane.getStyleClass().add(styleClass);
        }
    }

    public void setColorStyle(String colorStyleClass) {
        addStyleClass(colorStyleClass);
        colorStyles.stream().filter((style) -> !Objects.equals(style, colorStyleClass))
                .forEach((style) -> cellStackPane.getStyleClass().remove(style));

    }


    public StackPane getCellStackPane() {return cellStackPane;}
}
