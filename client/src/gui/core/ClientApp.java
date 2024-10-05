package gui.core;

import gui.scenes.dashboard.header.DashboardHeaderController;
import gui.scenes.dashboard.main.DashboardMainController;
import gui.scenes.login.LoginController;
import gui.scenes.workspace.appearance.AppearanceController;
import gui.scenes.workspace.commands.CommandsController;
import gui.scenes.workspace.header.HeaderController;
import gui.scenes.workspace.main.MainController;
import gui.scenes.workspace.sheet.SheetController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApp extends Application {
    private MainController workspaceMainController;
    private DashboardMainController dashboardMainController;
    private LoginController loginController;
    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        loadLogin();

    }

    public void loadLogin() throws IOException {
        Parent root = setupAndGetLoginMainComponent();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Sheet Cell");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Parent setupAndGetLoginMainComponent() throws IOException {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/gui/scenes/login/login.fxml"));
        ScrollPane root = mainLoader.load();
        loginController = mainLoader.getController();

        return root;
    }

    public void loadDashBoard() throws IOException {
        Parent root = setupAndGetDashboardMainComponent();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Sheet Cell");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private Parent setupAndGetDashboardMainComponent() throws IOException {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/gui/scenes/dashboard/main/main.fxml"));
        ScrollPane root = mainLoader.load();
        dashboardMainController = mainLoader.getController();
        dashboardMainController.setStage(primaryStage);

        loadSubControllersOfDashboard();
        return root;
    }

    private void loadSubControllersOfDashboard() throws IOException {
        FXMLLoader headerLoader = new FXMLLoader(getClass().getResource("/gui/scenes/dashboard/header/header.fxml"));
        Parent headerNode = headerLoader.load();
        DashboardHeaderController headerController = headerLoader.getController();

        // Make main controller know its sub controllers
        dashboardMainController.setHeaderController(headerController);


        //Make sub controllers know the main controller
        headerController.setMainController(dashboardMainController);


        // Add the headerPane to the top of the root layout
        BorderPane root = dashboardMainController.getMainBorderPane();
        root.setTop(headerNode);
    }

    public void loadWorkspace() throws IOException {
        Parent root = setupAndGetWorkspaceMainComponent();
        Scene scene = new Scene(root);
        primaryStage.setTitle("Sheet Cell");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Parent setupAndGetWorkspaceMainComponent() throws IOException {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/gui/scenes/workspace/main/main.fxml"));
        ScrollPane root = mainLoader.load();
        workspaceMainController = mainLoader.getController();
        workspaceMainController.setStage(primaryStage);

        loadSubControllersOfWorkspace();
        return root;
    }

    private void loadSubControllersOfWorkspace() throws IOException {
        FXMLLoader headerLoader = new FXMLLoader(getClass().getResource("/gui/scenes/workspace/header/header.fxml"));
        VBox headerNode = headerLoader.load();
        HeaderController headerController = headerLoader.getController();

        FXMLLoader sheetLoader = new FXMLLoader(getClass().getResource("/gui/scenes/workspace/sheet/sheet.fxml"));
        VBox sheetNode = sheetLoader.load();
        SheetController sheetController = sheetLoader.getController();

        FXMLLoader commandsLoader = new FXMLLoader(getClass().getResource("/gui/scenes/workspace/commands/commands.fxml"));
        VBox commandsNode = commandsLoader.load();
        CommandsController commandsController = commandsLoader.getController();

        FXMLLoader appearanceLoader = new FXMLLoader(getClass().getResource("/gui/scenes/workspace/appearance/appearance.fxml"));
        VBox appearanceNode = appearanceLoader.load();
        AppearanceController appearanceController = appearanceLoader.getController();



        // Make main controller know its sub controllers
        workspaceMainController.setHeaderController(headerController);
        workspaceMainController.setSheetController(sheetController);
        workspaceMainController.setCommandsController(commandsController);
        workspaceMainController.setAppearanceController(appearanceController);

        //Make sub controllers know the main controller
        headerController.setMainController(workspaceMainController);
        sheetController.setMainController(workspaceMainController);
        commandsController.setMainController(workspaceMainController);
        appearanceController.setMainController(workspaceMainController);

        // Add the headerPane to the top of the root layout
        BorderPane root = workspaceMainController.getMainBorderPane();
        root.setTop(headerNode);
        root.setCenter(sheetNode);
        root.setLeft(commandsNode);
        root.setRight(appearanceNode);

        BorderPane.setAlignment(commandsNode, Pos.TOP_LEFT);
        BorderPane.setAlignment(appearanceNode, Pos.TOP_RIGHT);
    }
}
