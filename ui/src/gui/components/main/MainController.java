package gui.components.main;

import engine.Engine;
import engine.EngineImpl;
import entities.sheet.DTOSheet;
import entities.sheet.Sheet;
import gui.components.header.HeaderController;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;

public class MainController {

    @FXML
    private BorderPane mainBorderPane;
    private Engine engine;
    private HeaderController headerController;
    private Sheet sheet;
    private BooleanProperty isSheetLoaded;

    public void initialize() {
        engine = new EngineImpl();

        //bind the buttons relevant for sheet
        isSheetLoaded = new SimpleBooleanProperty(false);


    }

    public void loadFile(String filePath) {
        if (filePath.endsWith(".xml")) {
            engine.loadSheetFromXMLFile(filePath);
        }
        if (filePath.endsWith(".dat")) {
            engine.loadStateFromFile(filePath);
        }
        isSheetLoaded.setValue(true);
    }

    public Engine getEngine() {return engine;}
    public void setHeaderController(HeaderController headerController) {this.headerController = headerController;}
    public BorderPane getMainBorderPane() {return mainBorderPane;}
    public BooleanProperty getIsSheetLoaded() {return isSheetLoaded;}
}