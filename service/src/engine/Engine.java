package engine;

import entities.cell.Cell;
import entities.coordinates.Coordinates;
import entities.sheet.Sheet;

import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public void addRange(String rangeName, String fromCellID, String toCellID);

    public void deleteRange(String rangeName);

    public void setSubSheet(String fromCellID, String toCellID);

    public Sheet getSubSheet();

    public List<Integer> sort(List<String> colNames, String fromCellID, String toCellID, boolean isSortingFirstRow);

    public List<Object> getEffectiveValuesInSpecificCol(String colName, String fromCellID, String toCellID);

    public Set<Integer> filter(String colName,List<Object> effectiveValuesToFilterBy, String fromCellID, String toCellID, boolean isFilteringEmptyCells);
}
