package gui.app;

import gui.components.header.HeaderController;
import gui.components.main.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIApp extends Application {
    private MainController mainController;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane root = loadMainLayout();
        Scene scene = new Scene(root);
        stage.setTitle("Sheet Cell");
        stage.setScene(scene);
        stage.show();
    }

    private BorderPane loadMainLayout() throws IOException {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/gui/components/main/main.fxml"));
        BorderPane root = mainLoader.load();
        mainController = mainLoader.getController();

        setupHeaderController();
        return root;
    }

    private void setupHeaderController() throws IOException {
        FXMLLoader headerLoader = new FXMLLoader(getClass().getResource("/gui/components/header/header.fxml"));
        ScrollPane headerPane = headerLoader.load();
        HeaderController headerController = headerLoader.getController();

        // Set controllers to each other
        mainController.setHeaderController(headerController);
        headerController.setMainController(mainController);

        // Add the headerPane to the top of the root layout
        BorderPane root = (BorderPane) mainController.getMainBorderPane();
        root.setTop(headerPane);
    }
}
