package entities.range;

import entities.coordinates.Coordinates;

import java.io.Serializable;
import java.util.*;

public interface RangeInterface extends Serializable {

    public Set<Coordinates> getCells();

}

