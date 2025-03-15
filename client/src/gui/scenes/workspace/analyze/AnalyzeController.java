package gui.scenes.workspace.analyze;


import constants.Constants;
import entities.coordinates.Coordinates;
import entities.sheet.DTOSheet;
import entities.sheet.Sheet;
import service_exceptions.InvalidArgumentException;
import gui.builder.DynamicSheet;
import gui.builder.DynamicSheetBuilder;
import gui.scenes.workspace.main.MainController;
import gui.utils.Utils;
import http.HttpClientMessenger;
import http.MyCallBack;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import json.GsonInstance;
import okhttp3.HttpUrl;

import java.util.ArrayList;
import java.util.List;

import static constants.Constants.CSS_DEFINITION_FOR_RED_COLOR;

public class AnalyzeController {

    private MainController mainController;
    private String sheetName;
    private int sheetVersion;
    private String cellID;
    private final BooleanProperty isAnalyzeActive = new SimpleBooleanProperty(false);
    private Double step;

    @FXML
    private ScrollPane wrapper;
    @FXML
    private Button resetButton;

    @FXML
    private Button setButton;

    @FXML
    private Label cellIDLabel;

    @FXML
    private TextField maxValTextField;

    @FXML
    private TextField minValTextField;

    @FXML
    private ScrollPane sheetViewScrollPane;

    @FXML
    private Label statusLabel;

    @FXML
    private TextField stepTextField;
    @FXML
    private Slider slider;

    //getters
    public ScrollPane getWrapper() {return wrapper;}

    //setters
    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        if (mainController != null) {
            Utils.setStyle(wrapper, mainController.getCurrentStyle());
        }
    }
    public void setSheetName(String sheetName) {this.sheetName = sheetName;}
    public void setSheetVersion(int sheetVersion) {this.sheetVersion = sheetVersion;}
    public void setCellID(String cellID) {this.cellID = cellID; cellIDLabel.setText(cellID);}

    public void initialize() {
        wrapper.setId("root-container");
        List<Control> controlsEnabledWhenAnalyzeIsNotActive = new ArrayList<>();
        controlsEnabledWhenAnalyzeIsNotActive.add(minValTextField);
        controlsEnabledWhenAnalyzeIsNotActive.add(maxValTextField);
        controlsEnabledWhenAnalyzeIsNotActive.add(stepTextField);
        controlsEnabledWhenAnalyzeIsNotActive.forEach((control)->control.disableProperty().bind(isAnalyzeActive));
        resetButton.disableProperty().bind(isAnalyzeActive.not());
        slider.disableProperty().bind(isAnalyzeActive.not());
        setButton.disableProperty().bind(minValTextField.textProperty().isEmpty().
                or(maxValTextField.textProperty().isEmpty())
                .or(stepTextField.textProperty().isEmpty()));

        slider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double sliderValue = newValue.doubleValue();
            if (step!=null) {
                sliderValue = Math.round(newValue.doubleValue() / step) * step;
                slider.setValue(sliderValue);
            }
            handleSliderDrag(sliderValue);
        });
    }

    @FXML
    void setButtonClicked(ActionEvent event) {
        String minValAsText = minValTextField.getText();
        String maxValAsText = maxValTextField.getText();
        String stepAsText = stepTextField.getText();
        try {
            double minVal = Double.parseDouble(minValAsText);
            double maxVal = Double.parseDouble(maxValAsText);
            double step = Double.parseDouble(stepAsText);
            validateSliderValuesOrThrow(minVal,maxVal,step);
            startAnalyze(minVal,maxVal,step);
        }
        catch (NumberFormatException e) {
            statusLabel.setStyle(CSS_DEFINITION_FOR_RED_COLOR);
            statusLabel.setText("All values must be numbers");
        }
        catch  (Exception e) {
            statusLabel.setStyle(CSS_DEFINITION_FOR_RED_COLOR);
            statusLabel.setText(e.getMessage());
        }
    }

    private void validateSliderValuesOrThrow(double minVal, double maxVal, double step) {
        if (minVal >= maxVal) throw new InvalidArgumentException
                ("the minimum value must be greater than the maximum value");
        if (step<0) throw new IllegalArgumentException("step must be positive");
        if (step>maxVal-minVal) throw new IllegalArgumentException
                ("step must be bigger than the difference between the minimum and maximum values");
    }

    private void startAnalyze(double minVal, double maxVal, double step) {
        slider.setMin(minVal);
        slider.setMax(maxVal);
        slider.setBlockIncrement(step);
        this.step = step;
        isAnalyzeActive.set(true);
        slider.setValue(minVal);
    }

    @FXML
    void resetButtonClicked(ActionEvent event) {
        minValTextField.clear();
        maxValTextField.clear();
        stepTextField.clear();
        this.step = null;
        sheetViewScrollPane.setContent(null);
        isAnalyzeActive.set(false);
    }

    private void handleSliderDrag(Double originalExpression) {
        String finalUrl = HttpUrl
                .parse(Constants.CELL_UPDATE_PREVIEW)
                .newBuilder()
                .addQueryParameter("name",sheetName)
                .addQueryParameter("version",String.valueOf(sheetVersion-1))
                .addQueryParameter("originalExpression",String.valueOf(originalExpression))
                .addQueryParameter("cellID",cellID)
                .build()
                .toString();

        HttpClientMessenger.sendGetRequestWithoutBodyAsync(finalUrl,
                new MyCallBack(statusLabel, this::updateSheet));
    }

    private void updateSheet(String body) {
        Sheet sheet = GsonInstance.getGson().fromJson(body, DTOSheet.class);
        DynamicSheet dynamicSheet = DynamicSheetBuilder.buildDynamicSheet(sheet);
        dynamicSheet.populateSheetWithData(sheet);
        dynamicSheet.getCoordinates2CellController().get(new Coordinates(cellID)).setColorStyle("selected-cell");
        GridPane gridPane = dynamicSheet.getGridPane();
        sheetViewScrollPane.setContent(gridPane);
    }

}
