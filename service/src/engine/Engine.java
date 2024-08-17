package engine;

import entities.Sheet;
import entities.core.CoreCell;

public interface Engine {

    public Sheet getSheet();

    public Sheet getSheet(int version);

    public void loadSheetFromXMLFile(String fullFilePath);

    public void loadSheetFromDummyData();

    public CoreCell getSpecificCell(String cellName);

    public void updateSpecificCell(String cellName, String originalExpression) throws CloneNotSupportedException;
}
