package gui.components.sort;

import gui.builder.DynamicSheetTable;
import gui.components.filter.FilterController;
import gui.components.main.MainController;
import javafx.fxml.FXMLLoader;

public class ControllersBuilder {
    public static SortController buildSortController(MainController mainController, DynamicSheetTable dynamicSheetTable, String fromCellID, String toCellID) {
        SortController sortController = null;
        try {
            FXMLLoader sortLoader = new FXMLLoader(ControllersBuilder.class.getResource("/gui/components/sort/sort.fxml"));
            sortLoader.load();
            sortController = sortLoader.getController();
            sortController.setMainController(mainController);
            sortController.setFromCellID(fromCellID);
            sortController.setToCellID(toCellID);
            sortController.setDynamicSheetTable(dynamicSheetTable);
        }
        catch (Exception ignored) {
        }

        return sortController;
    }

    public static FilterController buildFilterController(MainController mainController, DynamicSheetTable dynamicSheetTable, String fromCellID, String toCellID) {
        FilterController filterController = null;
        try {
            FXMLLoader filterLoader = new FXMLLoader(ControllersBuilder.class.getResource("/gui/components/filter/filter.fxml"));
            filterLoader.load();
            filterController = filterLoader.getController();
            filterController.setMainController(mainController);
            filterController.setFromCellID(fromCellID);
            filterController.setToCellID(toCellID);
            filterController.setDynamicSheetTable(dynamicSheetTable);
        }
        catch (Exception e) {
            e.printStackTrace();

        }

        return filterController;
    }
}
