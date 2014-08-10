package net.mcthunder.auth.serialize;

import com.google.gson.*;
import net.mcthunder.auth.properties.Property;
import net.mcthunder.auth.properties.PropertyMap;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;


public class PropertyMapSerializer
        implements JsonSerializer<PropertyMap>, JsonDeserializer<PropertyMap> {
    public PropertyMap deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        PropertyMap result = new PropertyMap();
        Iterator i$;
        Map.Entry entry;
        if ((json instanceof JsonObject)) {
            JsonObject object = (JsonObject) json;
            for (i$ = object.entrySet().iterator(); i$.hasNext(); ) {
                entry = (Map.Entry) i$.next();
                if ((entry.getValue() instanceof JsonArray)) {
                    for (JsonElement element : (JsonArray) entry.getValue())
                        result.put((String) entry.getKey(), new Property((String) entry.getKey(), element.getAsString()));
                }
            }
        } else if ((json instanceof JsonArray)) {
            for (JsonElement element : (JsonArray) json) {
                if ((element instanceof JsonObject)) {
                    JsonObject object = (JsonObject) element;
                    String name = object.getAsJsonPrimitive("name").getAsString();
                    String value = object.getAsJsonPrimitive("value").getAsString();
                    if (object.has("signature"))
                        result.put(name, new Property(name, value, object.getAsJsonPrimitive("signature").getAsString()));
                    else {
                        result.put(name, new Property(name, value));
                    }
                }
            }
        }

        return result;
    }

    public JsonElement serialize(PropertyMap src, Type typeOfSrc, JsonSerializationContext context) {
        JsonArray result = new JsonArray();
        for (Property property : src.values()) {
            JsonObject object = new JsonObject();
            object.addProperty("name", property.getName());
            object.addProperty("value", property.getValue());
            if (property.hasSignature()) {
                object.addProperty("signature", property.getSignature());
            }

            result.add(object);
        }

        return result;
    }
}