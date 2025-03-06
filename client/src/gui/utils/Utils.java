package gui.utils;

import entities.coordinates.Coordinates;
import entities.types.undefined.Undefined;
import entities.types.undefined.UndefinedBoolean;
import entities.types.undefined.UndefinedNumber;
import entities.types.undefined.UndefinedString;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import service_exceptions.ServiceException;
import gui.exceptions.GUIException;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static constants.Constants.*;

public class Utils {
    private static final Map<String,String> cssStyleName2cssStylePath = new HashMap<>();

    static {
        cssStyleName2cssStylePath.put(DEFAULT_STYLE, DEFAULT_STYLE_CSS_PATH);
        cssStyleName2cssStylePath.put(DARK_STYLE, DARK_STYLE_CSS_PATH);
        cssStyleName2cssStylePath.put(MACCABI_STYLE, MACCABI_STYLE_CSS_PATH);
    }

    public static String objectToString(Object obj) {
        String result = "";

        if (obj instanceof Number number) {
            if (number.doubleValue() % 1 == 0) {
                result = String.format("%,d", number.longValue());
            } else {
                result = String.format("%,.2f", number.doubleValue());
            }
        }
        else if (obj instanceof Boolean bool) {
            result = bool.toString().toUpperCase();
        }
        else if (obj instanceof Undefined) {
            if (obj instanceof UndefinedNumber){
                result = "NaN";
            }
            else if (obj instanceof UndefinedString){
                result = "!UNDEFINED!";
            }
            else if (obj instanceof UndefinedBoolean) {
                result = "UNKNOWN";
            }
            else {
                result = "Unknown Type of " + obj.getClass().getSimpleName();
            }
        }
        else if (obj!=null) {
            result = obj.toString();
        }
        return result;
    }

    public static String generateErrorMessageFromException(Exception e) {
        StringBuilder sb = new StringBuilder();
        if (e instanceof ServiceException || e instanceof GUIException) {
            sb.append(e.toString());
        } else {
            sb.append("Error: ");
            sb.append(e.getMessage());
        }
        return sb.toString();
    }

    public static boolean compareCoordinates(Coordinates first, Coordinates second) {
        if (first.getRow() <= second.getRow() && first.getCol() <= second.getCol()) {
            return true;
        }

        return false;
    }

    public static List<String> getLettersFromAToTheNLetter(int n) {
        List<String> letters = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            letters.add(Character.toString((char) ('A' + i)));
        }
        return letters;
    }

    public static List<Integer> getNumbersFrom1ToNNumber(int n) {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            numbers.add(i);
        }
        return numbers;
    }

    public static int convertColumnCharToIndex(Character ch) {
        ch = Character.toUpperCase(ch);
        return ch - 'A';
    }

    public static void runTaskInADaemonThread(Task task) {
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public static Task<Void> getTaskFromRunnable(Runnable runnable, ProgressIndicator progressIndicatorToBind, boolean isDelayed) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                updateMessage("Executing...");
                if (isDelayed) {
                    for (int i = 1; i <= 100; i++) {
                        Thread.sleep(10);
                        updateProgress(i, 100);
                    }
                }
                try {
                    runnable.run();
                    updateMessage(GENERAL_TASK_SUCCESS_MESSAGE);
                } catch (Exception e) {
                    updateMessage(Utils.generateErrorMessageFromException(e));
                    throw e;
                }
                return null;
            }
        };

        if (isDelayed) {
            progressIndicatorToBind.progressProperty().bind(task.progressProperty());
        }

        return task;
    }

    public static void openWindow(Parent root, String title) {
        Scene sortScene = new Scene(root);
        Stage sortWindow = new Stage();
        sortWindow.setTitle(title);
        sortWindow.setScene(sortScene);
        sortWindow.show();
    }

    public static void setStyle(Parent parent,String styleSheetName) {
        URL firstResource =Utils.class.getResource(GENERIC_STYLE_CSS_PATH);
        URL secondResource = Utils.class.getResource(cssStyleName2cssStylePath.get(styleSheetName));
        if (firstResource != null && secondResource != null) {
            parent.getStylesheets().clear();
            parent.getStylesheets().add(firstResource.toExternalForm());
            parent.getStylesheets().add(secondResource.toExternalForm());
        }
    }

    public static String removeExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');

        // If there is a dot in the file name, remove the extension
        if (lastDotIndex > 0) {
            return fileName.substring(0, lastDotIndex);
        } else {
            // If there's no extension, return the original file name
            return fileName;
        }
    }
}


