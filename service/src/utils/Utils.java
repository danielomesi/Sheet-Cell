package utils;

import service_exceptions.CellOutOfBoundsException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static String trimQuotes(String input) {
        if (input != null && !input.isEmpty()) {
            if (input.startsWith("\"") || input.startsWith("'") || input.startsWith("`")) {
                input = input.substring(1);
            }
            if (input.endsWith("\"") || input.endsWith("'") || input.endsWith("`")) {
                input = input.substring(0, input.length() - 1);
            }
        }
        return input;
    }

    public static void validateInRange(int toCheck, int start, int end) {
        if (!(start <= toCheck && toCheck <= end)) {
            throw new CellOutOfBoundsException("Expected a number between "
                    + String.valueOf(start+1) + " and " + String.valueOf(end), String.valueOf(toCheck + 1));
        }
    }

    public static String generateNameWithDateTime(String input) {
        // Create a SimpleDateFormat to format the current date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyyHHmm");

        // Get the current date and time
        String dateTime = dateFormat.format(new Date());

        // Concatenate the input string with the formatted date-time string
        return input + "_" + dateTime;
    }
}
