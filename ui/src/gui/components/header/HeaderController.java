package gui.components.header;

import entities.cell.Cell;
import entities.coordinates.Coordinates;
import entities.sheet.Sheet;
import gui.components.center.CenterController;
import gui.components.main.MainController;
import gui.utils.Utils;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class HeaderController {

    private MainController mainController;

    @FXML
    private VBox vBoxHeader;

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

    public void resetVersionComboBoxChoice() {
        versionComboBox.getSelectionModel().clearSelection();
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        if (mainController != null) {
            CenterController centerController = mainController.getCenterController();
            BooleanProperty isSheetLoadedProperty = mainController.getIsSheetLoaded();
            updateButton.disableProperty().bind(isSheetLoadedProperty.not().or(centerController.isSelectedCellProperty().not()));
            versionComboBox.disableProperty().bind(isSheetLoadedProperty.not());
            saveToFileButton.disableProperty().bind(isSheetLoadedProperty.not());
            newValueTextField.disableProperty().bind(isSheetLoadedProperty.not());
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
        int chosenVersion = versionComboBox.getSelectionModel().getSelectedIndex();
        if (chosenVersion >= 0 && chosenVersion<versionComboBox.getItems().size() - 1) {
            mainController.generateVersionWindow(chosenVersion);
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

    public void updateMyControlsOnFileLoad() {
        ComboBox<Integer> integerComboBox = (ComboBox<Integer>) versionComboBox;
        SimpleIntegerProperty simpleIntegerProperty = mainController.getDataModule().getVersionNumber();

        Runnable updateComboBoxItems = () -> {
            int maxValue = simpleIntegerProperty.get();
            EventHandler<ActionEvent> originalOnAction = integerComboBox.getOnAction();
            integerComboBox.setOnAction(null);
            integerComboBox.setItems(FXCollections.observableArrayList(
                    java.util.stream.IntStream.rangeClosed(1, maxValue).boxed().toList()));
            //unfortunately, the "setItems" method invokes the on action method of the version combo box,
            //so the current solution is to nullify the on action before calling "setItems" and then returning its original value
            integerComboBox.setOnAction(originalOnAction);
        };

        simpleIntegerProperty.addListener((observable, oldValue, newValue) -> updateComboBoxItems.run());
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

}




