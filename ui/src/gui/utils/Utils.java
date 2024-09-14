package gui.utils;

import entities.coordinates.Coordinates;
import entities.types.undefined.Undefined;
import entities.types.undefined.UndefinedBoolean;
import entities.types.undefined.UndefinedNumber;
import entities.types.undefined.UndefinedString;
import exceptions.ServiceException;
import gui.exceptions.GUIException;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Utils {
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
        if (first.getRow() < second.getRow()) {
            return true;
        } else if (first.getRow() == second.getRow()) {
            return first.getCol() <= second.getCol();
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

}
