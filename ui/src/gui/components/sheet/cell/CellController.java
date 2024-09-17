package gui.components.sheet.cell;

import entities.coordinates.Coordinates;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class CellController {

    private final static Set<String> colorStyles = new HashSet<>();

    @FXML
    private Label cellLabel;

    private Coordinates coordinates;
    private TableCellType tableCellType;
    private Color customizedBackgroundColor;
    private Color customizedTextColor;

    static {
        colorStyles.add("default-cell");
        colorStyles.add("range-cell");
        colorStyles.add("affected-cell");
        colorStyles.add("affecting-cell");
        colorStyles.add("selected-cell");
    }

    //getters
    public TableCellType getTableCellType() {return tableCellType;}
    public Coordinates getCoordinates() {return coordinates;}
    public String getLabelText() {return cellLabel.getText();}
    public Label getLabel() {return cellLabel;}
    public Color getCustomizedBackgroundColor() {return customizedBackgroundColor;}
    public Color getCustomizedTextColor() {return customizedTextColor;}

    //setters
    public void setCellCoordinates(Coordinates coordinates) {this.coordinates = coordinates;}
    public void setTableCellType(TableCellType tableCellType) {this.tableCellType = tableCellType;}
    public void setLabelText(String text) {cellLabel.setText(text);}
    public void setColorStyle(String colorStyleClass) {
        addStyleClass(colorStyleClass);
        colorStyles.stream().filter((style) -> !Objects.equals(style, colorStyleClass))
                .forEach((style) -> cellLabel.getStyleClass().remove(style));

    }
    public void setLabel(Label label) {this.cellLabel = label;}

    // Method to copy data from another CellController
    public void copyFrom(CellController other) {
        // Copy Coordinates (assuming Coordinates is immutable or copied appropriately)
        this.coordinates = other.coordinates;

        // Copy TableCellType and Color fields
        this.tableCellType = other.tableCellType;
        this.customizedBackgroundColor = other.customizedBackgroundColor;
        this.customizedTextColor = other.customizedTextColor;

        // Copy Label properties (text, styles)
        this.cellLabel.setText(other.cellLabel.getText());
        this.cellLabel.getStyleClass().setAll(other.cellLabel.getStyleClass());  // Copy style classes
        this.cellLabel.setStyle(other.cellLabel.getStyle());  // Copy inline styles
        this.cellLabel.setAlignment(other.cellLabel.getAlignment());  // Copy alignment
    }

    public void addStyleClass(String styleClass) {
        if (!cellLabel.getStyleClass().contains(styleClass)) {
            cellLabel.getStyleClass().add(styleClass);
        }
    }

    public void bindToModule(SimpleStringProperty value) {
        cellLabel.textProperty().bind(value);
    }

    private  String colorToString(Color color) {
        return String.format("#%02x%02x%02x", (int) (color.getRed() * 255), (int) (color.getGreen() * 255), (int) (color.getBlue() * 255));
    }

    public void setColorStyles(Color backgroundColor, Color textColor) {
        customizedBackgroundColor = backgroundColor;
        customizedTextColor = textColor;
        StringBuilder sb = new StringBuilder();
        if (backgroundColor != null) {
            sb.append("-fx-background-color: ").append(colorToString(backgroundColor)).append(";");
        }
        if (textColor != null) {
            sb.append("-fx-text-fill: ").append(colorToString(textColor)).append(";");
        }

        cellLabel.setStyle(sb.toString());
    }

    public void resetColorStyles() {
        customizedBackgroundColor = null;
        customizedTextColor = null;
        cellLabel.setStyle(null);
    }
}
