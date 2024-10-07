package gui.scenes.dashboard.sheetsTable;

import gui.scenes.dashboard.main.DashboardMainController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;

public class SheetsTableController {

    private DashboardMainController dashboardMainController;

    @FXML
    private TableView<?> tableView;

    @FXML
    private ScrollPane wrapper;

    public void setDashboardMainController(DashboardMainController DashboardMainController) {this.dashboardMainController = DashboardMainController;}

}
