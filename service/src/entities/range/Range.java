package entities.range;

import entities.coordinates.Coordinates;
import entities.sheet.Sheet;
import entities.stl.STLRange;
import entities.stl.STLSheet;
import service_exceptions.InvalidRangeException;
import utils.Utils;

import java.util.*;

public class Range implements RangeInterface {
    private String name;
    private Set<Coordinates> cells;

    public Set<Coordinates> getCells() {return cells;}
    public String getName() {return name;}
    public Range(String name,Sheet sheet, String fromCellID, String toCellID) {
        this(sheet,fromCellID,toCellID);
        validateRangeName(name);
        this.name=name;
    }

    public Range(Sheet sheet, String fromCellID, String toCellID) {
        setRangeCells(fromCellID,toCellID,sheet);
    }

    public Range(STLRange stlRange, Sheet sheet) {
        this(sheet,stlRange.getSTLBoundaries().getFrom(),stlRange.getSTLBoundaries().getTo());
    }

    private void setRangeCells(String fromCellID, String toCellID, Sheet sheet) {
        cells = new HashSet<>();
        Coordinates from = new Coordinates(fromCellID);
        Coordinates to = new Coordinates(toCellID);
        int fromRow = from.getRow();
        int fromCol = from.getCol();
        int toRow = to.getRow();
        int toCol = to.getCol();
        validateRange(sheet,fromRow,fromCol,toRow,toCol);
        for (int i = fromRow; i <= toRow; i++) {
            for (int j = fromCol; j <= toCol; j++) {
                cells.add(new Coordinates(i,j));
            }
        }
    }

    private Coordinates findExtreme(Comparator<Coordinates> comparator) {
        if (cells == null || cells.isEmpty()) {
            throw new IllegalArgumentException("The set of coordinates cannot be null or empty");
        }

        return cells.stream()
                .min(comparator)
                .orElseThrow(); // This should never happen since the set is not empty
    }

    public Coordinates findMostLeftUpper() {
        return findExtreme(Comparator.comparingInt(Coordinates::getCol).thenComparingInt(Coordinates::getRow));
    }

    public Coordinates findMostBottomRight() {
        return findExtreme((c1, c2) -> {
            int columnComparison = Integer.compare(c2.getCol(), c1.getCol());
            if (columnComparison != 0) {
                return columnComparison;
            }
            return Integer.compare(c2.getRow(), c1.getRow());
        });
    }


    public static Map<String,Range> generateRangesFromSTLRanges(STLSheet stlSheet, Sheet sheet) {
        Map<String,Range> rangesMap = new HashMap<>();
        List<STLRange> stlRanges = stlSheet.getSTLRanges().getSTLRange();
        for (STLRange stlRange : stlRanges) {
            String rangeName = stlRange.getName();
            if (!rangesMap.containsKey(rangeName)) {
                rangesMap.put(rangeName,new Range(stlRange,sheet));
            }
            else {
                throw new InvalidRangeException("A range cannot appear more than once");
            }
        }
        return rangesMap;
    }


    public static void validateRange(Sheet sheet, int fromRow, int fromCol, int toRow, int toCol) {
        Utils.validateInRange(fromRow,0,sheet.getNumOfRows());
        Utils.validateInRange(fromCol,0,sheet.getNumOfCols());
        Utils.validateInRange(toRow,0,sheet.getNumOfRows());
        Utils.validateInRange(toCol,0,sheet.getNumOfCols());
        if (fromRow>toRow || fromCol>toCol) {
            throw new InvalidRangeException("Range is invalid, first cell must be prior to second cell");
        }
    }

    private void validateRangeName(String name) {
        if (name == null || name.isEmpty()) {
            throw new InvalidRangeException("Range name cannot be empty");
        }
    }
}
