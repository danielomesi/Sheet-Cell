package engine;

import entities.cell.Cell;
import entities.cell.CoreCell;
import entities.coordinates.Coordinates;
import entities.coordinates.CoordinateFactory;
import entities.range.Range;
import entities.sheet.Layout;
import entities.sheet.Sheet;
import entities.sheet.CoreSheet;
import entities.sheet.DTOSheet;
import entities.stl.STLLayout;
import entities.stl.STLSheet;
import exceptions.InvalidXMLException;
import exceptions.NoExistenceException;
import jakarta.xml.bind.JAXBException;
import utils.Sorter;
import utils.Utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EngineImpl implements Engine {
    private List<CoreSheet> coreSheets;
    private CoreSheet subCoreSheet;

    public EngineImpl() {
        this.coreSheets = new LinkedList<>();
    }
    private final int maxRows = 50;
    private final int maxCols = 20;

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
    public List<Sheet> getSheetList() {
        List<Sheet> sheets = new LinkedList<>();
        coreSheets.forEach(coreSheet -> {
            DTOSheet dtoSheet = generateDTOSheet(coreSheet);
            sheets.add(dtoSheet);
        });

        return sheets;
    }

    @Override
    public void saveStateToFile(String fullFilePath) {
        fullFilePath = Utils.trimQuotes(fullFilePath);
        FileIOHandler.saveCoreSheetsToFile(coreSheets, fullFilePath);
    }

    @Override
    public void loadStateFromFile(String fullFilePath) {
        fullFilePath = Utils.trimQuotes(fullFilePath);
        coreSheets = FileIOHandler.loadCoreSheetsFromFile(fullFilePath);
    }

    @Override
    public void addRange(String rangeName, String fromCellID, String toCellID) {
        coreSheets.getLast().addRange(rangeName, fromCellID, toCellID);
    }

    @Override
    public void deleteRange(String rangeName) {
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
    public void loadSheetFromXMLFile(String fullFilePath) {
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
        coreSheets.clear();
        coreSheets.add(coreSheet);
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
    public void loadSheetFromDummyData() {
        CoreSheet coreSheets = new CoreSheet(2, 2, new Layout(4,4), "Hey"); // 7 rows, 7 columns

        CoordinateFactory.getCellObjectFromCellID(coreSheets, "A1").executeCalculationProcedure("3");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "B1").executeCalculationProcedure("Hello");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "A2").executeCalculationProcedure("true");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "B2").executeCalculationProcedure("8.5");

        this.coreSheets.addLast(coreSheets);
    }

    @Override
    public Cell getSpecificCell(String cellName) {
        return CoordinateFactory.getCellObjectFromCellID(coreSheets.getLast(), cellName);
    }

    @Override
    public void updateSpecificCell(String cellName, String originalExpression) {
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

}
