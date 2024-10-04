package gui.components.main;

import gui.components.dashboard.header.HeaderController;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class DashboardController {

    private HeaderController headerController;

    @FXML
    private ScrollPane mainScrollPane;

    @FXML
    private VBox mainVBox;

    public void setHeaderController(HeaderController headerController) {this.headerController = headerController;}
    public VBox getMainVBox() {return mainVBox;}

}