package gui.scenes.workspace.main;

import com.google.gson.JsonSyntaxException;
import engine.Engine;
import engine.EngineImpl;
import entities.coordinates.Coordinates;
import entities.sheet.DTOSheet;
import entities.sheet.Sheet;
import gui.builder.DynamicSheetBuilder;
import gui.builder.DynamicSheet;
import gui.scenes.workspace.filter.FilterController;
import gui.scenes.workspace.sheet.SheetController;
import gui.scenes.workspace.header.HeaderController;
import gui.scenes.workspace.commands.CommandsController;
import gui.scenes.workspace.appearance.AppearanceController;
import gui.scenes.workspace.sort.SortController;
import gui.builder.ControllersBuilder;
import gui.core.DataModule;
import gui.utils.Utils;
import http.HttpClientMessenger;
import http.MyResponseHandler;
import http.constants.Constants;
import http.dtos.AddRangeDTO;
import http.dtos.CellUpdateDTO;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import json.GsonInstance;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

public class MainController {

    //Highest wrapper is scroll pane
    @FXML
    private ScrollPane mainScrollPane;
    @FXML
    private BorderPane mainBorderPane;


    private Engine engine;
    private String currentSheetName;
    private Sheet currentLoadedSheet;
    private BooleanProperty isSheetLoaded;
    private DataModule dataModule;
    private List<String> cssStyles;

    //stage
    private Stage stage;

    //sub controllers
    private SheetController sheetController;
    private HeaderController headerController;
    private CommandsController commandsController;
    private AppearanceController appearanceController;
    private SortController sortController;
    private FilterController filterController;

    //getters
    public BorderPane getMainBorderPane() {return mainBorderPane;}
    public Engine getEngine() {return engine;}
    public Sheet getCurrentLoadedSheet() {return currentLoadedSheet;}
    public HeaderController getHeaderController() {return headerController;}
    public SheetController getSheetController() {return sheetController;}
    public CommandsController getCommandsController() {return commandsController;}
    public AppearanceController getAppearanceController() {return appearanceController;}
    public SortController getSortController() {return sortController;}
    public BooleanProperty getIsSheetLoaded() {return isSheetLoaded;}
    public DataModule getDataModule() {return dataModule;}
    public String getCurrentSheetName() {return currentSheetName;}

    //setters
    public void setStage(Stage stage) {this.stage = stage;}
    public void setHeaderController(HeaderController headerController) {this.headerController = headerController;}
    public void setSheetController(SheetController sheetController) {this.sheetController = sheetController;}
    public void setCommandsController(CommandsController commandsController) {this.commandsController = commandsController;}
    public void setAppearanceController(AppearanceController appearanceController) {this.appearanceController = appearanceController;}
    public void setSortController(SortController sortController) {this.sortController = sortController;}

    public void initialize() {
        engine = new EngineImpl();
        dataModule = new DataModule();

        //bind the buttons relevant for sheet
        isSheetLoaded = new SimpleBooleanProperty(false);
    }

    public void loadFile(Sheet sheet) {
        toDoOnSuccessfulFileLoad(sheet);
    }

    public void saveFile(String filePath) {
        engine.saveStateToFile(filePath);
    }

    public void toDoOnSuccessfulFileLoad(Sheet sheet) {
        currentLoadedSheet = sheet;
        currentSheetName = sheet.getName();
        dataModule.buildModule(currentLoadedSheet.getNumOfRows(),currentLoadedSheet.getNumOfCols(),currentLoadedSheet.getRangesNames());
        sheetController.initActionLineControls();
        sheetController.buildMainCellsTableDynamically(currentLoadedSheet);
        sheetController.updateMyControlsOnFileLoad();
        commandsController.updateMyControlsOnFileLoad();
        appearanceController.updateMyControlsOnFileLoad();
        dataModule.updateModule(currentLoadedSheet);
        isSheetLoaded.setValue(true);
    }

