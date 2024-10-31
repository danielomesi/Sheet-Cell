package gui.scenes.dashboard.main;

import gui.core.ClientApp;
import gui.scenes.dashboard.header.DashboardHeaderController;
import gui.scenes.dashboard.permissionsTable.PermissionsTableController;
import gui.scenes.dashboard.sheetsTable.SheetsTableController;
import http.RequestScheduler;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.Closeable;
import java.util.Objects;

import static constants.Constants.*;

public class DashboardMainController implements Closeable {
    private Stage stage;
    private ClientApp clientApp;
    private String username;

    //sub-controllers
    private DashboardHeaderController headerController;
    private SheetsTableController sheetsTableController;
    private PermissionsTableController permissionsTableController;

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private ScrollPane mainScrollPane;

    //getters
    public String getUsername() {return username;}
    public SheetsTableController getSheetsTableController() {return sheetsTableController;}
    public DashboardHeaderController getHeaderController() {return headerController;}
    public PermissionsTableController getPermissionsTableController() {return permissionsTableController;}
    public BorderPane getMainBorderPane() {return mainBorderPane;}
    public ClientApp getClientApp() {return clientApp;}
    //setters
    public void setUsername(String username) {this.username = username;}
    public void setClientApp(ClientApp clientApp) {this.clientApp = clientApp;}
    public void setStage(Stage stage) {this.stage = stage;}
    public void setHeaderController(DashboardHeaderController headerController) {this.headerController = headerController;}
    public void setSheetsTableController(SheetsTableController sheetsTableController) {this.sheetsTableController = sheetsTableController;}
    public void setPermissionsTableController(PermissionsTableController permissionsTableController) {this.permissionsTableController = permissionsTableController;}


    public void stopRefresher() {
        RequestScheduler.stopHttpRequestScheduler();
    }

    public void startRefresher() {
        sheetsTableController.startRefreshingTableData();
    }

    @Override
    public void close() {
        stopRefresher();
    }
}