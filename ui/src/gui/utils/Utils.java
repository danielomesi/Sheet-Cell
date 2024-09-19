package gui.utils;

import entities.coordinates.CoordinateFactory;
import entities.coordinates.Coordinates;
import entities.sheet.Sheet;
import entities.types.undefined.Undefined;
import entities.types.undefined.UndefinedBoolean;
import entities.types.undefined.UndefinedNumber;
import entities.types.undefined.UndefinedString;
import exceptions.ServiceException;
import gui.exceptions.GUIException;
import gui.exceptions.RowOutOfBoundsException;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Utils {
    public static final String NON_EXISTING_CELL_NAME = "[EMPTY-CELL]";

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

    public static Task<Void> getTaskFromRunnable(Runnable runnable, Label labelToBindMessage, ProgressBar progressBarToBind, boolean isDelayed) {
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
                    updateMessage("The task finished successfully!");
                } catch (Exception e) {
                    updateMessage(Utils.generateErrorMessageFromException(e));
                    throw e;
                }
                return null;
            }
        };

        labelToBindMessage.textProperty().bind(task.messageProperty());
        if (isDelayed) {
            progressBarToBind.progressProperty().bind(task.progressProperty());
        }

        task.stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                labelToBindMessage.setStyle("-fx-text-fill: green;");
            } else if (newValue == Worker.State.FAILED) {
                labelToBindMessage.setStyle("-fx-text-fill: red;");
            }
        });

        return task;
    }

    public static void openWindow(Parent root, String title) {
        Scene sortScene = new Scene(root);
        Stage sortWindow = new Stage();
        sortWindow.setTitle(title);
        sortWindow.setScene(sortScene);
        sortWindow.show();
    }




}