    public void calculateCellUpdate(Coordinates coordinates, String originalExpression) {
        String finalUrl = HttpUrl
                .parse(Constants.GET_CELL_ON_UPDATE)
                .newBuilder()
                .build()
                .toString();

        CellUpdateDTO cellUpdateDTO = new CellUpdateDTO(coordinates.getCellID(),originalExpression,currentSheetName);

        HttpClientMessenger.sendPostRequestWithBodyAsync(finalUrl,cellUpdateDTO, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        getHeaderController().getTaskStatusLabel().setText("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                HttpClientMessenger.genericOnResponseHandler(new MyResponseHandler() {
                                                                 @Override
                                                                 public void handle(String body) {
                                                                     updateSheet();
                                                                 }
                                                             },
                        response, getHeaderController().getTaskStatusLabel()
                );
            }
        });
    }

    public void generateVersionWindow(int chosenVersion) {
        Sheet selectedSheet = engine.getSheet(currentSheetName,chosenVersion);
        DynamicSheet DynamicSheet = DynamicSheetBuilder.buildDynamicSheet(selectedSheet);
        DynamicSheet.populateSheetWithData(selectedSheet);
        GridPane gridPane = DynamicSheet.getGridPane();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setMaxWidth(1200);
        scrollPane.setMaxHeight(800);
        scrollPane.setContent(gridPane);
        Scene newScene = new Scene(scrollPane);
        scrollPane.setId("root-container");
        Utils.setStyle(scrollPane,appearanceController.getSelectedStyle());

        Stage versionWindow = new Stage();
        versionWindow.setTitle("Version " + (chosenVersion+1));
        versionWindow.setOnCloseRequest(event -> {sheetController.resetVersionComboBoxChoice();});
        versionWindow.setScene(newScene);
        versionWindow.show();
    }

    public void addRange(String rangeName,String fromCellID,String toCellID) {
        String finalUrl = HttpUrl
                .parse(Constants.RANGE)
                .newBuilder()
                .build()
                .toString();

        AddRangeDTO addRangeDTO = new AddRangeDTO(currentSheetName, rangeName,fromCellID,toCellID);

        HttpClientMessenger.sendPostRequestWithBodyAsync(finalUrl,addRangeDTO ,new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        getHeaderController().getTaskStatusLabel().setText("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                HttpClientMessenger.genericOnResponseHandler(new MyResponseHandler() {
                         @Override
                         public void handle(String body) {
                             updateSheet();
                         }
                     },
                        response, getHeaderController().getTaskStatusLabel()
                );
            }
        });
    }

    public void updateSheet() {
        String finalUrl = HttpUrl
                .parse(Constants.GET_SHEET)
                .newBuilder()
                .addQueryParameter("name",currentSheetName)
                .build()
                .toString();


        HttpClientMessenger.sendGetRequestWithoutBodyAsync(finalUrl, new Callback() {

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Platform.runLater(() ->
                        getHeaderController().getTaskStatusLabel().setText("Something went wrong: " + e.getMessage())
                );
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                HttpClientMessenger.genericOnResponseHandler(new MyResponseHandler() {
                         @Override
                         public void handle(String body) {
                             System.out.println(body);
                             currentLoadedSheet = GsonInstance.getGson().fromJson(body, DTOSheet.class);
                             dataModule.updateModule(currentLoadedSheet);
                             sheetController.resetVersionComboBoxChoice();
                         }
                     }
                        ,
                        response, getHeaderController().getTaskStatusLabel()
                );
            }
        });
    }

    public void deleteRange(String rangeName) {
        engine.deleteRange(currentSheetName, rangeName);
        currentLoadedSheet = engine.getSheet(currentSheetName);
        Platform.runLater(() -> dataModule.updateModule(currentLoadedSheet));
    }

    public void openSortDialog(String fromCellID,String toCellID) {
        engine.setSubSheet(currentSheetName,fromCellID, toCellID);
        Sheet subSheet = engine.getSubSheet();
        Platform.runLater(() -> {
            sheetController.resetStyles();
            DynamicSheet dynamicSheet = DynamicSheetBuilder.buildSubDynamicSheetFromMainSheet(engine.getSheet(currentSheetName),sheetController.getDynamicSheetTable(),fromCellID,toCellID);
            sortController = ControllersBuilder.buildSortController(this, dynamicSheet,fromCellID,toCellID);
            List<String> colNames = Utils.getLettersFromAToTheNLetter(subSheet.getNumOfCols());
            sortController.populateListViewOfAllCols(colNames);
            Utils.openWindow(sortController.getWrapper(), "Sort Dialog");
        });
    }

    public void openFilterDialog(String fromCellID,String toCellID) {
        engine.setSubSheet(currentSheetName,fromCellID, toCellID);
        Sheet subSheet = engine.getSubSheet();
        Platform.runLater(() -> {
            sheetController.resetStyles();
            DynamicSheet dynamicSheet = DynamicSheetBuilder.buildSubDynamicSheetFromMainSheet(engine.getSheet(currentSheetName),sheetController.getDynamicSheetTable(),fromCellID,toCellID);
            filterController = ControllersBuilder.buildFilterController(this, dynamicSheet,fromCellID,toCellID);
            filterController.populateColComboBox(subSheet.getNumOfCols());
            Utils.openWindow(filterController.getWrapper(), "Filter Dialog");
        });
    }

    public void setStyle(String styleFileName) {
        Utils.setStyle(mainScrollPane, styleFileName);
    }
}