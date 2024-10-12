package json;

import com.google.gson.*;

import java.lang.reflect.Type;

public class EffectiveValueDeserializer implements JsonDeserializer<Object> {
    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonPrimitive()) {
            JsonPrimitive prim = json.getAsJsonPrimitive();

            // Check if it's a number
            if (prim.isNumber()) {
                return prim.getAsDouble();  // or getAsInt(), getAsLong(), etc., based on your needs
            }
            // Check if it's a boolean
            else if (prim.isBoolean()) {
                return prim.getAsBoolean();
            }
            // Otherwise, treat it as a string
            else {
                return prim.getAsString();
            }
        } else {
            return null;  // or handle other cases if needed (like objects, arrays, etc.)
        }
    }
}

