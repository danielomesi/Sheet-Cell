package engine;

import entities.cell.Cell;
import entities.permission.PermissionType;
import entities.sheet.Sheet;
import entities.sheet.SheetMetaData;
import permission.SheetData;

import java.util.List;
import java.util.Set;

public interface Engine {

    public Sheet getSubSheet(String username);

    public void loadSheetFromXMLFile(String fullFilePath, String uploaderUsername);

    public void loadSheetFromXMLString(String xmlFileContent, String uploaderUsername);

    public Sheet getSheet(String sheetName);

    public Sheet getSheet(String sheetName, int version);

    public int getNumOfVersions(String sheetName);

    public List<Sheet> getSheetList(String sheetName);

    public List<SheetData> getUserSheets(String username);

    public void saveStateToFile(String fullFilePath);

    public void loadStateFromFile(String fullFilePath, String uploaderUsername);

    public void addRange(String sheetName, String rangeName, String fromCellID, String toCellID);

    public void deleteRange(String sheetName, String rangeName);

    public void setSubSheet(String sheetName, String fromCellID, String toCellID, String username);

    public Cell getSpecificCell(String sheetName, String cellName);

    public void updateSpecificCell(String cellName, String originalExpression, String sheetName);

    public List<Integer> sort(String sheetName, List<String> colNames, String fromCellID, String toCellID, boolean isSortingFirstRow);

    public List<Object> getEffectiveValuesInSpecificCol(String sheetName, String colName, String fromCellID, String toCellID);

    public Set<Integer> filter(String sheetName, String colName, List<Object> effectiveValuesToFilterBy, String fromCellID, String toCellID, boolean isFilteringEmptyCells);

    public List<SheetMetaData> getAllSheetsMetaData();

    public void requestPermission(String requestingUsername, String sheetName, PermissionType permissionType);

    public void applyAccessDecision(String usernameWithPendingRequest, String sheetName, PermissionType permissionType, boolean isAccessAllowed);
}
