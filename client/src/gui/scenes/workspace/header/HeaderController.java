package gui.scenes.workspace.header;

import entities.permission.PermissionType;
import gui.scenes.workspace.main.MainController;
import gui.utils.Utils;
import javafx.beans.property.BooleanProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import static constants.Constants.*;

public class HeaderController {

    private MainController mainController;

    @FXML
    private Label mainLabel;
    @FXML
    private Label connectedUserLabel;

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
}




