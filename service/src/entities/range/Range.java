package entities.range;

import entities.coordinates.CoordinateFactory;
import entities.coordinates.Coordinates;
import entities.sheet.Sheet;
import entities.stl.STLRange;
import entities.stl.STLRanges;
import entities.stl.STLSheet;
import exceptions.InvalidRangeException;
import utils.Utils;

import java.io.Serializable;
import java.util.*;

public class Range implements Serializable {
    private final String name;
    private Set<Coordinates> cells;

    public Set<Coordinates> getCells() {return cells;}

    public Range(String name,Sheet sheet, String fromCellID, String toCellID) {
        this.name = name;
        setRangeCells(fromCellID,toCellID,sheet);
    }

    public Range(STLRange stlRange, Sheet sheet) {
        this(stlRange.getSTLBoundaries().getFrom(),stlRange.getSTLBoundaries().getTo(),sheet);
    }

    public Range(String fromCellID, String toCellID, Sheet sheet) {
        this.name = fromCellID;
        setRangeCells(fromCellID,toCellID,sheet);
    }

    private void setRangeCells(String fromCellID, String toCellID, Sheet sheet) {
        cells = new HashSet<>();
        Coordinates from = new Coordinates(fromCellID);
        Coordinates to = new Coordinates(toCellID);
        int fromRow = from.getRow();
        int fromCol = from.getCol();
        int toRow = to.getRow();
        int toCol = to.getCol();
        Utils.validateInRange(fromRow,0,sheet.getNumOfRows());
        Utils.validateInRange(fromCol,0,sheet.getNumOfColumns());
        Utils.validateInRange(toRow,0,sheet.getNumOfRows());
        Utils.validateInRange(toCol,0,sheet.getNumOfColumns());
        if (fromRow<=toRow && fromCol<=toCol) {
            for (int i = fromRow; i <= toRow; i++) {
                for (int j = fromCol; j <= toCol; j++) {
                    cells.add(new Coordinates(i,j));
                }
            }
        }
        else {
            throw new InvalidRangeException("Range " + name + " is invalid, first cell must be prior to second cell");
        }

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
}
