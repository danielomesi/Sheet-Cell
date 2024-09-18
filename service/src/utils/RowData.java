package utils;


import entities.cell.Cell;
import entities.coordinates.CoordinateFactory;

import java.util.List;

public class RowData {
    int rowIndex;
    List<Cell> values;

    RowData(int rowIndex, List<Cell> values) {
        this.rowIndex = rowIndex;
        this.values = values;
    }

    public Double getEffectiveValueByColName(String colName) {
        for (Cell cell : values) {
            if (CoordinateFactory.numberToLetter(cell.getCoordinates().getCol()).equals(colName)) {
                return (Double) cell.getEffectiveValue();
            }
        }

        return null;
    }
}