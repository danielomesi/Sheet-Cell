package gui.core;

import entities.permission.PermissionType;
import entities.sheet.Sheet;
import gui.scenes.dashboard.header.DashboardHeaderController;
import gui.scenes.dashboard.main.DashboardMainController;
import gui.scenes.dashboard.permissionsTable.PermissionsTableController;
import gui.scenes.dashboard.sheetsTable.SheetsTableController;
import gui.scenes.login.LoginController;
import gui.scenes.workspace.appearance.AppearanceController;
import gui.scenes.workspace.commands.CommandsController;
import gui.scenes.workspace.header.HeaderController;
import gui.scenes.workspace.main.MainController;
import gui.scenes.workspace.sheet.SheetController;
import gui.utils.CustomWindow;
import gui.utils.Utils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

import static constants.Constants.*;

public class ClientApp extends Application {
    private MainController workspaceMainController;
    private DashboardMainController dashboardMainController;
    private LoginController loginController;
    private Scene dashboardScene;
    private Stage primaryStage;
    private String username;

    @Override
    public void start(Stage stage) throws Exception {
        this.primaryStage = stage;
        primaryStage.setOnCloseRequest((event)->makeOnWindowClose());
        loadLogin();

    }

    private void makeOnWindowClose() {
        if (workspaceMainController != null) {
            workspaceMainController.close();
        }
        if (dashboardMainController != null) {
            dashboardMainController.close();
        }
        Platform.exit();
        System.exit(0);
    }

    public void switchSceneToDashboard(String username) throws IOException {
        this.username = username;
        loadDashBoard();
    }

    public void switchSceneToWorkspace(Sheet sheet, PermissionType permissionType,String currentStyle) throws IOException {
        dashboardMainController.stopRefresher();
        loadWorkspace(sheet,permissionType,currentStyle);
    }

    public void loadLogin() throws IOException {
        Parent root = setupAndGetLoginMainComponent();
        Scene scene = new Scene(root);
        primaryStage.setTitle(LOGIN_SCENE_TITLE);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.centerOnScreen();
    }

    private Parent setupAndGetLoginMainComponent() throws IOException {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource(LOGIN_FXML));
        ScrollPane root = mainLoader.load();
        loginController = mainLoader.getController();
        loginController.setClientApp(this);

        return root;
    }

    public void loadDashBoard() throws IOException {
        Parent root = setupAndGetDashboardMainComponent();
        dashboardScene = new Scene(root);
        primaryStage.setTitle(DASHBOARD_SCENE_TITLE + " [" + username + "]");
        primaryStage.setScene(dashboardScene);
        primaryStage.show();
        primaryStage.centerOnScreen();
    }


    private Parent setupAndGetDashboardMainComponent() throws IOException {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource(DASHBOARD_MAIN_FXML));
        ScrollPane root = mainLoader.load();
        dashboardMainController = mainLoader.getController();
        dashboardMainController.setStage(primaryStage);
        dashboardMainController.setClientApp(this);

        loadSubControllersOfDashboard();
        return root;
    }

    private void loadSubControllersOfDashboard() throws IOException {
        FXMLLoader headerLoader = new FXMLLoader(getClass().getResource(DASHBOARD_HEADER_FXML));
        Parent headerNode = headerLoader.load();
        DashboardHeaderController headerController = headerLoader.getController();

        FXMLLoader sheetsTableLoader = new FXMLLoader(getClass().getResource(DASHBOARD_SHEETS_TABLE_FXML));
        Parent sheetsLoaderNode = sheetsTableLoader.load();
        SheetsTableController sheetsTableController = sheetsTableLoader.getController();

        FXMLLoader permissionsTableLoader = new FXMLLoader(getClass().getResource(DASHBOARD_PERMISSIONS_TABLE_FXML));
        Parent permissionsTableNode = permissionsTableLoader.load();
        PermissionsTableController permissionsTableController = permissionsTableLoader.getController();


        //Make main controller know the user it sits in
        dashboardMainController.setUsername(username);

        // Make main controller know its sub controllers
        dashboardMainController.setHeaderController(headerController);
        dashboardMainController.setSheetsTableController(sheetsTableController);
        dashboardMainController.setPermissionsTableController(permissionsTableController);

        //Make sub controllers know the main controller
        headerController.setMainController(dashboardMainController);
        headerController.setConnectedAsLabel(username);
        sheetsTableController.setDashboardMainController(dashboardMainController);
        permissionsTableController.setDashboardMainController(dashboardMainController);


        // Add the headerPane to the top of the root layout
        BorderPane root = dashboardMainController.getMainBorderPane();
        root.setTop(headerNode);
        root.setCenter(sheetsLoaderNode);
        root.setBottom(permissionsTableNode);
    }

    public void loadWorkspace(Sheet sheet, PermissionType permissionType,String currentStyle) throws IOException {
        Parent root = setupAndGetWorkspaceMainComponent(sheet,permissionType,currentStyle);
        Scene scene = new Scene(root);
        primaryStage.setTitle(WORKSPACE_SCENE_TITLE + " [" + username + "]");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.centerOnScreen();
    }

    private Parent setupAndGetWorkspaceMainComponent(Sheet sheet, PermissionType permissionType,String currentStyle) throws IOException {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource(WORKSPACE_MAIN_FXML));
        ScrollPane root = mainLoader.load();
        workspaceMainController = mainLoader.getController();
        workspaceMainController.setStage(primaryStage);
        workspaceMainController.setClientApp(this);
        workspaceMainController.setAccessAttributes(permissionType);
        workspaceMainController.setStyle(currentStyle);

        loadSubControllersOfWorkspace(sheet);
        return root;
    }

    private void loadSubControllersOfWorkspace(Sheet sheet) throws IOException {
        FXMLLoader headerLoader = new FXMLLoader(getClass().getResource(WORKSPACE_HEADER_FXML));
        VBox headerNode = headerLoader.load();
        HeaderController headerController = headerLoader.getController();

        FXMLLoader sheetLoader = new FXMLLoader(getClass().getResource(WORKSPACE_SHEET_FXML));
        VBox sheetNode = sheetLoader.load();
        SheetController sheetController = sheetLoader.getController();

        FXMLLoader commandsLoader = new FXMLLoader(getClass().getResource(WORKSPACE_COMMANDS_FXML));
        VBox commandsNode = commandsLoader.load();
        CommandsController commandsController = commandsLoader.getController();

        FXMLLoader appearanceLoader = new FXMLLoader(getClass().getResource(WORKSPACE_APPEARANCE_FXML));
        VBox appearanceNode = appearanceLoader.load();
        AppearanceController appearanceController = appearanceLoader.getController();

        //Make main controller know the username
        workspaceMainController.setUsername(username);

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

        workspaceMainController.toDoOnSuccessfulFileLoad(sheet);
    }

    public void switchSceneBackToDashboardFromWorkspace(String currentStyle) {
        dashboardMainController.startRefresher();
        dashboardMainController.setStyle(currentStyle);
        primaryStage.setTitle(DASHBOARD_SCENE_TITLE + " [" + username + "]");
        primaryStage.setScene(dashboardScene);
        primaryStage.show();
        primaryStage.centerOnScreen();
    }
}
