package utils;

import entities.coordinates.Coordinates;
import entities.cell.CoreCell;
import entities.sheet.CoreSheet;
import exceptions.CircleReferenceException;

import java.util.*;

public class TopologicalSorter {

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
}
