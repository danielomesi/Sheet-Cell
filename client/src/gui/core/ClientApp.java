package gui.core;

import gui.components.dashboard.header.HeaderController;
import gui.components.main.DashboardController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApp extends Application {
    private DashboardController dashboardController;

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
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/gui/components/main/dashboard.fxml"));
        ScrollPane root = mainLoader.load();
        dashboardController = mainLoader.getController();

        loadSubControllers();
        return root;
    }

    private void loadSubControllers() throws IOException {
        FXMLLoader headerLoader = new FXMLLoader(getClass().getResource("/gui/components/dashboard/header/header.fxml"));
        VBox headerNode = headerLoader.load();
        HeaderController headerController = headerLoader.getController();



        // Make main controller know its sub controllers
        dashboardController.setHeaderController(headerController);

        //Make sub controllers know the main controller
        headerController.setDashboardController(dashboardController);

        // Add the headerPane to the top of the root layout
        VBox root = dashboardController.getMainVBox();
        root.getChildren().add(headerNode);
    }
}
