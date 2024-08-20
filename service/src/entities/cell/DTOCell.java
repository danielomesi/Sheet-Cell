package entities.cell;

import java.util.ArrayList;
import java.util.List;

public class DTOCell implements Cell {
    private final CellCoordinates coordinates;
    private final int version;
    private final Object effectiveValue;
    private String originalExpression;
    private List<CellCoordinates> cellsAffectedByMe;
    private List<CellCoordinates> cellsAffectingMe;

    public DTOCell(CoreCell coreCell)
    {
        coordinates= coreCell.getCoordinates();
        version = coreCell.getVersion();
        effectiveValue = coreCell.getEffectiveValue();
        originalExpression = coreCell.getOriginalExpression();
        List<CellCoordinates> outerCellsAffectedByMe = coreCell.getCellsAffectedByMe();
        List<CellCoordinates> outerCellsAffectingMe = coreCell.getCellsAffectingMe();
        cellsAffectedByMe = outerCellsAffectedByMe != null? new ArrayList<>(outerCellsAffectedByMe) : null;
        cellsAffectingMe = outerCellsAffectingMe != null? new ArrayList<>(outerCellsAffectingMe) : null;
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
    public List<CellCoordinates> getCellsAffectedByMe() {return cellsAffectedByMe;}
    @Override
    public List<CellCoordinates> getCellsAffectingMe() {return cellsAffectingMe;}
}
