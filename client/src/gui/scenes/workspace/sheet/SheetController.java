package gui.scenes.workspace.sheet;

import entities.cell.Cell;
import entities.coordinates.Coordinates;
import entities.range.RangeInterface;
import entities.sheet.Sheet;
import gui.animation.BlinkingEffect;
import gui.builder.DynamicSheet;
import gui.scenes.workspace.sheet.cell.CellController;
import gui.components.sheet.cell.TableCellType;
import gui.scenes.workspace.main.MainController;
import gui.core.DataModule;
import gui.builder.DynamicSheetBuilder;
import gui.utils.Utils;
import http.MyCallBack;
import http.RequestScheduler;
import constants.Constants;
import javafx.animation.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import json.GsonInstance;
import okhttp3.HttpUrl;
import static constants.Constants.*;
import java.util.Map;



public class SheetController {

    private SimpleObjectProperty<CellController> selectedCellController;
    private SimpleObjectProperty<CellController> previousSelectedCellController;
    private DynamicSheet dynamicSheet;
    private MainController mainController;
    private final BooleanProperty isSelectingFirstCell = new SimpleBooleanProperty(false);
    private final BooleanProperty isSelectingSecondCell = new SimpleBooleanProperty(false); ;
    private final BooleanProperty is2ValidCellsSelected = new SimpleBooleanProperty(false);
    private final BooleanProperty isSheetSynced = new SimpleBooleanProperty(true);
    @FXML
    private Label currentCellIDLabel;

    @FXML
    private Label lastUpdatedVersionLabel;

    @FXML
    private TextField newValueTextField;
    @FXML
    private Label newVersionNotifyLabel;
    @FXML
    private Button syncButton;
    @FXML
    private Label originalValueLabel;
    @FXML
    private VBox selectedCellsVbox;
    @FXML
    private Button selectCellsButton;
    @FXML
    private Label selectedBottomRightCellLabel;
    @FXML
    private Label selectedTopLeftCellLabel;
    @FXML
    private Label cellsSelectionStatusLabel;

    @FXML
    private ScrollPane sheetWrapperScrollPane;

    @FXML
    private Button updateButton;

    @FXML
    private ComboBox<Integer> versionComboBox;




    //getters
    public Coordinates getSelectedCellCoordinates() {return selectedCellController.get() == null ? null : selectedCellController.get().getCoordinates();}
    public SimpleObjectProperty<CellController> getSelectedCellController() {return selectedCellController;}
    public BooleanProperty getIs2ValidCellsSelected() {return is2ValidCellsSelected;}
    public DynamicSheet getDynamicSheetTable() {return dynamicSheet;}
    public String getSelectedTopLeftCellID() {return selectedTopLeftCellLabel.getText();}
    public String getSelectedBottomRightCellID() {return selectedBottomRightCellLabel.getText();}
    public BooleanProperty getIsSheetSynced() {return isSheetSynced;}

