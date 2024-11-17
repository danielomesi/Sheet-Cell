package engine;

import entities.cell.Cell;
import entities.cell.CoreCell;
import entities.coordinates.Coordinates;
import entities.coordinates.CoordinateFactory;
import entities.permission.PermissionRequest;
import entities.range.Range;
import entities.sheet.CoreSheet;
import entities.sheet.DTOSheet;
import entities.sheet.Sheet;
import entities.sheet.SheetMetaData;
import entities.stl.STLLayout;
import entities.stl.STLSheet;
import service_exceptions.ExistingSheetException;
import service_exceptions.InvalidXMLException;
import jakarta.xml.bind.JAXBException;
import entities.permission.PermissionType;
import permission.SheetData;
import service_exceptions.NoExistenceException;
import utils.Filter;
import utils.Sorter;
import utils.Utils;

import java.util.*;

public class EngineImpl implements Engine {
    private final int MAX_ROWS = 50;
    private final int MAX_COLS = 20;
    private final Map<String,CoreSheet> username2SubSheet = new HashMap<>();
    private final Map<String,SheetData> sheetName2SheetDataList = new HashMap<>();

    @Override
    public Sheet getSheet(String sheetName) {
        SheetData sheetData = sheetName2SheetDataList.get(sheetName);
        if (sheetData == null) {
            return null;
        }
        List<CoreSheet> coreSheetsVersions = sheetData.getSheetVersions();
        if (coreSheetsVersions == null) {
            return null;
        }

        return new DTOSheet(coreSheetsVersions.getLast());
    }

    @Override
    public Sheet getSheet(String sheetName, int version) {
        SheetData sheetData = sheetName2SheetDataList.get(sheetName);
        if (sheetData == null) {
            return null;
        }
        List<CoreSheet> coreSheetsVersions = sheetData.getSheetVersions();
        if (coreSheetsVersions == null || coreSheetsVersions.size() <= version) {
            return null;
        }

        return new DTOSheet(coreSheetsVersions.get(version));
    }

    @Override
    public int getNumOfVersions(String sheetName) {
        SheetData sheetData = sheetName2SheetDataList.get(sheetName);

        return sheetData == null ? 0 : sheetData.getSheetVersions().size();
    }

    @Override
    public synchronized List<Sheet> getSheetList(String sheetName) {
        if (sheetName2SheetDataList.containsKey(sheetName)) {
            List<Sheet> sheets = new LinkedList<>();
            sheetName2SheetDataList.get(sheetName).getSheetVersions().forEach(coreSheet -> {
                DTOSheet dtoSheet = generateDTOSheet(coreSheet);
                sheets.add(dtoSheet);
            });

            return sheets;
        }
        return null;
    }

    @Override
    public List<SheetData> getUserSheets(String username) {
        List<SheetData> userSheets = new ArrayList<>();
        for (String sheetName : sheetName2SheetDataList.keySet()) {
            SheetData sheetData = sheetName2SheetDataList.get(sheetName);
            PermissionType permission = sheetData.getPermission(username);
            if (permission == PermissionType.OWNER) {
                userSheets.add(sheetData);
            }
        }
        return userSheets;
    }

    @Override
    public synchronized void saveStateToFile(String fullFilePath) {
        fullFilePath = Utils.trimQuotes(fullFilePath);
        //FileIOHandler.saveCoreSheetsToFile(coreSheets, fullFilePath);
    }

    @Override
    public synchronized void loadStateFromFile(String fullFilePath, String uploaderUsername) {
        fullFilePath = Utils.trimQuotes(fullFilePath);
        if (!sheetName2SheetDataList.containsKey(uploaderUsername)) {
            List<CoreSheet> coreSheets = FileIOHandler.loadCoreSheetsFromFile(fullFilePath);
            String sheetName = coreSheets.getLast().getName();
            SheetData sheetData = new SheetData(sheetName,uploaderUsername);
            sheetData.setSheetVersions(coreSheets);
            sheetName2SheetDataList.put(sheetName,sheetData);

        }

    }

    @Override
    public synchronized void addRange(String sheetName, String rangeName, String fromCellID, String toCellID) {
        if (sheetName2SheetDataList.containsKey(sheetName)) {
            SheetData sheetData = sheetName2SheetDataList.get(sheetName);
            sheetData.getSheetVersions().getLast().addRange(rangeName, fromCellID, toCellID);
        }
    }

    @Override
    public synchronized void deleteRange(String sheetName, String rangeName) {
        if (sheetName2SheetDataList.containsKey(sheetName)) {
            SheetData sheetData = sheetName2SheetDataList.get(sheetName);
            sheetData.getSheetVersions().getLast().deleteRange(rangeName);
        }
    }

