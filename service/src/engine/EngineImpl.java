package engine;

import entities.cell.Cell;
import entities.cell.DTOCell;
import entities.sheet.Layout;
import entities.sheet.Sheet;
import entities.cell.CoreCell;
import entities.sheet.CoreSheet;
import entities.sheet.DTOSheet;
import entities.stl.STLLayout;
import entities.stl.STLSheet;
import exceptions.CloneFailureException;
import exceptions.InvalidXMLException;
import exceptions.NoExistenceException;
import exceptions.StringIndexOutOfBoundsException;
import jakarta.xml.bind.JAXBException;
import utils.Utils;

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
    public void loadSheetFromXMLFile(String fullFilePath) {
        STLSheet stlSheet;
        try {
            stlSheet = XMLHandler.loadXmlToObject(fullFilePath, STLSheet.class);
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
        Utils.getCellObjectFromCellID(coreSheets, "A1").executeCalculationProcedure("3");
        Utils.getCellObjectFromCellID(coreSheets, "B1").executeCalculationProcedure("Hello");
        Utils.getCellObjectFromCellID(coreSheets, "C1").executeCalculationProcedure("true");
        Utils.getCellObjectFromCellID(coreSheets, "D1").executeCalculationProcedure("8.5");
        Utils.getCellObjectFromCellID(coreSheets, "E1").executeCalculationProcedure("World");
        Utils.getCellObjectFromCellID(coreSheets, "F1").executeCalculationProcedure("false");
        Utils.getCellObjectFromCellID(coreSheets, "G1").executeCalculationProcedure("1.5");

        Utils.getCellObjectFromCellID(coreSheets, "A2").executeCalculationProcedure("7");
        Utils.getCellObjectFromCellID(coreSheets, "B2").executeCalculationProcedure("Test");
        Utils.getCellObjectFromCellID(coreSheets, "C2").executeCalculationProcedure("true");
        Utils.getCellObjectFromCellID(coreSheets, "D2").executeCalculationProcedure("9.0");
        Utils.getCellObjectFromCellID(coreSheets, "E2").executeCalculationProcedure("Data");
        Utils.getCellObjectFromCellID(coreSheets, "F2").executeCalculationProcedure("false");
        Utils.getCellObjectFromCellID(coreSheets, "G2").executeCalculationProcedure("2.5");

        Utils.getCellObjectFromCellID(coreSheets, "A3").executeCalculationProcedure("4");
        Utils.getCellObjectFromCellID(coreSheets, "B3").executeCalculationProcedure("Sample");
        Utils.getCellObjectFromCellID(coreSheets, "C3").executeCalculationProcedure("false");
        Utils.getCellObjectFromCellID(coreSheets, "D3").executeCalculationProcedure("7.7");
        Utils.getCellObjectFromCellID(coreSheets, "E3").executeCalculationProcedure("Value");
        Utils.getCellObjectFromCellID(coreSheets, "F3").executeCalculationProcedure("true");
        Utils.getCellObjectFromCellID(coreSheets, "G3").executeCalculationProcedure("3.0");

        Utils.getCellObjectFromCellID(coreSheets, "A4").executeCalculationProcedure("5");
        Utils.getCellObjectFromCellID(coreSheets, "B4").executeCalculationProcedure("Example");
        Utils.getCellObjectFromCellID(coreSheets, "C4").executeCalculationProcedure("true");
        Utils.getCellObjectFromCellID(coreSheets, "D4").executeCalculationProcedure("6.2");
        Utils.getCellObjectFromCellID(coreSheets, "E4").executeCalculationProcedure("Test");
        Utils.getCellObjectFromCellID(coreSheets, "F4").executeCalculationProcedure("false");
        Utils.getCellObjectFromCellID(coreSheets, "G4").executeCalculationProcedure("4.5");

        Utils.getCellObjectFromCellID(coreSheets, "A5").executeCalculationProcedure("6");
        Utils.getCellObjectFromCellID(coreSheets, "B5").executeCalculationProcedure("Data");
        Utils.getCellObjectFromCellID(coreSheets, "C5").executeCalculationProcedure("false");
        Utils.getCellObjectFromCellID(coreSheets, "D5").executeCalculationProcedure("8.0");
        Utils.getCellObjectFromCellID(coreSheets, "E5").executeCalculationProcedure("Info");
        Utils.getCellObjectFromCellID(coreSheets, "F5").executeCalculationProcedure("true");
        Utils.getCellObjectFromCellID(coreSheets, "G5").executeCalculationProcedure("5.5");

//        Utils.getCellObjectFromCellID(coreSheets, "A6").executeCalculationProcedure("8");
//        Utils.getCellObjectFromCellID(coreSheets, "B6").executeCalculationProcedure("String");
//        Utils.getCellObjectFromCellID(coreSheets, "C6").executeCalculationProcedure("true");
//        Utils.getCellObjectFromCellID(coreSheets, "D6").executeCalculationProcedure("10.1");
//        Utils.getCellObjectFromCellID(coreSheets, "E6").executeCalculationProcedure("More");
//        Utils.getCellObjectFromCellID(coreSheets, "F6").executeCalculationProcedure("false");
//        Utils.getCellObjectFromCellID(coreSheets, "G6").executeCalculationProcedure("6.7");

        Utils.getCellObjectFromCellID(coreSheets, "A7").executeCalculationProcedure("9");
        Utils.getCellObjectFromCellID(coreSheets, "B7").executeCalculationProcedure("Value");
        Utils.getCellObjectFromCellID(coreSheets, "C7").executeCalculationProcedure("false");
        Utils.getCellObjectFromCellID(coreSheets, "D7").executeCalculationProcedure("5.5");
        Utils.getCellObjectFromCellID(coreSheets, "E7").executeCalculationProcedure("End");
        Utils.getCellObjectFromCellID(coreSheets, "F7").executeCalculationProcedure("true");
        Utils.getCellObjectFromCellID(coreSheets, "G7").executeCalculationProcedure("7.8");

        this.coreSheets.addLast(coreSheets);
    }

    @Override
    public Cell getSpecificCell(String cellName) {
        return Utils.getCellObjectFromCellID(coreSheets.getLast(), cellName);
    }

    @Override
    public void updateSpecificCell(String cellName, String originalExpression) {
        CoreSheet cloned = null;
        try {
            cloned = coreSheets.getLast().clone();
        } catch (CloneNotSupportedException e) {
            throw new CloneFailureException("Failed to make a clone of the last sheet");
        }

        cloned.incrementVersion();
        Utils.getCellObjectFromCellID(cloned, cellName).executeCalculationProcedure(originalExpression);
        coreSheets.addLast(cloned);
    }

    private DTOSheet generateDTOSheet(CoreSheet coreSheet) {
        return new DTOSheet(coreSheet);
    }
}
