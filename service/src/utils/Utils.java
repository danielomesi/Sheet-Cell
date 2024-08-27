package utils;

import exceptions.CellOutOfBoundsException;

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
                    + String.valueOf(start+1) + " and " + String.valueOf(end + 1), String.valueOf(toCheck + 1));
        }
    }
}
