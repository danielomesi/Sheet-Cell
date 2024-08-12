package engine;

import entities.Sheet;
import entities.core.CoreCell;
import entities.core.CoreSheet;
import entities.dto.DTOSheet;
import utils.Utils;

public class EngineImpl implements Engine {
    CoreSheet coreSheet;
    DTOSheet sheet;

    public Sheet getSheet() {
        if (coreSheet != null)
        {
            if (sheet == null || sheet.getVersion() != coreSheet.getVersion()) {
                sheet = generateSheetToUI();
            }
        }
        else {
            sheet = null;
        }

        return sheet;
    }

    private DTOSheet generateSheetToUI() {
        return new DTOSheet(coreSheet);
    }

    @Override
    public void loadSheetFromXMLFile(String fullFilePath) {
    }

    @Override
    public void loadSheetFromDummyData() {
        CoreSheet sheet = new CoreSheet(7, 7); // 7 rows, 7 columns

        // Populate with explicit dummy data
        Utils.getCellObjectFromCellID(sheet, "A1").executeCalculationProcedure("3");
        Utils.getCellObjectFromCellID(sheet, "B1").executeCalculationProcedure("Hello");
        Utils.getCellObjectFromCellID(sheet, "C1").executeCalculationProcedure("true");
        Utils.getCellObjectFromCellID(sheet, "D1").executeCalculationProcedure("8.5");
        Utils.getCellObjectFromCellID(sheet, "E1").executeCalculationProcedure("World");
        Utils.getCellObjectFromCellID(sheet, "F1").executeCalculationProcedure("false");
        Utils.getCellObjectFromCellID(sheet, "G1").executeCalculationProcedure("1.5");

        Utils.getCellObjectFromCellID(sheet, "A2").executeCalculationProcedure("7");
        Utils.getCellObjectFromCellID(sheet, "B2").executeCalculationProcedure("Test");
        Utils.getCellObjectFromCellID(sheet, "C2").executeCalculationProcedure("true");
        Utils.getCellObjectFromCellID(sheet, "D2").executeCalculationProcedure("9.0");
        Utils.getCellObjectFromCellID(sheet, "E2").executeCalculationProcedure("Data");
        Utils.getCellObjectFromCellID(sheet, "F2").executeCalculationProcedure("false");
        Utils.getCellObjectFromCellID(sheet, "G2").executeCalculationProcedure("2.5");

        Utils.getCellObjectFromCellID(sheet, "A3").executeCalculationProcedure("4");
        Utils.getCellObjectFromCellID(sheet, "B3").executeCalculationProcedure("Sample");
        Utils.getCellObjectFromCellID(sheet, "C3").executeCalculationProcedure("false");
        Utils.getCellObjectFromCellID(sheet, "D3").executeCalculationProcedure("7.7");
        Utils.getCellObjectFromCellID(sheet, "E3").executeCalculationProcedure("Value");
        Utils.getCellObjectFromCellID(sheet, "F3").executeCalculationProcedure("true");
        Utils.getCellObjectFromCellID(sheet, "G3").executeCalculationProcedure("3.0");

        Utils.getCellObjectFromCellID(sheet, "A4").executeCalculationProcedure("5");
        Utils.getCellObjectFromCellID(sheet, "B4").executeCalculationProcedure("Example");
        Utils.getCellObjectFromCellID(sheet, "C4").executeCalculationProcedure("true");
        Utils.getCellObjectFromCellID(sheet, "D4").executeCalculationProcedure("6.2");
        Utils.getCellObjectFromCellID(sheet, "E4").executeCalculationProcedure("Test");
        Utils.getCellObjectFromCellID(sheet, "F4").executeCalculationProcedure("false");
        Utils.getCellObjectFromCellID(sheet, "G4").executeCalculationProcedure("4.5");

        Utils.getCellObjectFromCellID(sheet, "A5").executeCalculationProcedure("6");
        Utils.getCellObjectFromCellID(sheet, "B5").executeCalculationProcedure("Data");
        Utils.getCellObjectFromCellID(sheet, "C5").executeCalculationProcedure("false");
        Utils.getCellObjectFromCellID(sheet, "D5").executeCalculationProcedure("8.0");
        Utils.getCellObjectFromCellID(sheet, "E5").executeCalculationProcedure("Info");
        Utils.getCellObjectFromCellID(sheet, "F5").executeCalculationProcedure("true");
        Utils.getCellObjectFromCellID(sheet, "G5").executeCalculationProcedure("5.5");

        Utils.getCellObjectFromCellID(sheet, "A6").executeCalculationProcedure("8");
        Utils.getCellObjectFromCellID(sheet, "B6").executeCalculationProcedure("String");
        Utils.getCellObjectFromCellID(sheet, "C6").executeCalculationProcedure("true");
        Utils.getCellObjectFromCellID(sheet, "D6").executeCalculationProcedure("10.1");
        Utils.getCellObjectFromCellID(sheet, "E6").executeCalculationProcedure("More");
        Utils.getCellObjectFromCellID(sheet, "F6").executeCalculationProcedure("false");
        Utils.getCellObjectFromCellID(sheet, "G6").executeCalculationProcedure("6.7");

        Utils.getCellObjectFromCellID(sheet, "A7").executeCalculationProcedure("9");
        Utils.getCellObjectFromCellID(sheet, "B7").executeCalculationProcedure("Value");
        Utils.getCellObjectFromCellID(sheet, "C7").executeCalculationProcedure("false");
        Utils.getCellObjectFromCellID(sheet, "D7").executeCalculationProcedure("5.5");
        Utils.getCellObjectFromCellID(sheet, "E7").executeCalculationProcedure("End");
        Utils.getCellObjectFromCellID(sheet, "F7").executeCalculationProcedure("true");
        Utils.getCellObjectFromCellID(sheet, "G7").executeCalculationProcedure("7.8");

        this.coreSheet = sheet;
    }


    @Override
    public CoreCell getSpecificCell(String cellName) {
        return null;
    }

    @Override
    public void updateSpecificCell(String cellName, String originalExpression) throws CloneNotSupportedException {
        CoreSheet cloned = coreSheet.clone();
        Utils.getCellObjectFromCellID(cloned, cellName).executeCalculationProcedure(originalExpression);
        coreSheet = cloned;
    }
}
