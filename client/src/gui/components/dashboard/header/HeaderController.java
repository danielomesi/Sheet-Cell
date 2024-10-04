package gui.components.dashboard.header;

import gui.components.main.DashboardController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class HeaderController {

    private DashboardController dashboardController;

    @FXML
    private Label filePathLabel;

    @FXML
    private Button loadFileButton;

    @FXML
    private Button saveToFileButton;

    @FXML
    private ProgressBar taskProgressBar;

    @FXML
    private Label taskStatusLabel;

    @FXML
    private VBox vBoxHeader;

    public void setDashboardController(DashboardController dashboardController) {this.dashboardController = dashboardController;}

    @FXML
    void handleLoadFileButtonClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");

        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML Files", "*.xml")
                ,new FileChooser.ExtensionFilter("Data Files", "*.dat"));

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {

        }
    }

    @FXML
    void handleSaveToFileButton(ActionEvent event) {

    }

}
