package gui.scenes.dashboard.header;
import gui.scenes.dashboard.main.DashboardMainController;
import http.HttpClientMessenger;
import http.MyCallBack;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import static constants.Constants.*;

public class DashboardHeaderController {
    private DashboardMainController mainController;

    @FXML
    private Label connectedAsLabel;

    @FXML
    private Button loadFileButton;

    @FXML
    private Label taskStatusLabel;

    @FXML
    private VBox vBoxHeader;

    //getters
    public Label getTaskStatusLabel() {return taskStatusLabel;}

    //setters
    public void setMainController(DashboardMainController mainController) {this.mainController = mainController;}
    public void setConnectedAsLabel(String username) {
        connectedAsLabel.setText(CONNECTED_USER_MESSAGE(username));}

    @FXML
    void handleLoadFileButtonClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");

        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        loadFileAfterSelection(file);
    }

    private void loadFileAfterSelection(File file) {
        if (file != null) {
            HttpClientMessenger.sendFileToServer(file,new MyCallBack(taskStatusLabel,
                    (body -> {})));
        }
    }
}
