package engine;

import entities.cell.Cell;
import entities.sheet.Sheet;
import entities.cell.CoreCell;

public interface Engine {

    public Sheet getSheet();

    public Sheet getSheet(int version);

    public void loadSheetFromXMLFile(String fullFilePath);

    public void loadSheetFromDummyData();

    public Cell getSpecificCell(String cellName);

    public void updateSpecificCell(String cellName, String originalExpression);
}
