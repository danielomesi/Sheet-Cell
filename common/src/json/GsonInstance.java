package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import entities.cell.DTOCell;
import entities.coordinates.Coordinates;
import json.adapters.CellsMapDeserializer;
import json.adapters.DTOCellAdapter;
import java.util.Map;

public class GsonInstance {
    private static Gson gson = null;

    static {
        try {
            System.out.println("Tyring to create a gson builder");
            gson = new GsonBuilder()
                    .registerTypeAdapter(DTOCell.class, new DTOCellAdapter())
                    .registerTypeAdapter(new TypeToken<Map<Coordinates, DTOCell>>(){}.getType(), new CellsMapDeserializer())
                    .create();
            System.out.println("Finished creating gson");
        }
        catch (Throwable e) {
            System.out.println("Error creating gson");
            e.printStackTrace();
        }
    }

    public static Gson getGson() {
        return gson;
    }
}