    @Override
    public void setSubSheet(String sheetName, String fromCellID, String toCellID, String username) {
        CoreSheet temp = makeSubSheet(sheetName,fromCellID, toCellID);
        username2SubSheet.put(username,temp);
    }

    @Override
    public Sheet getSubSheet(String username) {
        CoreSheet temp = username2SubSheet.get(username);

        return temp == null ? null : generateDTOSheet(temp);
    }

    @Override
    public synchronized void loadSheetFromXMLFile(String fullFilePath, String uploaderUsername) {
        STLSheet stlSheet;
        fullFilePath = Utils.trimQuotes(fullFilePath);
        try {
            stlSheet = FileIOHandler.loadXMLToObject(fullFilePath, STLSheet.class);
        }
        catch (JAXBException e) {
            throw new InvalidXMLException("Invalid XML file");
        }
        validateXMLSheetLayout(stlSheet);
        CoreSheet coreSheet = new CoreSheet(stlSheet,uploaderUsername);
        String sheetName = coreSheet.getName();
        if (!sheetName2SheetDataList.containsKey(sheetName)) {
            List<CoreSheet> coreSheets = new ArrayList<>();
            coreSheets.add(coreSheet);
            SheetData sheetData = new SheetData(sheetName,uploaderUsername);
            sheetData.setSheetVersions(coreSheets);
            sheetName2SheetDataList.put(sheetName,sheetData);

        }
    }

    @Override
    public synchronized void loadSheetFromXMLString(String xmlFileContent, String uploaderUsername) {
        STLSheet stlSheet;
        try {
            stlSheet = FileIOHandler.loadXMLStringToObject(xmlFileContent, STLSheet.class);
        }
        catch (JAXBException e) {
            throw new InvalidXMLException("Invalid XML file");
        }
        validateXMLSheetLayout(stlSheet);
        CoreSheet coreSheet = new CoreSheet(stlSheet,uploaderUsername);
        String sheetName = coreSheet.getName();
        if (!sheetName2SheetDataList.containsKey(sheetName)) {
            List<CoreSheet> coreSheets = new ArrayList<>();
            coreSheets.add(coreSheet);
            SheetData sheetData = new SheetData(sheetName,uploaderUsername);
            sheetData.setSheetVersions(coreSheets);
            sheetName2SheetDataList.put(sheetName,sheetData);
        }
        else {
            throw new ExistingSheetException("A sheet with the same name already exists");
        }
    }

    @Override
    public String getXMLOfSheet(String sheetName) {
        CoreSheet coreSheet = sheetName2SheetDataList.get(sheetName).getSheetVersions().getLast();

        return FileIOHandler.getXMLOfObject(coreSheet);
    }

    private void validateXMLSheetLayout(STLSheet stlSheet) {
        Optional<STLSheet> optionalSTLSheet = Optional.ofNullable(stlSheet);
        if (optionalSTLSheet.isPresent()) {
            STLLayout stlLayout = optionalSTLSheet.get().getSTLLayout();
            int rows = stlLayout.getRows();
            int columns = stlLayout.getColumns();
            if (rows > MAX_ROWS || columns > MAX_COLS || rows < 1 || columns < 1 ) {
                throw new InvalidXMLException("Sheet layout is invalid", String.valueOf(rows) + "," + String.valueOf(columns));
            }
        }
    }

    @Override
    public Cell getSpecificCell(String sheetName, String cellName) {
        CoreSheet coreSheet = sheetName2SheetDataList.get(sheetName).getSheetVersions().getLast();
        return CoordinateFactory.getCellObjectFromCellID(coreSheet, cellName);
    }

    @Override
    public synchronized void updateSpecificCell(String cellName, String originalExpression, String sheetName, String editingUsername) {
        List<CoreSheet> coreSheets = sheetName2SheetDataList.get(sheetName).getSheetVersions();
        CoreSheet cloned = coreSheets.getLast().cloneWithSerialization();
        cloned.incrementVersion();
        cloned.initializeNumOfCellsChanged();
        CoreCell coreCell, toUpdate;
        coreCell = CoordinateFactory.getCellObjectFromCellID(cloned, cellName);
        if (coreCell != null) {
            toUpdate = coreCell;
        }
        else {
            Coordinates coordinates = new Coordinates(cellName);
            toUpdate = new CoreCell(cloned, coordinates.getRow(), coordinates.getCol());
            cloned.getCoreCellsMap().put(coordinates,toUpdate);
        }
        toUpdate.executeCalculationProcedure(originalExpression,editingUsername);
        coreSheets.addLast(cloned);
    }

    private DTOSheet generateDTOSheet(CoreSheet coreSheet) {
        return new DTOSheet(coreSheet);
    }

