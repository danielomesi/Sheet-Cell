package gui.utils;

import entities.cell.Cell;
import entities.coordinates.Coordinates;
import entities.sheet.Sheet;
import entities.types.undefined.Undefined;
import entities.types.undefined.UndefinedBoolean;
import entities.types.undefined.UndefinedNumber;
import entities.types.undefined.UndefinedString;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static String objectToString(Object obj) {
        String result;

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
        else {
            result = obj.toString();
        }

        return result;
    }
}
