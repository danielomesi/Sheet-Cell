package gui.components.sort;

import gui.builder.DynamicSheetTable;
import gui.components.main.MainController;
import javafx.fxml.FXMLLoader;

public class SortControllerBuilder {
    public static SortController buildSortController(MainController mainController, DynamicSheetTable dynamicSheetTable, String fromCellID, String toCellID) {
        SortController sortController = null;
        try {
            FXMLLoader sortLoader = new FXMLLoader(SortControllerBuilder.class.getResource("/gui/components/sort/sort.fxml"));
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
}
