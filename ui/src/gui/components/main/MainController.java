package gui.components.main;

import engine.Engine;
import engine.EngineImpl;
import entities.cell.Cell;
import entities.coordinates.Coordinates;
import entities.sheet.Sheet;
import gui.components.center.CenterController;
import gui.components.header.HeaderController;
import gui.exceptions.UnsupportedFileFormat;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainController {

    //consider deleting this member since its not the highest wrapper
    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private ScrollPane mainScrollPane;

    private Engine engine;
    private Sheet currentLoadedSheet;
    private BooleanProperty isSheetLoaded;

    //state
    private Stage stage;

    //sub controllers
    private CenterController centerController;
    private HeaderController headerController;

    public Engine getEngine() {return engine;}
    public Sheet getCurrentLoadedSheet() {return currentLoadedSheet;}
    public void setStage(Stage stage) {this.stage = stage;}
    public void setHeaderController(HeaderController headerController) {this.headerController = headerController;}
    public void setCenterController(CenterController centerController) {this.centerController = centerController;}
    public BorderPane getMainBorderPane() {return mainBorderPane;}
    public BooleanProperty getIsSheetLoaded() {return isSheetLoaded;}

    public void initialize() {
        engine = new EngineImpl();

        //bind the buttons relevant for sheet
        isSheetLoaded = new SimpleBooleanProperty(false);
    }

    public void loadFile(String filePath) {
        if (filePath.endsWith(".xml")) {
            engine.loadSheetFromXMLFile(filePath);
        }
        else if (filePath.endsWith(".dat")) {
            engine.loadStateFromFile(filePath);
        }
        else {
            throw new UnsupportedFileFormat("The selected file is not supported");
        }

        currentLoadedSheet = engine.getSheet();
        notifySubComponentsOnFileLoad(engine.getSheetList());

        isSheetLoaded.setValue(true);
    }

    public void notifySubComponentsOnFileLoad(List<Sheet> sheetList) {
        Platform.runLater(() -> {
            centerController.buildCellsTableDynamically(sheetList.getLast());
            headerController.updateMyControllersOnFileLoad(sheetList);
            stage.sizeToScene();
        } );
    }

    public Set<Cell> getSetOfCells(Set<Coordinates> coordinatesList) {
        Set<Cell> cells = new HashSet<>();
        for (Coordinates coordinates : coordinatesList) {
            cells.add(currentLoadedSheet.getCell(coordinates.getRow(), coordinates.getCol()));
        }

        return cells;
    }

    public void populateChosenCellDataInHeader(Cell chosenCell) {
        headerController.populateHeaderControlsOnCellChoose(chosenCell);
        stage.sizeToScene();
    }
}