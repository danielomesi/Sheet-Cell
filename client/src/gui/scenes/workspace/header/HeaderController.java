package gui.scenes.workspace.header;

import constants.Constants;
import entities.permission.PermissionType;
import entities.sheet.DTOSheet;
import entities.sheet.Sheet;
import gui.builder.ControllersBuilder;
import gui.builder.DynamicSheet;
import gui.builder.DynamicSheetBuilder;
import gui.scenes.workspace.main.MainController;
import gui.utils.Utils;
import http.HttpClientMessenger;
import http.MyCallBack;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import javafx.beans.property.BooleanProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import json.GsonInstance;
import okhttp3.HttpUrl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import static constants.Constants.*;

public class HeaderController {

    private MainController mainController;

    @FXML
    private Label mainLabel;
    @FXML
    private Label connectedUserLabel;
    @FXML
    private Button saveFileButton;

    //task progress controls
    @FXML
    private ProgressBar taskProgressBar;
    @FXML
    private Label taskStatusLabel;

    //getters
    public Label getTaskStatusLabel() {return taskStatusLabel;}
    public ProgressBar getTaskProgressBar() {return taskProgressBar;}

    //setters
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        if (mainController != null) {
            BooleanProperty isSheetLoadedProperty = mainController.getIsSheetLoaded();
            setHeaderLabel();
        }
    }

    public void setHeaderLabel() {
        boolean isWriteAccessAllowed = mainController.getIsWriteAccessAllowed().get();
        String permission = isWriteAccessAllowed ? "WRITE" : "READ";
        mainLabel.setText("Sheet View [" + permission + " Mode]");
    }

    public void setConnectedUserLabel(String username) {
        connectedUserLabel.setText(CONNECTED_USER_MESSAGE(username));
    }

    @FXML
    void saveFileButtonClicked(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save XML File");

        // Set extension filter to allow only .xml files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // Open the save dialog
        File file = fileChooser.showSaveDialog(((Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow()));

        if (file != null) {
            // Ensure the file has the .xml extension
            if (!file.getName().toLowerCase().endsWith(".xml")) {
                file = new File(file.getAbsolutePath() + ".xml");
            }
            saveFileToXML(file);
        }
    }

    private void saveFileToXML(File file) {
        String finalUrl = HttpUrl
                .parse(SHEET_XML)
                .newBuilder()
                .addQueryParameter("name",mainController.getCurrentSheetName())
                .addQueryParameter("file",Utils.removeExtension(file.getName()))
                .build()
                .toString();


        HttpClientMessenger.sendGetRequestWithoutBodyAsync(finalUrl, new MyCallBack(taskStatusLabel,(body -> {
            String xml = GsonInstance.getGson().fromJson(body,String.class);
            try {
                Files.write(file.toPath(), xml.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            }
            catch (IOException e) {System.out.println(e.getMessage());}

        })));

    }
}




