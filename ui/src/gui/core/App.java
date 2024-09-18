package gui.core;

import entities.coordinates.CoordinateFactory;
import entities.sheet.Sheet;
import gui.components.sheet.SheetController;
import gui.components.header.HeaderController;
import gui.components.commands.CommandsController;
import gui.components.main.MainController;
import gui.components.appearance.AppearanceController;
import gui.components.sort.SortController;
import gui.exceptions.RowOutOfBoundsException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;


//public static String removeFirstRowOutOfFromCellID(Sheet sheet, String fromCellID) {
//    int fromRow = CoordinateFactory.getRowIndexFromCellID(fromCellID);
//    int fromCol = CoordinateFactory.getColIndexFromCellID(fromCellID);
//    validateNextRowExistence(sheet,fromRow);
//    fromRow++;
//
//    return CoordinateFactory.getCellIDFromIndices(fromRow,fromCol);
//}
//
//public static String removeFirstRowOutOfToCellID(Sheet sheet, String toCellID) {
//    int toRow = CoordinateFactory.getRowIndexFromCellID(toCellID);
//    int toCol = CoordinateFactory.getColIndexFromCellID(toCellID);
//    if (toRow == 0) {
//        validateNextRowExistence(sheet,toRow);
//        toRow++;
//        toCellID = CoordinateFactory.getCellIDFromIndices(toRow,toCol);
//    }
//
//    return toCellID;
//}
//
//public static void validateNextRowExistence(Sheet sheet, int row) {
//    int lastRowNumber = sheet.getNumOfRows()-1; //0-indexed
//    int nextRowNumber = row + 1;
//    if (nextRowNumber > lastRowNumber) {
//        throw new RowOutOfBoundsException("Can't find row number " + nextRowNumber);
//    }
//}

//think where to put the code above - probably in engine ?
//fix the bug in sorting not working
//do not forget that the cell style determined by USER needs to stay after sorting/filtering etc
//choose an arbitrary number in my opinion for every component and for the main component
//add separators between sections in app
//fix the bug of "add range" button enabled when choosing bad top left and bottom right?
//fix the thing when you enter a number in the range name when adding a new range, so that it will work
//make the app launch with the user defined layout requests!!
//add the java fxml right/left/center to the main fxml as included (need to see how to in the video)
//make the exception to string happen just on the frontend part
//make a button to activate/disable animations (such as load file delay...)
//delete all the "print stack trace" all over the code
//fix the proper window size thing
public class App extends Application {
    private MainController mainController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        ScrollPane root = loadAndSetupMainPane(stage);
        Scene scene = new Scene(root);
        stage.setTitle("Sheet Cell");
        stage.setScene(scene);
        stage.show();
    }

    private ScrollPane loadAndSetupMainPane(Stage stage) throws IOException {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/gui/components/main/main.fxml"));
        ScrollPane root = mainLoader.load();
        mainController = mainLoader.getController();
        mainController.setStage(stage);

        loadSubControllers();
        return root;
    }

    private void loadSubControllers() throws IOException {
        FXMLLoader headerLoader = new FXMLLoader(getClass().getResource("/gui/components/header/header.fxml"));
        VBox headerVbox = headerLoader.load();
        HeaderController headerController = headerLoader.getController();

        FXMLLoader sheetLoader = new FXMLLoader(getClass().getResource("/gui/components/sheet/sheet.fxml"));
        VBox sheetVbox = sheetLoader.load();
        SheetController sheetController = sheetLoader.getController();

        FXMLLoader commandsLoader = new FXMLLoader(getClass().getResource("/gui/components/commands/commands.fxml"));
        VBox commandsVbox = commandsLoader.load();
        CommandsController commandsController = commandsLoader.getController();

        FXMLLoader appearanceLoader = new FXMLLoader(getClass().getResource("/gui/components/appearance/appearance.fxml"));
        VBox appearanceVbox = appearanceLoader.load();
        AppearanceController appearanceController = appearanceLoader.getController();



        // Make main controller know its sub controllers
        mainController.setHeaderController(headerController);
        mainController.setSheetController(sheetController);
        mainController.setCommandsController(commandsController);
        mainController.setAppearanceController(appearanceController);

        //Make sub controllers know the main controller
        headerController.setMainController(mainController);
        sheetController.setMainController(mainController);
        commandsController.setMainController(mainController);
        appearanceController.setMainController(mainController);

        // Add the headerPane to the top of the root layout
        BorderPane root = mainController.getMainBorderPane();
        root.setTop(headerVbox);
        root.setCenter(sheetVbox);
        root.setLeft(commandsVbox);
        root.setRight(appearanceVbox);
    }
}
