package engine;

import entities.Sheet;
import entities.core.CoreCell;
import entities.core.CoreSheet;
import entities.dto.DTOSheet;
import exceptions.NoExistenceException;
import utils.Utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class EngineImpl implements Engine {
    List<CoreSheet> coreSheets;

    public EngineImpl() {
        this.coreSheets = new LinkedList<>();
    }

    @Override
    public Sheet getSheet() {
        return getSheet(coreSheets.size() - 1);
    }

    @Override
    public Sheet getSheet(int version) {
        return Optional.ofNullable(coreSheets.get(version))
                .map(this::generateDTOSheet)
                .orElseThrow(() -> new NoExistenceException("No sheet is available to load data from"));
    }

    @Override
    public void loadSheetFromXMLFile(String fullFilePath) {
    }

    @Override
    public void loadSheetFromDummyData() {
        CoreSheet coreSheets = new CoreSheet(7, 7); // 7 rows, 7 columns

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
    public CoreCell getSpecificCell(String cellName) {
        return null;
    }

    @Override
    public void updateSpecificCell(String cellName, String originalExpression) throws CloneNotSupportedException {
        CoreSheet cloned = coreSheets.getLast().clone();
        cloned.incrementVersion();
        Utils.getCellObjectFromCellID(cloned, cellName).executeCalculationProcedure(originalExpression);
        coreSheets.addLast(cloned);
    }

    private DTOSheet generateDTOSheet(CoreSheet coreSheet) {
        return new DTOSheet(coreSheet);
    }
}
