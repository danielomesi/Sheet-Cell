package gui.components.header;

import gui.components.main.MainController;
import gui.utils.Utils;
import javafx.beans.property.BooleanProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
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
    private Label errorMessageLabel;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        if (mainController != null) {
            BooleanProperty isSheetLoadedProperty = mainController.getIsSheetLoaded();
            saveToFileButton.disableProperty().bind(isSheetLoadedProperty.not());
        }
    }

    @FXML
    void handleSaveToFileButton(ActionEvent event) {}

    @FXML
    void handleLoadFileButtonClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Data Files", "*.dat"));
        
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow(); 
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            Task<Void> task = getTaskFromRunnable(() -> mainController.loadFile(file.getAbsolutePath()), false);
            task.setOnSucceeded(e -> {
                filePathLabel.setText(file.getName());
            });

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        }
    }


    public Task<Void> getTaskFromRunnable(Runnable runnable, boolean isDelayed) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (isDelayed) {
                    for (int i = 1; i <= 100; i++) {
                        Thread.sleep(25);
                        updateProgress(i, 100);
                    }
                }
                try {
                    updateMessage("Executing...");
                    runnable.run();
                    updateMessage("Success!");
                } catch (Exception e) {
                    updateMessage(Utils.generateErrorMessageFromException(e));
                    throw e;
                }
                return null;
            }
        };

        errorMessageLabel.textProperty().bind(task.messageProperty());
        if (isDelayed) {
            taskProgressBar.progressProperty().bind(task.progressProperty());
        }
        return task;
    }

    public void showErrorMessage(String message) {
        errorMessageLabel.setText(message);
    }

}




