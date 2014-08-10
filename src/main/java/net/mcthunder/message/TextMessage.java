package net.mcthunder.message;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class TextMessage extends Message {
    private String text;

    public TextMessage(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public TextMessage clone() {
        return (TextMessage) new TextMessage(getText()).setStyle(getStyle().clone()).setExtra(getExtra());
    }

    public JsonElement toJson() {
        if ((getStyle().isDefault()) && (getExtra().isEmpty())) {
            return new JsonPrimitive(this.text);
        }
        JsonElement e = super.toJson();
        if (e.isJsonObject()) {
            JsonObject json = e.getAsJsonObject();
            json.addProperty("text", this.text);
            return json;
        }
        return e;
    }
}