package engine;

import entities.cell.Cell;
import entities.cell.CoreCell;
import entities.coordinates.Coordinates;
import entities.coordinates.CoordinateFactory;
import entities.range.Range;
import entities.sheet.Sheet;
import entities.sheet.CoreSheet;
import entities.sheet.DTOSheet;
import entities.stl.STLLayout;
import entities.stl.STLSheet;
import exceptions.InvalidXMLException;
import exceptions.NoExistenceException;
import jakarta.xml.bind.JAXBException;
import permission.SheetData;
import utils.Filter;
import utils.Sorter;
import utils.Utils;

import java.util.*;

public class EngineImpl implements Engine {
    private final int maxRows = 50;
    private final int maxCols = 20;
    //private List<CoreSheet> coreSheets;
    private CoreSheet subCoreSheet;
    private Map<String,SheetData> sheetName2SheetDataList;


    public EngineImpl() {
        this.coreSheets = new LinkedList<>();
    }


    @Override
    public Sheet getSheet() {
        if (coreSheets.isEmpty()) {
            return null;
        }
        return getSheet(coreSheets.size() - 1);
    }

    @Override
    public Sheet getSheet(int version) {
        return Optional.ofNullable(coreSheets.get(version))
                .map(this::generateDTOSheet)
                .orElseThrow(() -> new NoExistenceException("No sheet found"));
    }

    @Override
    public synchronized List<Sheet> getSheetList() {
        List<Sheet> sheets = new LinkedList<>();
        coreSheets.forEach(coreSheet -> {
            DTOSheet dtoSheet = generateDTOSheet(coreSheet);
            sheets.add(dtoSheet);
        });

        return sheets;
    }

    @Override
    public synchronized void saveStateToFile(String fullFilePath) {
        fullFilePath = Utils.trimQuotes(fullFilePath);
        FileIOHandler.saveCoreSheetsToFile(coreSheets, fullFilePath);
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
    public synchronized void addRange(String rangeName, String fromCellID, String toCellID) {
        coreSheets.getLast().addRange(rangeName, fromCellID, toCellID);
    }

    @Override
    public synchronized void deleteRange(String rangeName) {
        coreSheets.getLast().deleteRange(rangeName);
    }

    @Override
    public void setSubSheet(String fromCellID, String toCellID) {
        subCoreSheet = makeSubSheet(fromCellID, toCellID);
    }

    @Override
    public Sheet getSubSheet() {
        if (subCoreSheet != null) {
            return generateDTOSheet(subCoreSheet);
        }

        return null;
    }

    @Override
    public synchronized void loadSheetFromXMLFile(String fullFilePath,String uploaderUsername) {
        STLSheet stlSheet;
        fullFilePath = Utils.trimQuotes(fullFilePath);
        try {
            stlSheet = FileIOHandler.loadXMLToObject(fullFilePath, STLSheet.class);
        }
        catch (JAXBException e) {
            throw new InvalidXMLException("Invalid XML file");
        }
        validateXMLSheetLayout(stlSheet);
        CoreSheet coreSheet = new CoreSheet(stlSheet);
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
        CoreSheet coreSheet = new CoreSheet(stlSheet);
        String sheetName = coreSheet.getName();
        if (!sheetName2SheetDataList.containsKey(sheetName)) {
            List<CoreSheet> coreSheets = new ArrayList<>();
            coreSheets.add(coreSheet);
            SheetData sheetData = new SheetData(sheetName,uploaderUsername);
            sheetData.setSheetVersions(coreSheets);
            sheetName2SheetDataList.put(sheetName,sheetData);

        }
    }

    private void validateXMLSheetLayout(STLSheet stlSheet) {
        Optional<STLSheet> optionalSTLSheet = Optional.ofNullable(stlSheet);
        if (optionalSTLSheet.isPresent()) {
            STLLayout stlLayout = optionalSTLSheet.get().getSTLLayout();
            int rows = stlLayout.getRows();
            int columns = stlLayout.getColumns();
            if (rows > maxRows || columns > maxCols || rows < 1 || columns < 1 ) {
                throw new InvalidXMLException("Sheet layout is invalid", String.valueOf(rows) + "," + String.valueOf(columns));
            }
        }
    }

    @Override
    public Cell getSpecificCell(String cellName) {
        return CoordinateFactory.getCellObjectFromCellID(coreSheets.getLast(), cellName);
    }

    @Override
    public synchronized void updateSpecificCell(String cellName, String originalExpression,String sheetName) {
        if ()
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
        toUpdate.executeCalculationProcedure(originalExpression);
        coreSheets.addLast(cloned);
    }

    private DTOSheet generateDTOSheet(CoreSheet coreSheet) {
        return new DTOSheet(coreSheet);
    }

    private CoreSheet makeSubSheet(String fromCellID, String toCellID) {
        int numOfRows, numOfCols;
        Coordinates topLeftCoordinates = new Coordinates(fromCellID);
        Coordinates bottomRightCoordinates = new Coordinates(toCellID);
        Range.validateRange(coreSheets.getLast(),topLeftCoordinates.getRow(), topLeftCoordinates.getCol(),
                bottomRightCoordinates.getRow(), bottomRightCoordinates.getCol());
        return new CoreSheet(coreSheets.getLast(),topLeftCoordinates,bottomRightCoordinates);
    }

    public List<Integer> sort(List<String> colNames, String fromCellID, String toCellID, boolean isSortingFirstRow) {
        Sheet subSheet = makeSubSheet(fromCellID, toCellID);
        return Sorter.sortRowsByColumns(subSheet,colNames,isSortingFirstRow);
    }

    @Override
    public List<Object> getEffectiveValuesInSpecificCol(String colName, String fromCellID, String toCellID) {
        Sheet subSheet = makeSubSheet(fromCellID, toCellID);
        return Filter.getEffectiveValuesInSpecificCol(subSheet,colName);
    }

    @Override
    public Set<Integer> filter(String colName,List<Object> effectiveValuesToFilterBy, String fromCellID, String toCellID, boolean isFilteringEmptyCells) {
        Sheet subSheet = makeSubSheet(fromCellID, toCellID);
        return Filter.filter(subSheet,colName,effectiveValuesToFilterBy,isFilteringEmptyCells);
    }
}