    //setters
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        if (mainController != null) {
            SheetController sheetController = mainController.getSheetController();
            BooleanProperty isSheetLoadedProperty = mainController.getIsSheetLoaded();
            updateButton.disableProperty().bind(isSheetLoadedProperty.not().or(sheetController.getSelectedCellController().isNull()).
                    or(isSheetSynced.not()).or(mainController.getIsWriteAccessAllowed().not()));
            syncButton.visibleProperty().bind(isSheetSynced.not());
            versionComboBox.disableProperty().bind(isSheetLoadedProperty.not());
            newValueTextField.disableProperty().bind(isSheetLoadedProperty.not().or(mainController.getIsWriteAccessAllowed().not()));
            selectCellsButton.disableProperty().bind(isSheetLoadedProperty.not().or(isSelectingFirstCell).
                    or(isSelectingSecondCell));
        }
    }
    public void resetVersionComboBoxChoice() {
        versionComboBox.getSelectionModel().clearSelection();
    }

    public void initialize() {
        selectedCellController = new SimpleObjectProperty<>();
        previousSelectedCellController = new SimpleObjectProperty<>();

        selectedCellsVbox.visibleProperty().bind(is2ValidCellsSelected);
        selectedCellController.addListener((observable, oldCellController, newCellController) -> {
            previousSelectedCellController.set(oldCellController);
            if (oldCellController != null) {
                oldCellController.setColorStyle("default-cell");
            }
            if (newCellController != null) {
                newCellController.setColorStyle("selected-cell");
            }
        });
    }

    public void buildMainCellsTableDynamically(Sheet sheet) {
        dynamicSheet = DynamicSheetBuilder.buildDynamicSheet(sheet);
        GridPane gridPane = dynamicSheet.getGridPane();
        Map<Coordinates,CellController> cellControllersMap = dynamicSheet.getCoordinates2CellController();
        DataModule dataModule = mainController.getDataModule();

        int numRows = sheet.getNumOfRows();
        int numCols = sheet.getNumOfCols();

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                Coordinates coordinates = new Coordinates(row,col);
                CellController cellController = cellControllersMap.get(coordinates);
                cellController.bindToModule(dataModule.getCoordinates2EffectiveValues().get(coordinates));
                Label cellLabel = cellController.getLabel();
                cellLabel.setOnMouseClicked(event -> handleCellClick(coordinates));
            }
        }

        sheetWrapperScrollPane.setContent(gridPane);
        drawAnimationsIfNeeded(gridPane);
    }

    private void handleCellClick(Coordinates clickedCellCoordinates) {
        CellController cellController = dynamicSheet.getCoordinates2CellController().get(clickedCellCoordinates);
        if (cellController != selectedCellController.get()) {
            resetStyles();
            selectedCellController.set(cellController);
            populateActionLineControlsOnCellChoose(clickedCellCoordinates);
            if (cellController.getTableCellType() == TableCellType.DATA && !isRangeChoice(clickedCellCoordinates)) {
                Cell clickedCell = mainController.getCurrentLoadedSheet().
                        getCell(clickedCellCoordinates.getRow(), clickedCellCoordinates.getCol());
                if (clickedCell!= null) {
                    clickedCell.getCellsAffectingMe().forEach(dependentCell ->
                            dynamicSheet.getCoordinates2CellController().get(dependentCell).setColorStyle("affecting-cell"));

                    clickedCell.getCellsAffectedByMe().forEach(affectedCell ->
                            dynamicSheet.getCoordinates2CellController().get(affectedCell).setColorStyle("affected-cell"));
                }
            }
        }
    }

    private boolean isRangeChoice(Coordinates clickedCellCoordinates) {
        String topLeftCellID, bottomRightCellID;
        boolean res = isSelectingFirstCell.get() || isSelectingSecondCell.get();
        if (isSelectingFirstCell.get()) {
            cellsSelectionStatusLabel.setText("Please select the bottom right cell of the requested cell area");
            isSelectingFirstCell.set(false);
        }
        else if (isSelectingSecondCell.get()) {
            if (Utils.compareCoordinates(previousSelectedCellController.get().getCoordinates(), clickedCellCoordinates)) {
                topLeftCellID = previousSelectedCellController.get().getCoordinates().getCellID();
                bottomRightCellID = clickedCellCoordinates.getCellID();
                updateSelectedCellSIDLabel(topLeftCellID, bottomRightCellID);
                highlightChosenRangeCells(mainController.getCommandsController().getSelectedRange());
                cellsSelectionStatusLabel.setTextFill(Color.GREEN);
                cellsSelectionStatusLabel.setText("");
                isSelectingSecondCell.set(false);
                is2ValidCellsSelected.set(true);
            }
            else {
                cellsSelectionStatusLabel.setTextFill(Color.RED);
                cellsSelectionStatusLabel.setText("A valid choice select is one where the first cell is prior (row-wise and column-wise) to the second cell");
            }

        }
        return res;
    }

    public void highlightChosenRangeCells(RangeInterface range) {
        if (range != null) {
            resetStyles();
            for(Coordinates coordinates : range.getCells()) {
                CellController cellController = dynamicSheet.getCoordinates2CellController().get(coordinates);
                cellController.setColorStyle("range-cell");
            }
        }
    }

    public void resetStyles() {
        dynamicSheet.getCoordinates2CellController().forEach((c, cellController) -> {cellController.setColorStyle("default-cell");});
    }

    public void resetProperties() {
        selectedCellController.set(null);
        is2ValidCellsSelected.set(false);
        isSelectingFirstCell.set(false);
        isSelectingSecondCell.set(false);
    }

    public void updateMyControlsOnFileLoad() {
        resetProperties();
        SimpleIntegerProperty simpleIntegerProperty = mainController.getDataModule().getVersionNumber();

        Runnable updateComboBoxItems = () -> {
            int maxValue = simpleIntegerProperty.get();
            EventHandler<ActionEvent> originalOnAction = versionComboBox.getOnAction();
            versionComboBox.setOnAction(null);
            versionComboBox.setItems(FXCollections.observableArrayList(
                    java.util.stream.IntStream.rangeClosed(1, maxValue).boxed().toList()));
            //unfortunately, the "setItems" method invokes the on action method of the version combo box,
            //so the current solution is to nullify the on action before calling "setItems" and then returning its original value
            versionComboBox.setOnAction(originalOnAction);
        };

        simpleIntegerProperty.addListener((observable, oldValue, newValue) -> updateComboBoxItems.run());
    }

    public void populateActionLineControlsOnCellChoose(Coordinates cellCoordinates) {
        String cellID = cellCoordinates.getCellID();
        currentCellIDLabel.setText(cellID);
        Cell cell = mainController.getCurrentLoadedSheet().getCell(cellCoordinates.getRow(), cellCoordinates.getCol());
        String originalExpression = cell != null ? cell.getOriginalExpression() : NON_EXISTING_CELL_NAME;
        int lastUpdatedVersion = cell != null ? cell.getVersion() : 0;
        originalValueLabel.setText(originalExpression);
        lastUpdatedVersionLabel.setText(String.valueOf(lastUpdatedVersion));
    }

    public void initActionLineControls() {
        currentCellIDLabel.setText("");
        originalValueLabel.setText("");
        lastUpdatedVersionLabel.setText("");
    }

    public void updateSelectedCellSIDLabel(String topLeftText, String bottomRightText) {
        selectedTopLeftCellLabel.setText(topLeftText);
        selectedBottomRightCellLabel.setText(bottomRightText);
    }

    private void drawAnimationsIfNeeded(GridPane gridPane) {
        boolean isAnimationsEnabled = mainController.getAppearanceController().isAnimationsEnabled();
        if (isAnimationsEnabled) {
            RotateTransition rotateTransition = createRotateTransition(gridPane);
            TranslateTransition translateTransition =  applyTranslateTransitionAffect(gridPane);
            SequentialTransition sequentialTransition = new SequentialTransition(rotateTransition,translateTransition);
            sequentialTransition.play();
            dynamicSheet.applyFadeTransitionForAllCells();
        }
    }

    private RotateTransition createRotateTransition(GridPane gridPane) {
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(1.5), gridPane);
        rotateTransition.setByAngle(360); // Rotate 360 degrees
        rotateTransition.setCycleCount(1);
        rotateTransition.setAutoReverse(true);

        return rotateTransition;
    }

    private TranslateTransition applyTranslateTransitionAffect(GridPane gridPane) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.75), gridPane);
        translateTransition.setByY(-100);
        translateTransition.setCycleCount(2);
        translateTransition.setAutoReverse(true);

        return translateTransition;
    }

    public void startVersionRefresher() {
        String sheetName = mainController.getCurrentSheetName();
        String finalUrl = HttpUrl
                .parse(Constants.NUM_OF_VERSIONS)
                .newBuilder()
                .addQueryParameter("name",sheetName)
                .build()
                .toString();

        RequestScheduler.startHttpRequestScheduler(finalUrl,new MyCallBack(mainController.getHeaderController().getTaskStatusLabel(),
                (body -> {
                    int numOfVersions = GsonInstance.getGson().fromJson(body, Integer.class);
                    int currentMaximumVersion = mainController.getDataModule().getVersionNumber().get();
                    if (numOfVersions > currentMaximumVersion) {
                        isSheetSynced.set(false);
                        BlinkingEffect.startEffect(newVersionNotifyLabel,"New version Detected! Click sync to update");
                    }
                    else {
                        isSheetSynced.set(true);
                    }
                })));
    }

    @FXML
    void handleSelectCellsButtonClick(ActionEvent event) {
        cellsSelectionStatusLabel.setTextFill(Color.BLACK);
        cellsSelectionStatusLabel.setText("Please select the top left cell of the requested cell area");
        is2ValidCellsSelected.set(false);
        isSelectingFirstCell.set(true);
        isSelectingSecondCell.set(true);
    }

    @FXML
    void handleUpdateOnClick(ActionEvent event) {
        Coordinates coordinates = getSelectedCellCoordinates();
        Runnable runnable = () -> {
            mainController.calculateCellUpdate(coordinates,newValueTextField.getText());
            newValueTextField.clear();
        };
        Label taskStatusLabel = mainController.getHeaderController().getTaskStatusLabel();
        ProgressBar progressBar = mainController.getHeaderController().getTaskProgressBar();
        boolean isAnimationsEnabled = mainController.getAppearanceController().isAnimationsEnabled();
        Task<Void> task = Utils.getTaskFromRunnable(runnable,taskStatusLabel,progressBar, isAnimationsEnabled);
        Utils.runTaskInADaemonThread(task);
    }

    @FXML
    void handleVersionOnChoose(ActionEvent event) {
        int chosenVersion = versionComboBox.getSelectionModel().getSelectedIndex();
        if (chosenVersion >= 0 && chosenVersion<versionComboBox.getItems().size() - 1) {
            mainController.generateVersionWindow(chosenVersion);
        }

    }

    @FXML
    void syncButtonClicked(ActionEvent event) {
        mainController.updateSheet();
    }

    public void makeOnSync() {
        isSheetSynced.set(true);
        BlinkingEffect.finishEffect(newVersionNotifyLabel,null);
    }

}
