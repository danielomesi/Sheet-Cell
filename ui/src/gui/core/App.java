package gui.core;

import gui.components.center.CenterController;
import gui.components.header.HeaderController;
import gui.components.main.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

//ask aviad whether I can make it fail when a static wrong argument is given
//make the exception tostring happen just on the frontend part
//make a button to activate/disable animations (such as load file delay...)
//delete all the printstacktrace all over the code
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
        VBox vboxHeader = headerLoader.load();
        HeaderController headerController = headerLoader.getController();

        FXMLLoader centerLoader = new FXMLLoader(getClass().getResource("/gui/components/center/center.fxml"));
        ScrollPane centerPane = centerLoader.load();
        CenterController centerController = centerLoader.getController();

        // Make main controller know its sub controllers
        mainController.setHeaderController(headerController);
        mainController.setCenterController(centerController);

        //Make sub controllers know the main controller
        headerController.setMainController(mainController);
        centerController.setMainController(mainController);

        // Add the headerPane to the top of the root layout
        BorderPane root = (BorderPane) mainController.getMainBorderPane();
        root.setTop(vboxHeader);
        root.setCenter(centerPane);
    }
}
