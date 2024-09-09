package gui.core;

import entities.cell.Cell;
import entities.coordinates.Coordinates;
import entities.sheet.Sheet;
import gui.utils.Utils;
import javafx.beans.property.*;

import java.util.HashMap;
import java.util.Map;

public class DataModule {
    private Map<Coordinates, SimpleStringProperty> coordinates2EffectiveValues;
    private SimpleIntegerProperty versionNumber;

    public DataModule() {
        coordinates2EffectiveValues = new HashMap<Coordinates, SimpleStringProperty>();
        versionNumber = new SimpleIntegerProperty(0);
    }

    public Map<Coordinates, SimpleStringProperty> getCoordinates2EffectiveValues() {return coordinates2EffectiveValues;}
    public SimpleIntegerProperty getVersionNumber() {return versionNumber;}

    public void buildModule(int numOfRows, int numOfColumns) {
        coordinates2EffectiveValues.clear();
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfColumns; j++) {
                Coordinates coordinates = new Coordinates(i, j);
                SimpleStringProperty effectiveValue = new SimpleStringProperty("");
                coordinates2EffectiveValues.put(coordinates,effectiveValue);
            }
        }
    }

    public void updateModule(Sheet sheet) {
        int numOfRows = sheet.getNumOfRows();
        int numOfColumns = sheet.getNumOfColumns();
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfColumns; j++) {
                Coordinates coordinates = new Coordinates(i, j);
                Cell currentCell = sheet.getCell(i, j);
                String effectiveValue = currentCell != null ? Utils.objectToString(currentCell.getEffectiveValue()) : null;
                coordinates2EffectiveValues.get(coordinates).set(effectiveValue);
            }
        }
        versionNumber.set(sheet.getVersion());
    }
}
