package gui.builder;


import gui.scenes.workspace.analyze.AnalyzeController;
import gui.scenes.workspace.filter.FilterController;
import gui.scenes.workspace.main.MainController;
import gui.scenes.workspace.sort.SortController;
import javafx.fxml.FXMLLoader;
import static constants.Constants.*;

public class ControllersBuilder {
    public static SortController buildSortController(MainController mainController, DynamicSheet dynamicSheet,
                                                     String fromCellID, String toCellID) {
        SortController sortController = null;
        try {
            sortController = buildController(WORKSPACE_SORT_FXML);
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
            filterController = buildController(WORKSPACE_FILTER_FXML);
            filterController.setMainController(mainController);
            filterController.setFromCellID(fromCellID);
            filterController.setToCellID(toCellID);
            filterController.setDynamicSheetTable(dynamicSheet);
            filterController.setTable(dynamicSheet.getGridPane());
        }
        catch (Exception ignored) {}

        return filterController;
    }

    public static AnalyzeController buildAnalyzeController(MainController mainController, String cellID, String sheetName, int sheetVersion) {
        AnalyzeController analyzeController = null;
        try {
            analyzeController = buildController(WORKSPACE_ANALYZE_FXML);
            analyzeController.setMainController(mainController);
            analyzeController.setSheetName(sheetName);
            analyzeController.setSheetVersion(sheetVersion);
            analyzeController.setCellID(cellID);
        }
        catch (Exception ignored) {}

        return analyzeController;
    }

    private static <T> T buildController(String pathToXml) {
        T controller = null;
        try {
            FXMLLoader controllerLoader = new FXMLLoader(ControllersBuilder.class.getResource(pathToXml));
            controllerLoader.load();
            controller = controllerLoader.getController();
        }
        catch (Exception ignored) {}

        return controller;
    }

}
