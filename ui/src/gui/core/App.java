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
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.IOException;


//handle the case when the sort/filter window is containing a big sheet,
// but when I enlarge the window, the scroll pane won't move with it
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
        VBox headerNode = headerLoader.load();
        HeaderController headerController = headerLoader.getController();

        FXMLLoader sheetLoader = new FXMLLoader(getClass().getResource("/gui/components/sheet/sheet.fxml"));
        VBox sheetNode = sheetLoader.load();
        SheetController sheetController = sheetLoader.getController();

        FXMLLoader commandsLoader = new FXMLLoader(getClass().getResource("/gui/components/commands/commands.fxml"));
        VBox commandsNode = commandsLoader.load();
        CommandsController commandsController = commandsLoader.getController();

        FXMLLoader appearanceLoader = new FXMLLoader(getClass().getResource("/gui/components/appearance/appearance.fxml"));
        VBox appearanceNode = appearanceLoader.load();
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
        root.setTop(headerNode);
        root.setCenter(sheetNode);
        root.setLeft(commandsNode);
        root.setRight(appearanceNode);

        BorderPane.setAlignment(commandsNode, Pos.TOP_LEFT);
        BorderPane.setAlignment(appearanceNode, Pos.TOP_RIGHT);
    }
}
