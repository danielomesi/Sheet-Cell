package engine;

import entities.cell.Cell;
import entities.sheet.Sheet;

import java.util.List;

public interface Engine {

    public Sheet getSheet();

    public Sheet getSheet(int version);

    public void loadSheetFromXMLFile(String fullFilePath);

    public void loadSheetFromDummyData();

    public Cell getSpecificCell(String cellName);

    public void updateSpecificCell(String cellName, String originalExpression);

    public List<Sheet> getSheetList();

    public void saveStateToFile(String fullFilePath);

    public void loadStateFromFile(String fullFilePath);

}
