package entities.cell;

import entities.coordinates.Coordinates;

import java.io.Serializable;
import java.util.Set;

public interface Cell extends Serializable {
    public Coordinates getCoordinates();
    public int getVersion();
    public String getLastEditor();
    public Object getEffectiveValue();
    public String getOriginalExpression();
    public Set<Coordinates> getCellsAffectedByMe();
    public Set<Coordinates> getCellsAffectingMe();
}
