package gui.scenes.dashboard.main;

import constants.Constants;
import gui.core.ClientApp;
import gui.scenes.Themable;
import gui.scenes.dashboard.header.DashboardHeaderController;
import gui.scenes.dashboard.permissionsTable.PermissionsTableController;
import gui.scenes.dashboard.sheetsTable.SheetsTableController;
import gui.utils.Utils;
import http.HttpClientMessenger;
import http.MyCallBack;
import http.RequestScheduler;
import javafx.fxml.FXML;
import javafx.scene.control.Control;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import okhttp3.HttpUrl;

import java.io.Closeable;
import java.util.Objects;

import static constants.Constants.*;

public class DashboardMainController implements Closeable, Themable {
    private Stage stage;
    private ClientApp clientApp;
    private String username;
    private String currentStyle;

    //sub-controllers
    private DashboardHeaderController headerController;
    private SheetsTableController sheetsTableController;
    private PermissionsTableController permissionsTableController;

    public void initialize() {

        Utils.setStyle(mainScrollPane, DEFAULT_STYLE);
        currentStyle = DEFAULT_STYLE;
    }

    @Override
    public Control GetThemeWrapper() {
        return mainScrollPane;
    }

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
    public String getCurrentStyle() {return currentStyle;}
    //setters
    public void setUsername(String username) {this.username = username;}
    public void setClientApp(ClientApp clientApp) {this.clientApp = clientApp;}
    public void setStage(Stage stage) {this.stage = stage;}
    public void setHeaderController(DashboardHeaderController headerController) {this.headerController = headerController;}
    public void setSheetsTableController(SheetsTableController sheetsTableController) {this.sheetsTableController = sheetsTableController;}
    public void setPermissionsTableController(PermissionsTableController permissionsTableController) {this.permissionsTableController = permissionsTableController;}
    public void setCurrentStyle(String newStyle) {currentStyle = newStyle;}

    public void stopRefresher() {
        RequestScheduler.stopHttpRequestScheduler();
    }

    public void startRefresher() {
        sheetsTableController.startRefreshingTableData();
    }

    public void logout() {
        String finalUrl = HttpUrl
                .parse(LOGOUT)
                .newBuilder()
                .build()
                .toString();

        HttpClientMessenger.sendGetRequestWithoutBodyAsync(finalUrl,new MyCallBack(headerController.getTaskStatusLabel(), (body) -> {}));
    }

    @Override
    public void close() {
        stopRefresher();
        logout();
    }

    public void setStyle(String currentStyle) {
        Utils.setStyle(mainScrollPane, currentStyle);
        this.currentStyle = currentStyle;
    }
}