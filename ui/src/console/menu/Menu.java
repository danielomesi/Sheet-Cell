package console.menu;

import console.ConsolePrintHelper;
import console.exceptions.NoExistenceException;
import console.exceptions.OutOfRangeException;
import engine.Engine;
import engine.EngineImpl;
import entities.cell.Cell;

import java.util.*;

public class Menu {
    private static final Engine engine;
    private static final Map<MENU_OPTION, Runnable> option2Runnable;
    private static final Scanner scanner;

    public enum MENU_OPTION {
        LOAD_SHEET_FROM_XML,
        PRINT_SHEET,
        SHOW_INFO_OF_SPECIFIC_CELL,
        UPDATE_VALUE_OF_SPECIFIC_CELL,
        SHOW_VERSIONS,
        SAVE_STATE_TO_FILE,
        LOAD_STATE_FROM_FILE,
        EXIT
    }

    static {
        scanner = new Scanner(System.in);
        engine = new EngineImpl();
        option2Runnable = new HashMap<>();
        option2Runnable.put(MENU_OPTION.LOAD_SHEET_FROM_XML, Menu::loadSheetFromXMLFile);
        option2Runnable.put(MENU_OPTION.PRINT_SHEET, Menu::printSheet);
        option2Runnable.put(MENU_OPTION.SHOW_INFO_OF_SPECIFIC_CELL, Menu::showInfoOfSpecificCell);
        option2Runnable.put(MENU_OPTION.UPDATE_VALUE_OF_SPECIFIC_CELL, Menu::updateValueOfSpecificCell);
        option2Runnable.put(MENU_OPTION.SHOW_VERSIONS, Menu::showVersions);
        option2Runnable.put(MENU_OPTION.SAVE_STATE_TO_FILE, Menu::saveStateToFile);
        option2Runnable.put(MENU_OPTION.LOAD_STATE_FROM_FILE, Menu::loadStateFromFile);
        option2Runnable.put(MENU_OPTION.EXIT, Menu::exit);
    }

    public static Map<MENU_OPTION, Runnable> getOption2Runnable() {return option2Runnable;}

    public static void start() {
        MENU_OPTION menuOption = null;
        do {
            try {
                printMenu();
                String input = scanner.nextLine();
                int choice = Integer.parseInt(input);
                menuOption = validateAndCastToEnum(choice);
                System.out.println("---Option chosen. executing...---");
                validateSheetExistenceIfNeeded(menuOption);
                option2Runnable.get(menuOption).run();
                System.out.println("---Option execution terminated successfully---");
                System.out.println();
            } catch (Exception e) {
                ConsolePrintHelper.printExceptionInfo(e);
            }
        }
        while (menuOption != MENU_OPTION.EXIT);
    }

    private static void printMenu() {
        System.out.println("Please choose an option by typing its number:");
        System.out.println("1 - Load Sheet Data from XML File");
        System.out.println("2 - Display Sheet");
        System.out.println("3 - Show Info of Specific Cell");
        System.out.println("4 - Update Value of Specific Cell");
        System.out.println("5 - Show All Versions");
        System.out.println("6 - Save State to File");
        System.out.println("7 - Load State from File");
        System.out.println("8 - Exit System");
    }

    private static MENU_OPTION validateAndCastToEnum(int choice) {
        if (choice <= 0 || choice > option2Runnable.size()) {
            throw new OutOfRangeException("Choice must be between 1 and " + option2Runnable.size());
        }

        //since choice originally came from user side which is 1-based, Converting to 0-based is necessary
        return MENU_OPTION.values()[choice-1];
    }

    private static void validateSheetExistenceIfNeeded(MENU_OPTION menuOption) {
        if (menuOption != MENU_OPTION.LOAD_SHEET_FROM_XML && menuOption != MENU_OPTION.LOAD_STATE_FROM_FILE && menuOption != MENU_OPTION.EXIT) {
            if (engine.getSheet() == null) {
                throw new NoExistenceException("No sheet found. This option requires a sheet to be loaded first");
            }
        }
    }

    private static void loadSheetFromXMLFile() {
        System.out.println("Please enter the path to the XML file, including the file name (no .xml extension included):");
        String fullFilePath = scanner.nextLine();
        engine.loadSheetFromXMLFile(fullFilePath);
    }

    private static void printSheet() {
        ConsolePrintHelper.printSheet(engine.getSheet());
    }



    private static void showInfoOfSpecificCell() {
        System.out.println("Please enter the Cell ID (ex. E7):");
        String cellID = scanner.nextLine();
        Cell cell = engine.getSpecificCell(cellID);
        ConsolePrintHelper.printCellInfo(cell);
    }

    private static void updateValueOfSpecificCell() {
        System.out.println("Please enter the Cell ID (ex. E7):");
        String cellID = scanner.nextLine();
        Cell cell = engine.getSpecificCell(cellID);
        ConsolePrintHelper.printBasicCellInfo(cell);
        System.out.println("Please enter the expression value that you want to insert into the cell:");
        String expression = scanner.nextLine();
        engine.updateSpecificCell(cellID,expression);
    }

    private static void showVersions() {
        ConsolePrintHelper.printSheetVersionsInfo(engine.getSheetList());
        System.out.println("Please choose a sheet to show by typing its number:");
        String input = scanner.nextLine();
        int choice = Integer.parseInt(input) - 1; //decrementing 1 to make it 0-based
        ConsolePrintHelper.printSheet(engine.getSheet(choice));
    }

    private static void saveStateToFile() {
        String fullFileName = getFullFilePathFromuUser();
        engine.saveStateToFile(fullFileName);
    }

    private static void loadStateFromFile() {
        String fullFileName = getFullFilePathFromuUser();
        engine.loadStateFromFile(fullFileName);
    }

    private static String getFullFilePathFromuUser() {
        System.out.println("Please enter the path to the file, including the file name (no extension included):");
        return scanner.nextLine();
    }

    private static void exit() {}
}
