package entities.sheet;

public class Layout {
    private final int rowHeightUnits;
    private final int columnWidthUnits;

    public Layout(int rowHeightUnits, int columnWidthUnits) {
        this.rowHeightUnits = rowHeightUnits;
        this.columnWidthUnits = columnWidthUnits;
    }

    public int getRowHeightUnits() {return rowHeightUnits;}
    public int getColumnWidthUnits() {return columnWidthUnits;}
}