    private CoreSheet makeSubSheet(String sheetName, String fromCellID, String toCellID) {
        CoreSheet coreSheet = sheetName2SheetDataList.get(sheetName).getSheetVersions().getLast();
        int numOfRows, numOfCols;
        Coordinates topLeftCoordinates = new Coordinates(fromCellID);
        Coordinates bottomRightCoordinates = new Coordinates(toCellID);
        Range.validateRange(coreSheet,topLeftCoordinates.getRow(), topLeftCoordinates.getCol(),
                bottomRightCoordinates.getRow(), bottomRightCoordinates.getCol());
        return new CoreSheet(coreSheet,topLeftCoordinates,bottomRightCoordinates);
    }

    @Override
    public List<Integer> sort(String sheetName, List<String> colNames, String fromCellID, String toCellID, boolean isSortingFirstRow) {
        Sheet subSheet = makeSubSheet(sheetName, fromCellID, toCellID);
        return Sorter.sortRowsByColumns(subSheet,colNames,isSortingFirstRow);
    }

    @Override
    public List<Object> getEffectiveValuesInSpecificCol(String sheetName, String colName, String fromCellID, String toCellID) {
        Sheet subSheet = makeSubSheet(sheetName,fromCellID, toCellID);
        return Filter.getEffectiveValuesInSpecificCol(subSheet,colName);
    }

    @Override
    public Set<Integer> filter(String sheetName, String colName, List<Object> effectiveValuesToFilterBy, String fromCellID, String toCellID, boolean isFilteringEmptyCells) {
        Sheet subSheet = makeSubSheet(sheetName, fromCellID, toCellID);
        return Filter.filter(subSheet,colName,effectiveValuesToFilterBy,isFilteringEmptyCells);
    }

    @Override
    public List<SheetMetaData> getAllSheetsMetaData() {
        List<SheetMetaData> result = new ArrayList<>();
        sheetName2SheetDataList.forEach((username, sheetData) -> {
            result.add(new SheetMetaData(sheetData.getName(),sheetData.getOwnerUsername(),
                    sheetData.getSheetVersions().getLast().getNumOfRows(),
                    sheetData.getSheetVersions().getLast().getNumOfCols(),
                    sheetData.getPermissions(),sheetData.getPendingRequests(),sheetData.getDeniedRequests()));
        });
        return result;
    }

    @Override
    public void requestPermission(String requestingUsername, String sheetName, PermissionType permissionType) {
        SheetData sheetData = sheetName2SheetDataList.get(sheetName);
        sheetData.addPermissionRequest(new PermissionRequest(requestingUsername,permissionType));
    }

    @Override
    public void applyAccessDecision(String usernameWithPendingRequest, String sheetName,
                                    PermissionType permissionType, boolean isAccessAllowed) {
        SheetData sheetData = sheetName2SheetDataList.get(sheetName);
        sheetData.ApplyPermissionAccessDecision(usernameWithPendingRequest,permissionType,isAccessAllowed);
    }

    @Override
    public Sheet previewSpecificUpdateOnCell(String cellName, String originalExpression, String sheetName, int sheetVersion,String editingUsername) {
        List<CoreSheet> coreSheets = sheetName2SheetDataList.get(sheetName).getSheetVersions();
        int versionNumber = sheetVersion >=0 ? sheetVersion : coreSheets.size() - 1;
        CoreSheet cloned = coreSheets.get(versionNumber).cloneWithSerialization();
        cloned.incrementVersion();
        cloned.initializeNumOfCellsChanged();
        CoreCell coreCell, toUpdate;
        coreCell = CoordinateFactory.getCellObjectFromCellID(cloned, cellName);
        if (coreCell != null) {
            toUpdate = coreCell;
        }
        else {
            Coordinates coordinates = new Coordinates(cellName);
            toUpdate = new CoreCell(cloned, coordinates.getRow(), coordinates.getCol());
            cloned.getCoreCellsMap().put(coordinates,toUpdate);
        }
        toUpdate.executeCalculationProcedure(originalExpression,editingUsername);
        return generateDTOSheet(cloned);
    }

    @Override
    public boolean isExistPendingPermissionRequest(String sheetName, String username, boolean isWrite) {
        SheetData sheetData = sheetName2SheetDataList.get(sheetName);
        boolean res = false;
        if (sheetData == null) {
            throw new NoExistenceException("Sheet "+sheetName+" does not exist");
        }
        else {
            PermissionRequest permissionRequest = sheetData.getPendingRequest(username);
            if (permissionRequest != null) {
                if (isWrite) {
                    if (permissionRequest.getPermissionType() == PermissionType.WRITE) res = true;
                }
                else {
                    if (permissionRequest.getPermissionType() == PermissionType.READ) res = true;
                }
            }
        }
        return res;
    }
}
