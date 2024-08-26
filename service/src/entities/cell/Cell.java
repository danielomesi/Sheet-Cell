package entities.cell;

import entities.coordinates.CellCoordinates;

import java.util.Set;

public interface Cell {
    public CellCoordinates getCoordinates();
    public int getVersion();
    public Object getEffectiveValue();
    public String getOriginalExpression();
    public Set<CellCoordinates> getCellsAffectedByMe();
    public Set<CellCoordinates> getCellsAffectingMe();
}
