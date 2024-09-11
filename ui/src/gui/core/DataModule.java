package gui.core;

import entities.cell.Cell;
import entities.coordinates.Coordinates;
import entities.sheet.Sheet;
import gui.utils.Utils;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DataModule {
    private final Map<Coordinates, SimpleStringProperty> coordinates2EffectiveValues = new HashMap<Coordinates, SimpleStringProperty>();
    private final SimpleIntegerProperty versionNumber = new SimpleIntegerProperty(0);
    private final SimpleSetProperty<String> rangesNames = new SimpleSetProperty<String>();

    public Map<Coordinates, SimpleStringProperty> getCoordinates2EffectiveValues() {return coordinates2EffectiveValues;}
    public SimpleIntegerProperty getVersionNumber() {return versionNumber;}
    public SimpleSetProperty<String> getRangesNames() {return rangesNames;}

    public void buildModule(int numOfRows, int numOfColumns, Set<String> rangesNames) {
        coordinates2EffectiveValues.clear();
        for (int i = 0; i < numOfRows; i++) {
            for (int j = 0; j < numOfColumns; j++) {
                Coordinates coordinates = new Coordinates(i, j);
                SimpleStringProperty effectiveValue = new SimpleStringProperty("");
                coordinates2EffectiveValues.put(coordinates,effectiveValue);
            }
        }
        ObservableSet<String> observableRangesNames = FXCollections.observableSet(rangesNames);
        this.rangesNames.set(observableRangesNames);
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
        ObservableSet<String> observableRangesNames = FXCollections.observableSet(sheet.getRangesNames());
        rangesNames.set(observableRangesNames);
    }
}
