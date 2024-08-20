package utils;

import entities.cell.CellCoordinates;
import entities.cell.CoreCell;
import entities.sheet.CoreSheet;
import exceptions.CircleReferenceException;

import java.util.*;

public class TopologicalSorter {

    public static List<CellCoordinates> topologicalSort(CoreSheet sheet) {
        // List to store the result of topological sort
        List<CellCoordinates> topologicalOrder = new ArrayList<>();

        // Map to store the in-degree of each cell
        Map<CellCoordinates, Integer> inDegreeMap = new HashMap<>();

        // Queue to process cells with in-degree 0
        Queue<CoreCell> zeroInDegreeQueue = new LinkedList<>();

        // Initialize in-degree map
        for (CoreCell[] row : sheet.getCellsTable()) {
            for (CoreCell cell : row) {
                // Set initial in-degree for each cell
                inDegreeMap.put(cell.getCoordinates(), cell.getCellsAffectingMe().size());

                // If the cell has no dependencies (in-degree 0), add it to the queue
                if (cell.getCellsAffectingMe().isEmpty()) {
                    zeroInDegreeQueue.add(cell);
                }
            }
        }

        // Process cells with in-degree 0
        while (!zeroInDegreeQueue.isEmpty()) {
            CoreCell currentCell = zeroInDegreeQueue.poll();
            CellCoordinates currentCoordinates = currentCell.getCoordinates();

            // Add the current cell to the topological order
            topologicalOrder.add(currentCoordinates);

            // Reduce the in-degree of cells that depend on the current cell
            for (CellCoordinates dependentCoordinates : currentCell.getCellsAffectedByMe()) {
                CoreCell dependentCell = Utils.getCellObjectFromCellID(sheet, dependentCoordinates.toString());

                // Reduce the in-degree of the dependent cell
                int newInDegree = inDegreeMap.get(dependentCoordinates) - 1;
                inDegreeMap.put(dependentCoordinates, newInDegree);

                // If the dependent cell has no more incoming edges, add it to the queue
                if (newInDegree == 0) {
                    zeroInDegreeQueue.add(dependentCell);
                }
            }
        }

        // Check for cycles: if the topological order doesn't include all cells, there's a cycle
        if (topologicalOrder.size() != sheet.getNumOfRows() * sheet.getNumOfColumns()) {
            throw new CircleReferenceException("Circle reference identified");
        }

        return topologicalOrder;
    }
}
