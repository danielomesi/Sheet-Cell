package gui.utils;

import entities.types.undefined.Undefined;
import entities.types.undefined.UndefinedBoolean;
import entities.types.undefined.UndefinedNumber;
import entities.types.undefined.UndefinedString;
import exceptions.ServiceException;
import gui.exceptions.GUIException;

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
}
