package adventofcode.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 * Author: kachte4
 * Date: 14-11-2020.
 */
public class JsonTools {
    public static String getValueFromJsonElement(final JsonElement element,
                                                 final String name) {
        final JsonObject object = element.getAsJsonObject();
        final JsonElement valueElement = object.get(name);

        return valueElement.isJsonNull() ? null : valueElement.getAsString();
    }

    public static String toJSON(final Map<String, String> m) {
        try {
            return new ObjectMapper().writeValueAsString(m);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error while serializing", e);
        }
    }
}
