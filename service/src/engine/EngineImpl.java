package engine;

import entities.cell.Cell;
import entities.cell.CoreCell;
import entities.coordinates.CellCoordinates;
import entities.coordinates.CoordinateFactory;
import entities.sheet.Layout;
import entities.sheet.Sheet;
import entities.sheet.CoreSheet;
import entities.sheet.DTOSheet;
import entities.stl.STLLayout;
import entities.stl.STLSheet;
import exceptions.InvalidXMLException;
import exceptions.NoExistenceException;
import jakarta.xml.bind.JAXBException;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class EngineImpl implements Engine {
    List<CoreSheet> coreSheets;

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
        FileIOHandler.saveCoreSheetsToFile(coreSheets, fullFilePath);
    }

    @Override
    public void loadStateFromFile(String fullFilePath) {
        coreSheets = FileIOHandler.loadCoreSheetsFromFile(fullFilePath);
    }

    @Override
    public void loadSheetFromXMLFile(String fullFilePath) {
        STLSheet stlSheet;
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
        CoreSheet coreSheets = new CoreSheet(7, 7, new Layout(4,4), "Hey"); // 7 rows, 7 columns

        // Populate with explicit dummy data
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "A1").executeCalculationProcedure("3");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "B1").executeCalculationProcedure("Hello");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "C1").executeCalculationProcedure("true");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "D1").executeCalculationProcedure("8.5");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "E1").executeCalculationProcedure("World");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "F1").executeCalculationProcedure("false");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "G1").executeCalculationProcedure("1.5");

        CoordinateFactory.getCellObjectFromCellID(coreSheets, "A2").executeCalculationProcedure("7");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "B2").executeCalculationProcedure("Test");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "C2").executeCalculationProcedure("true");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "D2").executeCalculationProcedure("9.0");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "E2").executeCalculationProcedure("Data");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "F2").executeCalculationProcedure("false");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "G2").executeCalculationProcedure("2.5");

        CoordinateFactory.getCellObjectFromCellID(coreSheets, "A3").executeCalculationProcedure("4");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "B3").executeCalculationProcedure("Sample");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "C3").executeCalculationProcedure("false");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "D3").executeCalculationProcedure("7.7");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "E3").executeCalculationProcedure("Value");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "F3").executeCalculationProcedure("true");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "G3").executeCalculationProcedure("3.0");

        CoordinateFactory.getCellObjectFromCellID(coreSheets, "A4").executeCalculationProcedure("5");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "B4").executeCalculationProcedure("Example");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "C4").executeCalculationProcedure("true");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "D4").executeCalculationProcedure("6.2");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "E4").executeCalculationProcedure("Test");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "F4").executeCalculationProcedure("false");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "G4").executeCalculationProcedure("4.5");

        CoordinateFactory.getCellObjectFromCellID(coreSheets, "A5").executeCalculationProcedure("6");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "B5").executeCalculationProcedure("Data");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "C5").executeCalculationProcedure("false");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "D5").executeCalculationProcedure("8.0");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "E5").executeCalculationProcedure("Info");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "F5").executeCalculationProcedure("true");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "G5").executeCalculationProcedure("5.5");

        CoordinateFactory.getCellObjectFromCellID(coreSheets, "A7").executeCalculationProcedure("9");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "B7").executeCalculationProcedure("Value");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "C7").executeCalculationProcedure("false");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "D7").executeCalculationProcedure("5.5");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "E7").executeCalculationProcedure("End");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "F7").executeCalculationProcedure("true");
        CoordinateFactory.getCellObjectFromCellID(coreSheets, "G7").executeCalculationProcedure("7.8");

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
            CellCoordinates cellCoordinates = new CellCoordinates(cellName);
            toUpdate = new CoreCell(cloned,cellCoordinates.getRow(), cellCoordinates.getCol());
            cloned.getCoreCellsMap().put(cellCoordinates,toUpdate);
        }
        toUpdate.executeCalculationProcedure(originalExpression);
        coreSheets.addLast(cloned);
    }

    private DTOSheet generateDTOSheet(CoreSheet coreSheet) {
        return new DTOSheet(coreSheet);
    }

}
