package utils;

import entities.cell.Cell;
import entities.coordinates.CoordinateFactory;
import entities.coordinates.Coordinates;
import entities.cell.CoreCell;
import entities.sheet.CoreSheet;
import entities.sheet.Sheet;
import exceptions.CircleReferenceException;
import exceptions.InvalidArgumentException;

import java.util.*;

public class Sorter {

    public static List<Coordinates> topologicalSort(CoreSheet sheet) {
        List<Coordinates> topologicalOrder = new ArrayList<>();

        Map<Coordinates, Integer> inDegreeMap = new HashMap<>();

        Queue<CoreCell> zeroInDegreeQueue = new LinkedList<>();

        for (Map.Entry<Coordinates, CoreCell> entry : sheet.getCoreCellsMap().entrySet()) {
            Coordinates coordinates = entry.getKey();
            CoreCell cell = entry.getValue();

            inDegreeMap.put(coordinates, cell.getCellsAffectingMe().size());

            // If the cell has no dependencies (in-degree 0), add it to the queue
            if (cell.getCellsAffectingMe().isEmpty()) {
                zeroInDegreeQueue.add(cell);
            }
        }

        // Process cells with in-degree 0
        while (!zeroInDegreeQueue.isEmpty()) {
            CoreCell currentCell = zeroInDegreeQueue.poll();
            Coordinates currentCoordinates = currentCell.getCoordinates();

            // Add the current cell to the topological order
            topologicalOrder.add(currentCoordinates);

            // Reduce the in-degree of cells that depend on the current cell
            for (Coordinates dependentCoordinates : currentCell.getCellsAffectedByMe()) {
                CoreCell dependentCell = sheet.getCoreCellsMap().get(dependentCoordinates);

                // Reduce the in-degree of the dependent cell
                int newInDegree = inDegreeMap.get(dependentCoordinates) - 1;
                inDegreeMap.put(dependentCoordinates, newInDegree);

                // If the dependent cell has no more incoming edges, add it to the queue
                if (newInDegree == 0) {
                    zeroInDegreeQueue.add(dependentCell);
                }
            }
        }

        // Check for cycles: if one of the cells has an inorder degree bigger than 0 - there's a cycle
        for (Map.Entry<Coordinates, Integer> entry : inDegreeMap.entrySet()) {
            int inDegree = entry.getValue();
            if (inDegree > 0) {
                throw new CircleReferenceException("Circular reference identified");
            }
        }
        return topologicalOrder;
    }

    public static List<Integer> sortRowsByColumns(Sheet sheet, List<String> colNames) {
        validateNumericValuesOrThrow(sheet);

        int numRows = sheet.getNumOfRows();

        List<RowData> rows = new ArrayList<>();

        for (int row = 0; row < numRows; row++) {
            List<Cell> values = new ArrayList<>();
            for (String colName : colNames) {
                int col = CoordinateFactory.convertColumnStringToIndex(colName);
                Cell cell = sheet.getCell(row, col);
                values.add(cell);
            }
            rows.add(new RowData(row, values));
        }

        rows.sort(new RowsComparator(colNames));

        List<Integer> sortedRowIndices = new ArrayList<>();
        for (int i=0; i < rows.size(); i++) {
            sortedRowIndices.add(getObjectIndexByRowIndex(rows, i));
        }

        return sortedRowIndices;
    }

    private static int getObjectIndexByRowIndex(List<RowData> rows, int rowIndex) {
        for (int i=0; i < rows.size(); i++) {
            if (rows.get(i).rowIndex == rowIndex) {
                return i;
            }
        }
        throw new InvalidArgumentException("Row index out of bounds");
    }



    public static void validateNumericValuesOrThrow(Sheet sheet) {
        int numRows = sheet.getNumOfRows();
        int numCols = sheet.getNumOfCols();
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                Cell cell = sheet.getCell(i, j);
                if (cell == null || !(cell.getEffectiveValue() instanceof Number)) {
                    throw new InvalidArgumentException("Sorting only supports numbers");
                }
            }
        }
    }
}
