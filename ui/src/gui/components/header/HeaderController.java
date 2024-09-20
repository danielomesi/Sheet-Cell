package gui.components.header;

import gui.components.main.MainController;
import gui.utils.Utils;
import javafx.beans.property.BooleanProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class HeaderController {

    private MainController mainController;

    //load and save controls
    @FXML
    private Label filePathLabel;
    @FXML
    private Button saveToFileButton;

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
            saveToFileButton.disableProperty().bind(isSheetLoadedProperty.not());
        }
    }

    @FXML
    void handleSaveToFileButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Data Files", "*.dat"));

        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            boolean isAnimationsEnabled = mainController.getAppearanceController().isAnimationsEnabled();
            Task<Void> task = Utils.getTaskFromRunnable(() -> mainController.saveFile(file.getAbsolutePath())
                    , taskStatusLabel,taskProgressBar, isAnimationsEnabled);
            Utils.runTaskInADaemonThread(task);
        }
    }

    @FXML
    void handleLoadFileButtonClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML Files", "*.xml")
                ,new FileChooser.ExtensionFilter("Data Files", "*.dat"));
        
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow(); 
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            boolean isAnimationsEnabled = mainController.getAppearanceController().isAnimationsEnabled();
            Task<Void> task = Utils.getTaskFromRunnable(() -> mainController.loadFile(file.getAbsolutePath())
                    , taskStatusLabel,taskProgressBar, isAnimationsEnabled);
            task.setOnSucceeded(e -> {
                filePathLabel.setText(file.getName());
            });

            Utils.runTaskInADaemonThread(task);
        }
    }




}




