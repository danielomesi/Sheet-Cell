package gui.scenes.dashboard.main;

import gui.core.ClientApp;
import gui.scenes.dashboard.header.DashboardHeaderController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class DashboardMainController {
    private Stage stage;
    private ClientApp clientApp;

    //sub-controllers
    private DashboardHeaderController headerController;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private ScrollPane mainScrollPane;

    public void setClientApp(ClientApp clientApp) {this.clientApp = clientApp;}
    public void setStage(Stage stage) {this.stage = stage;}
    public void setHeaderController(DashboardHeaderController headerController) {this.headerController = headerController;}
    public BorderPane getMainBorderPane() {return mainBorderPane;}
}