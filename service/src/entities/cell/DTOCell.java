package entities.cell;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DTOCell implements Cell {
    private final CellCoordinates coordinates;
    private final int version;
    private final Object effectiveValue;
    private String originalExpression;
    private Set<CellCoordinates> cellsAffectedByMe;
    private Set<CellCoordinates> cellsAffectingMe;

    public DTOCell(CoreCell coreCell)
    {
        coordinates= coreCell.getCoordinates();
        version = coreCell.getVersion();
        effectiveValue = coreCell.getEffectiveValue();
        originalExpression = coreCell.getOriginalExpression();
        Set<CellCoordinates> outerCellsAffectedByMe = coreCell.getCellsAffectedByMe();
        Set<CellCoordinates> outerCellsAffectingMe = coreCell.getCellsAffectingMe();
        cellsAffectedByMe = outerCellsAffectedByMe != null? new HashSet<>(outerCellsAffectedByMe) : null;
        cellsAffectingMe = outerCellsAffectingMe != null? new HashSet<>(outerCellsAffectingMe) : null;
    }

    @Override
    public CellCoordinates getCoordinates() {return coordinates;}
    @Override
    public int getVersion() {return version;}
    @Override
    public Object getEffectiveValue() {return effectiveValue;}
    @Override
    public String getOriginalExpression() {return originalExpression;}
    @Override
    public Set<CellCoordinates> getCellsAffectedByMe() {return cellsAffectedByMe;}
    @Override
    public Set<CellCoordinates> getCellsAffectingMe() {return cellsAffectingMe;}
}
