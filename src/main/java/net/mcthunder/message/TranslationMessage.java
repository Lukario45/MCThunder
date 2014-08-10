package net.mcthunder.message;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Arrays;

public class TranslationMessage extends Message {
    private String translationKey;
    private Message[] translationParams;

    public TranslationMessage(String translationKey, Message[] translationParams) {
        this.translationKey = translationKey;
        this.translationParams = translationParams;
        this.translationParams = getTranslationParams();
        for (Message param : this.translationParams)
            param.getStyle().setParent(getStyle());
    }

    public String getTranslationKey() {
        return this.translationKey;
    }

    public Message[] getTranslationParams() {
        Message[] copy = (Message[]) Arrays.copyOf(this.translationParams, this.translationParams.length);
        for (int index = 0; index < copy.length; index++) {
            copy[index] = copy[index].clone();
        }

        return copy;
    }

    public Message setStyle(MessageStyle style) {
        super.setStyle(style);
        for (Message param : this.translationParams) {
            param.getStyle().setParent(getStyle());
        }

        return this;
    }

    public String getText() {
        return this.translationKey;
    }

    public TranslationMessage clone() {
        return (TranslationMessage) new TranslationMessage(getTranslationKey(), getTranslationParams()).setStyle(getStyle().clone()).setExtra(getExtra());
    }

    public JsonElement toJson() {
        JsonElement e = super.toJson();
        if (e.isJsonObject()) {
            JsonObject json = e.getAsJsonObject();
            json.addProperty("translate", this.translationKey);
            JsonArray params = new JsonArray();
            for (Message param : this.translationParams) {
                params.add(param.toJson());
            }

            json.add("with", params);
            return json;
        }
        return e;
    }
}