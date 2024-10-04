package gui.builder;


import gui.scenes.workspace.filter.FilterController;
import gui.scenes.workspace.main.MainController;
import gui.scenes.workspace.sort.SortController;
import javafx.fxml.FXMLLoader;

public class ControllersBuilder {
    public static SortController buildSortController(MainController mainController, DynamicSheet dynamicSheet,
                                                     String fromCellID, String toCellID) {
        SortController sortController = null;
        try {
            FXMLLoader sortLoader = new FXMLLoader(ControllersBuilder.class.getResource("/gui/scenes/workspace/sort/sort.fxml"));
            sortLoader.load();
            sortController = sortLoader.getController();
            sortController.setMainController(mainController);
            sortController.setFromCellID(fromCellID);
            sortController.setToCellID(toCellID);
            sortController.setDynamicSheetTable(dynamicSheet);
            sortController.setTable(dynamicSheet.getGridPane());
        }
        catch (Exception ignored) {}

        return sortController;
    }

    public static FilterController buildFilterController(MainController mainController, DynamicSheet dynamicSheet,
                                                         String fromCellID, String toCellID) {
        FilterController filterController = null;
        try {
            FXMLLoader filterLoader = new FXMLLoader(ControllersBuilder.class.getResource("/gui/scenes/workspace/filter/filter.fxml"));
            filterLoader.load();
            filterController = filterLoader.getController();
            filterController.setMainController(mainController);
            filterController.setFromCellID(fromCellID);
            filterController.setToCellID(toCellID);
            filterController.setDynamicSheetTable(dynamicSheet);
            filterController.setTable(dynamicSheet.getGridPane());
        }
        catch (Exception ignored) {}

        return filterController;
    }
}
