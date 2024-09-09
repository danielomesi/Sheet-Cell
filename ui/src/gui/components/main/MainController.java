package gui.components.main;

import engine.Engine;
import engine.EngineImpl;
import entities.cell.Cell;
import entities.coordinates.Coordinates;
import entities.sheet.Sheet;
import gui.components.center.CenterController;
import gui.components.header.HeaderController;
import gui.core.DataModule;
import gui.exceptions.UnsupportedFileFormat;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainController {

    //consider deleting this member since its not the highest wrapper
    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private ScrollPane mainScrollPane;

    private Engine engine;
    private Sheet currentLoadedSheet;
    private BooleanProperty isSheetLoaded;
    private DataModule dataModule;

    //state
    private Stage stage;

    //sub controllers
    private CenterController centerController;
    private HeaderController headerController;

    //getters
    public Engine getEngine() {return engine;}
    public Sheet getCurrentLoadedSheet() {return currentLoadedSheet;}
    public BorderPane getMainBorderPane() {return mainBorderPane;}
    public CenterController getCenterController() {return centerController;}
    public BooleanProperty getIsSheetLoaded() {return isSheetLoaded;}
    public DataModule getDataModule() {return dataModule;}

    //setters
    public void setStage(Stage stage) {this.stage = stage;}
    public void setHeaderController(HeaderController headerController) {this.headerController = headerController;}
    public void setCenterController(CenterController centerController) {this.centerController = centerController;}


    public void initialize() {
        engine = new EngineImpl();
        dataModule = new DataModule();

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

        toDoOnSuccessfulFileLoad();
    }

    public void toDoOnSuccessfulFileLoad() {
        currentLoadedSheet = engine.getSheet();
        Platform.runLater(() -> {
            dataModule.buildModule(currentLoadedSheet.getNumOfRows(),currentLoadedSheet.getNumOfColumns());
            centerController.buildCellsTableDynamically(currentLoadedSheet);
            headerController.updateMyControlsOnFileLoad(engine.getSheetList());
            dataModule.updateModule(currentLoadedSheet);
            isSheetLoaded.setValue(true);
        } );
    }

    public void populateChosenCellDataInHeader(Cell chosenCell) {
        headerController.populateHeaderControlsOnCellChoose(chosenCell);
        stage.sizeToScene();
    }

    public void calculateCellUpdate(Coordinates coordinates, String originalExpression) {
        engine.updateSpecificCell(coordinates.getCellID(), originalExpression);
        currentLoadedSheet = engine.getSheet();
        Platform.runLater(()-> dataModule.updateModule(currentLoadedSheet));
    }


}