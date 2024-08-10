package service;

import service.entities.Cell;
import service.entities.Sheet;
import service.utils.Utils;

public class EngineImpl implements Engine {
    Sheet sheet;

    public Sheet getSheet() {return sheet;}

    @Override
    public void loadSheetFromXMLFile(String fullFilePath) {
    }

    @Override
    public void loadSheetFromDummyData() {
        Sheet sheet = new Sheet(7, 7); // 7 rows, 7 columns

        // Populate with explicit dummy data
        Utils.getCellObjectFromCellName(sheet, "A1").setOriginalAndEffectiveValues("3");
        Utils.getCellObjectFromCellName(sheet, "B1").setOriginalAndEffectiveValues("Hello");
        Utils.getCellObjectFromCellName(sheet, "C1").setOriginalAndEffectiveValues("true");
        Utils.getCellObjectFromCellName(sheet, "D1").setOriginalAndEffectiveValues("8.5");
        Utils.getCellObjectFromCellName(sheet, "E1").setOriginalAndEffectiveValues("World");
        Utils.getCellObjectFromCellName(sheet, "F1").setOriginalAndEffectiveValues("false");
        Utils.getCellObjectFromCellName(sheet, "G1").setOriginalAndEffectiveValues("1.5");

        Utils.getCellObjectFromCellName(sheet, "A2").setOriginalAndEffectiveValues("7");
        Utils.getCellObjectFromCellName(sheet, "B2").setOriginalAndEffectiveValues("Test");
        Utils.getCellObjectFromCellName(sheet, "C2").setOriginalAndEffectiveValues("true");
        Utils.getCellObjectFromCellName(sheet, "D2").setOriginalAndEffectiveValues("9.0");
        Utils.getCellObjectFromCellName(sheet, "E2").setOriginalAndEffectiveValues("Data");
        Utils.getCellObjectFromCellName(sheet, "F2").setOriginalAndEffectiveValues("false");
        Utils.getCellObjectFromCellName(sheet, "G2").setOriginalAndEffectiveValues("2.5");

        Utils.getCellObjectFromCellName(sheet, "A3").setOriginalAndEffectiveValues("4");
        Utils.getCellObjectFromCellName(sheet, "B3").setOriginalAndEffectiveValues("Sample");
        Utils.getCellObjectFromCellName(sheet, "C3").setOriginalAndEffectiveValues("false");
        Utils.getCellObjectFromCellName(sheet, "D3").setOriginalAndEffectiveValues("7.7");
        Utils.getCellObjectFromCellName(sheet, "E3").setOriginalAndEffectiveValues("Value");
        Utils.getCellObjectFromCellName(sheet, "F3").setOriginalAndEffectiveValues("true");
        Utils.getCellObjectFromCellName(sheet, "G3").setOriginalAndEffectiveValues("3.0");

        Utils.getCellObjectFromCellName(sheet, "A4").setOriginalAndEffectiveValues("5");
        Utils.getCellObjectFromCellName(sheet, "B4").setOriginalAndEffectiveValues("Example");
        Utils.getCellObjectFromCellName(sheet, "C4").setOriginalAndEffectiveValues("true");
        Utils.getCellObjectFromCellName(sheet, "D4").setOriginalAndEffectiveValues("6.2");
        Utils.getCellObjectFromCellName(sheet, "E4").setOriginalAndEffectiveValues("Test");
        Utils.getCellObjectFromCellName(sheet, "F4").setOriginalAndEffectiveValues("false");
        Utils.getCellObjectFromCellName(sheet, "G4").setOriginalAndEffectiveValues("4.5");

        Utils.getCellObjectFromCellName(sheet, "A5").setOriginalAndEffectiveValues("6");
        Utils.getCellObjectFromCellName(sheet, "B5").setOriginalAndEffectiveValues("Data");
        Utils.getCellObjectFromCellName(sheet, "C5").setOriginalAndEffectiveValues("false");
        Utils.getCellObjectFromCellName(sheet, "D5").setOriginalAndEffectiveValues("8.0");
        Utils.getCellObjectFromCellName(sheet, "E5").setOriginalAndEffectiveValues("Info");
        Utils.getCellObjectFromCellName(sheet, "F5").setOriginalAndEffectiveValues("true");
        Utils.getCellObjectFromCellName(sheet, "G5").setOriginalAndEffectiveValues("5.5");

        Utils.getCellObjectFromCellName(sheet, "A6").setOriginalAndEffectiveValues("8");
        Utils.getCellObjectFromCellName(sheet, "B6").setOriginalAndEffectiveValues("String");
        Utils.getCellObjectFromCellName(sheet, "C6").setOriginalAndEffectiveValues("true");
        Utils.getCellObjectFromCellName(sheet, "D6").setOriginalAndEffectiveValues("10.1");
        Utils.getCellObjectFromCellName(sheet, "E6").setOriginalAndEffectiveValues("More");
        Utils.getCellObjectFromCellName(sheet, "F6").setOriginalAndEffectiveValues("false");
        Utils.getCellObjectFromCellName(sheet, "G6").setOriginalAndEffectiveValues("6.7");

        Utils.getCellObjectFromCellName(sheet, "A7").setOriginalAndEffectiveValues("9");
        Utils.getCellObjectFromCellName(sheet, "B7").setOriginalAndEffectiveValues("Value");
        Utils.getCellObjectFromCellName(sheet, "C7").setOriginalAndEffectiveValues("false");
        Utils.getCellObjectFromCellName(sheet, "D7").setOriginalAndEffectiveValues("5.5");
        Utils.getCellObjectFromCellName(sheet, "E7").setOriginalAndEffectiveValues("End");
        Utils.getCellObjectFromCellName(sheet, "F7").setOriginalAndEffectiveValues("true");
        Utils.getCellObjectFromCellName(sheet, "G7").setOriginalAndEffectiveValues("7.8");

        this.sheet = sheet;
    }


    @Override
    public Cell getSpecificCell(String cellName) {
        return null;
    }

    @Override
    public void UpdateSpecificCell(String cellName, String originalExpression) throws CloneNotSupportedException {
        Sheet cloned = sheet.clone();
        Utils.getCellObjectFromCellName(cloned, cellName).setOriginalAndEffectiveValues(originalExpression);
        sheet = cloned;
    }
}
