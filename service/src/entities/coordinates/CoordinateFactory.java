package entities.coordinates;

import entities.cell.CoreCell;
import entities.sheet.CoreSheet;
import service_exceptions.CellOutOfBoundsException;
import service_exceptions.InvalidArgumentException;

import static entities.coordinates.Coordinates.*;

public class CoordinateFactory {

    public static CoreCell getCellObjectFromCellID(CoreSheet sheet, String cellName) {
        Coordinates coordinates = new Coordinates(cellName);

        int rowIndex = coordinates.getRow();
        int colIndex = coordinates.getCol();

        if (rowIndex < 0 || rowIndex >= sheet.getNumOfRows() ||
                colIndex < 0 || colIndex >= sheet.getNumOfCols()) {
            throw new CellOutOfBoundsException("Cell is out bounds. Valid row range: [1-" + sheet.getNumOfRows()
                    + "], Valid column range: [1-" + sheet.getNumOfCols() + "]", (rowIndex+1) + "," + (colIndex+1) );
        }

        return getCellObjectFromIndices(sheet,rowIndex,colIndex);
    }



    public static Coordinates getIndicesFromCellObject(CoreCell cell) {
        return new Coordinates(cell.getCoordinates().getRow(), cell.getCoordinates().getCol());
    }

    public static String getCellIDFromCellObject(CoreCell cell) {
        Coordinates coordinates = getIndicesFromCellObject(cell);
        return getCellIDFromIndices(cell.getCoordinates().getRow(), cell.getCoordinates().getCol());
    }

    public static CoreCell getCellObjectFromIndices(CoreSheet sheet, int rowIndex, int colIndex) {
        Coordinates coordinates = new Coordinates(rowIndex, colIndex);

        return sheet.getCoreCellsMap().get(coordinates);
    }



    public static int[] getIndicesFromCellID(String cellID) {
        int[] indices = new int[2];
        if (cellID == null || cellID.isEmpty()) {
            throw new InvalidArgumentException("Cell name cannot be null or empty", cellID);
        }

        int rowIndex = getRowIndexFromCellID(cellID);
        int colIndex = getColIndexFromCellID(cellID);
        indices[0] = rowIndex;
        indices[1] = colIndex;

        return indices;
    }






}
