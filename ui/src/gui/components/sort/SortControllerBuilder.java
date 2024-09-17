package gui.components.sort;

import gui.components.main.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

public class SortControllerBuilder {
    public static SortController buildSortController(MainController mainController) {
        SortController sortController = null;
        try {
            FXMLLoader sortLoader = new FXMLLoader(SortControllerBuilder.class.getResource("/gui/components/sort/sort.fxml"));
            sortLoader.load();
            sortController = sortLoader.getController();
            sortController.setMainController(mainController);
        }
        catch (Exception ignored) {
        }

        return sortController;
    }
}
