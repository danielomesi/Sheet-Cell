package gui.scenes.dashboard.sheetsTable;

import gui.scenes.dashboard.main.DashboardMainController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SheetsTableController {

    private DashboardMainController dashboardMainController;

    @FXML
    private TableView<SheetTableEntry> tableView;

    @FXML
    private ScrollPane wrapper;

    public void setDashboardMainController(DashboardMainController DashboardMainController) {this.dashboardMainController = DashboardMainController;}

    @FXML
    public void initialize() {
        // Bind the columns to the Sheet properties
        TableColumn<SheetTableEntry, String> uploaderColumn = (TableColumn<SheetTableEntry, String>) tableView.getColumns().get(0);
        uploaderColumn.setCellValueFactory(new PropertyValueFactory<>("uploader"));

        TableColumn<SheetTableEntry, String> sheetNameColumn = (TableColumn<SheetTableEntry, String>) tableView.getColumns().get(1);
        sheetNameColumn.setCellValueFactory(new PropertyValueFactory<>("sheetName"));

        TableColumn<SheetTableEntry, String> sheetSizeColumn = (TableColumn<SheetTableEntry, String>) tableView.getColumns().get(2);
        sheetSizeColumn.setCellValueFactory(new PropertyValueFactory<>("sheetSize"));

        TableColumn<SheetTableEntry, String> accessLevelColumn = (TableColumn<SheetTableEntry, String>) tableView.getColumns().get(3);
        accessLevelColumn.setCellValueFactory(new PropertyValueFactory<>("accessLevel"));
    }

    public void addTableEntry() {
        SheetTableEntry tableEntry = new SheetTableEntry("John","Beginner","8x10","READ");
        tableView.getItems().add(tableEntry);
    }
}
