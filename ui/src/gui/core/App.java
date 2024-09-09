package gui.core;

import gui.components.center.CenterController;
import gui.components.header.HeaderController;
import gui.components.main.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

//make version combo box be binded to the module member which is version number
//make a button to activate/disable animations (such as load file delay...)
//delete all the printstacktrace all over the code
//stop making sizetoscene call when choosing a cell because all the time because it constantly changes the size of the window
//add a button "resize to natural size" to fix the sizetoscene thing
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
        StackPane headerPane = headerLoader.load();
        HeaderController headerController = headerLoader.getController();

        FXMLLoader centerLoader = new FXMLLoader(getClass().getResource("/gui/components/center/center.fxml"));
        AnchorPane centerPane = centerLoader.load();
        CenterController centerController = centerLoader.getController();

        // Make main controller know its sub controllers
        mainController.setHeaderController(headerController);
        mainController.setCenterController(centerController);

        //Make sub controllers know the main controller
        headerController.setMainController(mainController);
        centerController.setMainController(mainController);

        // Add the headerPane to the top of the root layout
        BorderPane root = (BorderPane) mainController.getMainBorderPane();
        root.setTop(headerPane);
        root.setCenter(centerPane);
    }
}
