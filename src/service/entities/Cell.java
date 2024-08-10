package service.entities;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    private Sheet sheet;
    private final int row;
    private final int col;
    private int version;
    private Object effectiveValue;
    private String originalExpression;
    private final List<Cell> cellsThatAffectMe = new ArrayList<>(0);
    private final List<Cell> cellsAffectedByMe = new ArrayList<>(0);

    public Cell(Sheet sheet, int row, int col)
    {
        this.sheet = sheet;
        this.row = row;
        this.col = col;
    }

    public int getRow() {return row;}
    public int getCol() {return col;}
    public Object getEffectiveValue() {return effectiveValue;}

    public void setEffectiveValue(Object value) {
        this.effectiveValue = value;
        //notifyDependents();
    }
    public void addDependent(Cell cell) {
        cellsThatAffectMe.add(cell);
    }

    public void removeDependent(Cell cell) {
        cellsThatAffectMe.remove(cell);
    }

//    public void update()
//    {
//        if (originalExpression != null)
//        {
//            originalExpression.execute(cellsThatAffectMe);
//        }
//        notifyDependents();
//    }

//    private void notifyDependents() {
//        for (Cell dependent : cellsThatAffectMe) {
//            dependent.update();  // Method to update the dependent cell
//        }
//    }


}
