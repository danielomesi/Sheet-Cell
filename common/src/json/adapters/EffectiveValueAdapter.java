package json.adapters;

import com.google.gson.*;
import entities.types.undefined.UndefinedBoolean;
import entities.types.undefined.UndefinedNumber;
import entities.types.undefined.UndefinedString;

import java.lang.reflect.Type;

public class EffectiveValueAdapter implements JsonSerializer<Object>, JsonDeserializer<Object> {

    @Override
    public JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();

        if (src instanceof UndefinedString) {
            jsonObject.addProperty("type", "undefined_string");
        } else if (src instanceof UndefinedBoolean) {
            jsonObject.addProperty("type", "undefined_boolean");
        } else if (src instanceof UndefinedNumber) {
            jsonObject.addProperty("type", "undefined_number");
        } else if (src instanceof Number) {
            jsonObject.add("value", new JsonPrimitive((Number) src));
        } else if (src instanceof Boolean) {
            jsonObject.add("value", new JsonPrimitive((Boolean) src));
        } else if (src instanceof String) {
            jsonObject.add("value", new JsonPrimitive((String) src));
        } else {
            jsonObject = null;
        }

        return jsonObject;
    }

    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json == null || json == JsonNull.INSTANCE) return null;
        if (json.isJsonObject()) {
            JsonObject jsonObject = json.getAsJsonObject();

            if (jsonObject.has("type")) {
                String type = jsonObject.get("type").getAsString();
                switch (type) {
                    case "undefined_string":
                        return new UndefinedString();
                    case "undefined_boolean":
                        return new UndefinedBoolean();
                    case "undefined_number":
                        return new UndefinedNumber();
                }
            } else if (jsonObject.has("value")) {
                JsonElement value = jsonObject.get("value");
                if (value.isJsonPrimitive()) {
                    JsonPrimitive prim = value.getAsJsonPrimitive();
                    if (prim.isNumber()) {
                        return prim.getAsDouble();
                    } else if (prim.isBoolean()) {
                        return prim.getAsBoolean();
                    } else if (prim.isString()) {
                        return prim.getAsString();
                    }
                }
            }
        }

        return null;
    }
}
