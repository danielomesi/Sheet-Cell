package gui.scenes.dashboard.header;

import com.google.gson.Gson;
import entities.sheet.SheetMetaData;
import gui.scenes.dashboard.main.DashboardMainController;
import http.HttpClientMessenger;
import http.MyCallBack;
import http.MyResponseHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class DashboardHeaderController {
    private DashboardMainController mainController;

    @FXML
    private Label greetingLabel;

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

    public Label getTaskStatusLabel() {return taskStatusLabel;}
    public void setMainController(DashboardMainController mainController) {this.mainController = mainController;}

    //
    public void setGreetingLabel(String username) {greetingLabel.setText("Hi " + username + "!");}
    @FXML
    void handleLoadFileButtonClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");

        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML Files", "*.xml")
                ,new FileChooser.ExtensionFilter("Data Files", "*.dat"));

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            HttpClientMessenger.sendFileToServer(file,new MyCallBack(taskStatusLabel,
                    (body -> {})));
        }
    }

    private SheetMetaData getSheetMetaDataFromJson(String json) {
            Gson gson = new Gson();
            return gson.fromJson(json, SheetMetaData.class);
    }

    @FXML
    void handleSaveToFileButton(ActionEvent event) {

    }

}
