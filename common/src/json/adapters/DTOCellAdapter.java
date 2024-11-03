package json.adapters;

import com.google.gson.*;
import entities.cell.DTOCell;
import entities.coordinates.Coordinates;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class DTOCellAdapter implements JsonSerializer<DTOCell>, JsonDeserializer<DTOCell> {

    @Override
    public JsonElement serialize(DTOCell dtoCell, Type typeOfSrc, JsonSerializationContext context) {
        if (dtoCell == null) return null;
        JsonObject jsonObject = new JsonObject();

        // Serialize each field
        jsonObject.add("coordinates", context.serialize(dtoCell.getCoordinates()));
        jsonObject.addProperty("version", dtoCell.getVersion());
        jsonObject.addProperty("lastEditor", dtoCell.getLastEditor());
        jsonObject.add("effectiveValue", new EffectiveValueAdapter().serialize(dtoCell.getEffectiveValue(), Object.class, context));
        jsonObject.addProperty("originalExpression", dtoCell.getOriginalExpression());
        jsonObject.add("cellsAffectedByMe", serializeCoordinatesSet(dtoCell.getCellsAffectedByMe(), context));
        jsonObject.add("cellsAffectingMe", serializeCoordinatesSet(dtoCell.getCellsAffectingMe(), context));

        return jsonObject;
    }



    @Override
    public DTOCell deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json == null | json == JsonNull.INSTANCE) return null;
        JsonObject jsonObject = json.getAsJsonObject();

        Coordinates coordinates = context.deserialize(jsonObject.get("coordinates"), Coordinates.class);
        int version = jsonObject.get("version").getAsInt();
        String lastEditor = jsonObject.get("lastEditor").getAsString();
        Object effectiveValue = new EffectiveValueAdapter().deserialize(jsonObject.get("effectiveValue"), Object.class,context);
        JsonElement jsonElementOfOriginalExpression = jsonObject.get("originalExpression");
        String originalExpression = jsonElementOfOriginalExpression != null?  jsonElementOfOriginalExpression.getAsString() : null;
        Set<Coordinates> cellsAffectedByMe = deserializeCoordinatesSet(jsonObject.get("cellsAffectedByMe"), context);
        Set<Coordinates> cellsAffectingMe = deserializeCoordinatesSet(jsonObject.get("cellsAffectingMe"), context);

        return new DTOCell(coordinates,version,lastEditor,effectiveValue,originalExpression,cellsAffectedByMe,cellsAffectingMe);
    }

    private JsonArray serializeCoordinatesSet(Set<Coordinates> coordinatesSet, JsonSerializationContext context) {
        JsonArray jsonArray = new JsonArray();
        if (coordinatesSet != null) {
            for (Coordinates coordinates : coordinatesSet) {
                jsonArray.add(context.serialize(coordinates));
            }
        }
        return jsonArray;
    }

    private Set<Coordinates> deserializeCoordinatesSet(JsonElement element, JsonDeserializationContext context) {
        Set<Coordinates> coordinatesSet = new HashSet<>();
        if (element.isJsonArray()) {
            for (JsonElement e : element.getAsJsonArray()) {
                Coordinates coordinates = context.deserialize(e, Coordinates.class);
                coordinatesSet.add(coordinates);
            }
        }
        return coordinatesSet;
    }
}
