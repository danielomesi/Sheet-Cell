package gui.scenes.dashboard.main;

import gui.core.ClientApp;
import gui.scenes.dashboard.header.DashboardHeaderController;
import gui.scenes.dashboard.sheetsTable.SheetsTableController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DashboardMainController {
    private Stage stage;
    private ClientApp clientApp;
    private String username;

    //sub-controllers
    private DashboardHeaderController headerController;
    private SheetsTableController sheetsTableController;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private ScrollPane mainScrollPane;

    public void setUsername(String username) {this.username = username;}
    public void setClientApp(ClientApp clientApp) {this.clientApp = clientApp;}
    public void setStage(Stage stage) {this.stage = stage;}
    public void setHeaderController(DashboardHeaderController headerController) {this.headerController = headerController;}
    public void setSheetsTableController(SheetsTableController sheetsTableController) {this.sheetsTableController = sheetsTableController;}
    public SheetsTableController getSheetsTableController() {return sheetsTableController;}
    public BorderPane getMainBorderPane() {return mainBorderPane;}
}