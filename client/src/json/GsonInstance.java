package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import entities.cell.DTOCell;
import entities.coordinates.Coordinates;

import java.util.Map;

public class GsonInstance {
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Object.class, new EffectiveValueDeserializer())
            .registerTypeAdapter(new TypeToken<Map<Coordinates, DTOCell>>(){}.getType(), new CellsMapDeserializer())
            .create();

    public static Gson getGson() {
        return gson;
    }
}
