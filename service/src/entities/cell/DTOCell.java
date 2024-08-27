package entities.cell;

import entities.coordinates.Coordinates;

import java.util.HashSet;
import java.util.Set;

public class DTOCell implements Cell {
    private final Coordinates coordinates;
    private final int version;
    private final Object effectiveValue;
    private final String originalExpression;
    private final Set<Coordinates> cellsAffectedByMe;
    private final Set<Coordinates> cellsAffectingMe;

    public DTOCell(CoreCell coreCell)
    {
        coordinates= coreCell.getCoordinates();
        version = coreCell.getVersion();
        effectiveValue = coreCell.getEffectiveValue();
        originalExpression = coreCell.getOriginalExpression();
        Set<Coordinates> outerCellsAffectedByMe = coreCell.getCellsAffectedByMe();
        Set<Coordinates> outerCellsAffectingMe = coreCell.getCellsAffectingMe();
        cellsAffectedByMe = outerCellsAffectedByMe != null? new HashSet<>(outerCellsAffectedByMe) : null;
        cellsAffectingMe = outerCellsAffectingMe != null? new HashSet<>(outerCellsAffectingMe) : null;
    }

    @Override
    public Coordinates getCoordinates() {return coordinates;}
    @Override
    public int getVersion() {return version;}
    @Override
    public Object getEffectiveValue() {return effectiveValue;}
    @Override
    public String getOriginalExpression() {return originalExpression;}
    @Override
    public Set<Coordinates> getCellsAffectedByMe() {return cellsAffectedByMe;}
    @Override
    public Set<Coordinates> getCellsAffectingMe() {return cellsAffectingMe;}
}
