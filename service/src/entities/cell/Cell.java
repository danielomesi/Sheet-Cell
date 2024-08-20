package entities.cell;

import java.util.List;

public interface Cell {
    public CellCoordinates getCoordinates();
    public int getVersion();
    public Object getEffectiveValue();
    public String getOriginalExpression();
    public List<CellCoordinates> getCellsAffectedByMe();
    public List<CellCoordinates> getCellsAffectingMe();
}
