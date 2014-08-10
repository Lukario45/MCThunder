package net.mcthunder.auth.serialize;

import com.google.gson.*;
import net.mcthunder.auth.GameProfile;
import net.mcthunder.auth.response.ProfileSearchResultsResponse;

import java.lang.reflect.Type;

public class ProfileSearchResultsSerializer implements JsonDeserializer<ProfileSearchResultsResponse> {
    public ProfileSearchResultsResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ProfileSearchResultsResponse result = new ProfileSearchResultsResponse();
        if (json instanceof JsonObject) {
            JsonObject object = (JsonObject) json;
            if (object.has("error")) {
                result.setError(object.getAsJsonPrimitive("error").getAsString());
            }

            if (object.has("errorMessage")) {
                result.setError(object.getAsJsonPrimitive("errorMessage").getAsString());
            }

            if (object.has("cause")) {
                result.setError(object.getAsJsonPrimitive("cause").getAsString());
            }
        } else {
            result.setProfiles((GameProfile[]) context.deserialize(json, GameProfile[].class));
        }

        return result;
    }
}
