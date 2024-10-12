package json;

import com.google.gson.*;
import entities.cell.DTOCell;
import entities.coordinates.Coordinates;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class CellsMapDeserializer implements JsonDeserializer<Map<Coordinates, DTOCell>> {
    @Override
    public Map<Coordinates, DTOCell> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Map<Coordinates, DTOCell> cellsMap = new HashMap<>();
        JsonObject jsonObject = json.getAsJsonObject();

        for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
            String key = entry.getKey(); // e.g., "B2"
            JsonElement value = entry.getValue();

            // Convert the string key to Coordinates
            Coordinates coordinates = stringToCoordinates(key);

            // Deserialize the DTOCell from the JSON value
            DTOCell dtoCell = context.deserialize(value, DTOCell.class);

            // Add the coordinates and the corresponding DTOCell to the map
            cellsMap.put(coordinates, dtoCell);
        }

        return cellsMap;
    }

    private Coordinates stringToCoordinates(String cellIdentifier) {
        char columnChar = cellIdentifier.charAt(0); // e.g., 'B'
        int rowNumber = Integer.parseInt(cellIdentifier.substring(1)); // e.g., 2

        // Convert column letter to column index (0-based)
        int columnIndex = columnChar - 'A'; // assuming 'A' = 0, 'B' = 1, ...

        return new Coordinates(rowNumber - 1, columnIndex); // Adjust for 0-based index
    }
}
