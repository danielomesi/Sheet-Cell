package entities.sheet;

import java.io.Serializable;

public class Layout implements Serializable {
    private final int rowHeightUnits;
    private final int columnWidthUnits;

    public Layout(int rowHeightUnits, int columnWidthUnits) {
        this.rowHeightUnits = rowHeightUnits;
        this.columnWidthUnits = columnWidthUnits;
    }

    public int getRowHeightUnits() {return rowHeightUnits;}
    public int getColumnWidthUnits() {return columnWidthUnits;}
}
