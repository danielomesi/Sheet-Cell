package gui.components.header;

import engine.Engine;
import gui.components.main.MainController;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;

public class HeaderController {

    @FXML
    private Label currentCellIDLabel;

    @FXML
    private Label filePathLabel;

    @FXML
    private Button loadFileButton;

    @FXML
    private TextField newValueTextField;

    @FXML
    private Button updateButton;

    @FXML
    private ComboBox<?> versionComboBox;

    private MainController mainController;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        if (mainController != null) {
            updateButton.disableProperty().bind(mainController.getIsSheetLoaded().not());
            versionComboBox.disableProperty().bind(mainController.getIsSheetLoaded().not());
        }
    }

    @FXML
    void handleUpdateOnClick(ActionEvent event) {

    }

    @FXML
    void handleVersionOnChoose(ActionEvent event) {

    }

    @FXML
    void handleLoadFileButtonClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Data Files", "*.dat"));
        
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow(); 
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            Task<Void> task = getTaskToExecuteFileLoading(file);

            task.setOnSucceeded(e -> {
                // Code to execute on successful completion
                filePathLabel.setText("File loaded successfully: " + file.getName());
            });

            task.setOnFailed(e -> {
                // Code to execute if task fails
                filePathLabel.setText("Failed to load file: " + task.getException().getMessage());
            });

            task.setOnCancelled(e -> {
                // Code to execute if task is cancelled
                filePathLabel.setText("File loading was cancelled.");
            });

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        }
    }

    private Task<Void> getTaskToExecuteFileLoading(File file) {
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    String path = file.getCanonicalPath();
                    mainController.loadFile(path);
                } catch (Exception e) {
                    // Handle any exceptions thrown by mainController.loadFile
                    updateMessage("Error: " + e.getMessage());
                    throw e;
                }
                return null;
            }
        };
    }

}




