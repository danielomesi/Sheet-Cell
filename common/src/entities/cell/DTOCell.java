package entities.cell;

import com.google.gson.annotations.JsonAdapter;
import entities.coordinates.Coordinates;
import json.adapters.EffectiveValueAdapter;

import java.util.HashSet;
import java.util.Set;

public class DTOCell implements Cell {
    private final Coordinates coordinates;
    private final int version;
    private final String lastEditor;

    @JsonAdapter(EffectiveValueAdapter.class)
    private final Object effectiveValue;

    private final String originalExpression;
    private final Set<Coordinates> cellsAffectedByMe;
    private final Set<Coordinates> cellsAffectingMe;

    public DTOCell(Cell coreCell)
    {
        coordinates= coreCell.getCoordinates();
        version = coreCell.getVersion();
        lastEditor = coreCell.getLastEditor();
        effectiveValue = coreCell.getEffectiveValue();
        originalExpression = coreCell.getOriginalExpression();
        Set<Coordinates> outerCellsAffectedByMe = coreCell.getCellsAffectedByMe();
        Set<Coordinates> outerCellsAffectingMe = coreCell.getCellsAffectingMe();
        cellsAffectedByMe = outerCellsAffectedByMe != null? new HashSet<>(outerCellsAffectedByMe) : null;
        cellsAffectingMe = outerCellsAffectingMe != null? new HashSet<>(outerCellsAffectingMe) : null;
    }

    public DTOCell(Coordinates coordinates, int version, String lastEditor, Object effectiveValue,
                   String originalExpression, Set<Coordinates> cellsAffectedByMe, Set<Coordinates> cellsAffectingMe) {
        this.coordinates = coordinates;
        this.version = version;
        this.lastEditor = lastEditor;
        this.effectiveValue = effectiveValue;
        this.originalExpression = originalExpression;
        this.cellsAffectedByMe = cellsAffectedByMe;
        this.cellsAffectingMe = cellsAffectingMe;
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
    @Override
    public String getLastEditor() {return lastEditor;}
}
