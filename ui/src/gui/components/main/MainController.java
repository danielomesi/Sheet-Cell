package gui.components.main;

import engine.Engine;
import engine.EngineImpl;
import entities.cell.Cell;
import entities.coordinates.Coordinates;
import entities.sheet.Sheet;
import gui.builder.DynamicBuilder;
import gui.builder.DynamicSheetTable;
import gui.components.center.CenterController;
import gui.components.center.cell.CellController;
import gui.components.center.cell.TableCellType;
import gui.components.header.HeaderController;
import gui.components.left.LeftController;
import gui.components.right.RightController;
import gui.core.DataModule;
import gui.exceptions.UnsupportedFileFormat;
import gui.utils.Utils;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainController {

    //Highest wrapper is scroll pane
    @FXML
    private ScrollPane mainScrollPane;
    @FXML
    private BorderPane mainBorderPane;


    private Engine engine;
    private Sheet currentLoadedSheet;
    private BooleanProperty isSheetLoaded;
    private DataModule dataModule;

    //stage
    private Stage stage;

    //sub controllers
    private CenterController centerController;
    private HeaderController headerController;
    private LeftController leftController;
    private RightController rightController;

    //getters
    public BorderPane getMainBorderPane() {return mainBorderPane;}
    public Engine getEngine() {return engine;}
    public Sheet getCurrentLoadedSheet() {return currentLoadedSheet;}
    public HeaderController getHeaderController() {return headerController;}
    public CenterController getCenterController() {return centerController;}
    public LeftController getLeftController() {return leftController;}
    public RightController getRightController() {return rightController;}
    public BooleanProperty getIsSheetLoaded() {return isSheetLoaded;}
    public DataModule getDataModule() {return dataModule;}

    //setters
    public void setStage(Stage stage) {this.stage = stage;}
    public void setHeaderController(HeaderController headerController) {this.headerController = headerController;}
    public void setCenterController(CenterController centerController) {this.centerController = centerController;}
    public void setLeftController(LeftController leftController) {this.leftController = leftController;}
    public void setRightController(RightController rightController) {this.rightController = rightController;}

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
            dataModule.buildModule(currentLoadedSheet.getNumOfRows(),currentLoadedSheet.getNumOfColumns(),currentLoadedSheet.getRangesNames());
            centerController.buildMainCellsTableDynamically(currentLoadedSheet);
            headerController.updateMyControlsOnFileLoad();
            leftController.updateMyControlsOnFileLoad();
            rightController.updateMyControlsOnFileLoad();
            dataModule.updateModule(currentLoadedSheet);
            isSheetLoaded.setValue(true);
        } );
    }

    public void calculateCellUpdate(Coordinates coordinates, String originalExpression) {
        engine.updateSpecificCell(coordinates.getCellID(), originalExpression);
        currentLoadedSheet = engine.getSheet();
        Platform.runLater(()-> {
            dataModule.updateModule(currentLoadedSheet);
            headerController.resetVersionComboBoxChoice();
        });
    }

    public void generateVersionWindow(int chosenVersion) {
        Sheet selectedSheet = engine.getSheet(chosenVersion);
        DynamicSheetTable dynamicSheetTable = DynamicBuilder.buildDynamicSheetTable(selectedSheet);
        dynamicSheetTable.populateSheetWithData(selectedSheet);
        GridPane gridPane = dynamicSheetTable.getGridPane();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(gridPane);
        Scene newScene = new Scene(scrollPane);

        Stage versionWindow = new Stage();
        versionWindow.setTitle("Version " + (chosenVersion+1));
        versionWindow.setOnCloseRequest(event -> {headerController.resetVersionComboBoxChoice();});
        versionWindow.setScene(newScene);
        versionWindow.show();
    }

    public void addRange(String rangeName,String fromCellID,String toCellID) {
        engine.addRange(rangeName, fromCellID, toCellID);
        currentLoadedSheet = engine.getSheet();
        Platform.runLater(() -> dataModule.updateModule(currentLoadedSheet));
    }

    public void deleteRange(String rangeName) {
        engine.deleteRange(rangeName);
        currentLoadedSheet = engine.getSheet();
        Platform.runLater(() -> dataModule.updateModule(currentLoadedSheet));
    }

}