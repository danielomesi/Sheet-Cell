package gui.components.header;

import entities.cell.Cell;
import entities.coordinates.Coordinates;
import entities.sheet.Sheet;
import gui.components.center.CenterController;
import gui.components.main.MainController;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HeaderController {

    private MainController mainController;

    @FXML
    private StackPane stackPaneWrapper;

    //load and save controls
    @FXML
    private Label filePathLabel;
    @FXML
    private Button loadFileButton;
    @FXML
    private Button saveToFileButton;

    //task progress controls
    @FXML
    private ProgressBar taskProgressBar;
    @FXML
    private Label errorMessageLabel;

    //action row controls
    @FXML
    private Label currentCellIDLabel;
    @FXML
    private Label originalValueLabel;
    @FXML
    private Label lastUpdatedVersionLabel;
    @FXML
    private TextField newValueTextField;
    @FXML
    private Button updateButton;
    @FXML
    private ComboBox<?> versionComboBox;

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        if (mainController != null) {
            CenterController centerController = mainController.getCenterController();
            updateButton.disableProperty().bind(mainController.getIsSheetLoaded().not().or(centerController.isSelectedCellProperty().not()));
            versionComboBox.disableProperty().bind(mainController.getIsSheetLoaded().not());
            saveToFileButton.disableProperty().bind(mainController.getIsSheetLoaded().not());
        }
    }

    @FXML
    void handleUpdateOnClick(ActionEvent event) {
        Coordinates coordinates = mainController.getCenterController().getSelectedCell();
        Task<Void> task = getTaskFromRunnable(() -> mainController.calculateCellUpdate(coordinates,newValueTextField.getText()), false);
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    void handleVersionOnChoose(ActionEvent event) {

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

    public void updateMyControlsOnFileLoad(List<Sheet> sheetList) {
        ComboBox<Integer> integerComboBox = (ComboBox<Integer>) versionComboBox;

        // Set items of the ComboBox using a range from 1 to the size of the sheetList
        integerComboBox.setItems(FXCollections.observableArrayList(
                java.util.stream.IntStream.rangeClosed(1, sheetList.size()).boxed().toList()
        ));

        if (!integerComboBox.getItems().isEmpty()) {
            integerComboBox.getSelectionModel().selectLast();
        }
    }

    public void populateHeaderControlsOnCellChoose(Cell chosenCell) {
        String cellID = chosenCell.getCoordinates().getCellID();
        currentCellIDLabel.setText(cellID);
        originalValueLabel.setText(chosenCell.getOriginalExpression());
        lastUpdatedVersionLabel.setText(String.valueOf(chosenCell.getVersion()));
    }

    private Task<Void> getTaskFromRunnable(Runnable runnable, boolean isDelayed) {
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
                    runnable.run();
                } catch (Exception e) {
                    updateMessage("Error: " + e.getMessage());
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

}




