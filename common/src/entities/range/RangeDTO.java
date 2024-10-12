package entities.range;

import entities.coordinates.Coordinates;

import java.util.HashSet;
import java.util.Set;

public class RangeDTO implements RangeInterface{
    private final Set<Coordinates> cells;
    private final String name;

    public RangeDTO(RangeInterface range) {
        cells = new HashSet<Coordinates>(range.getCells());
        name = range.getName();
    }

    //getters
    public Set<Coordinates> getCells() {return cells;}
    public String getName() {return name;}
}
