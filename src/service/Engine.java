package service;

import service.entities.Cell;
import service.entities.Sheet;

public interface Engine {

    public Sheet getSheet();

    public void loadSheetFromXMLFile(String fullFilePath);

    public void loadSheetFromDummyData();

    public Cell getSpecificCell(String cellName);

    public void UpdateSpecificCell(String cellName, String originalExpression) throws CloneNotSupportedException;
}
