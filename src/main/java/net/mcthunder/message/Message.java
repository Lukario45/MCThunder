package net.mcthunder.message;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public abstract class Message
        implements Cloneable {
    private MessageStyle style = new MessageStyle();
    private List<Message> extra = new ArrayList();

    public static Message fromString(String str) {
        try {
            return fromJson((JsonElement) new Gson().fromJson(str, JsonObject.class));
        } catch (Exception e) {
        }
        return new TextMessage(str);
    }

    public static Message fromJson(JsonElement e) {
        if (e.isJsonPrimitive())
            return new TextMessage(e.getAsString());
        if (e.isJsonObject()) {
            JsonObject json = e.getAsJsonObject();
            Message msg = null;
            if (json.has("text")) {
                msg = new TextMessage(json.get("text").getAsString());
            } else if (json.has("translate")) {
                Message[] with = new Message[0];
                if (json.has("with")) {
                    JsonArray withJson = json.get("with").getAsJsonArray();
                    with = new Message[withJson.size()];
                    for (int index = 0; index < withJson.size(); index++) {
                        JsonElement el = withJson.get(index);
                        if (el.isJsonPrimitive())
                            with[index] = new TextMessage(el.getAsString());
                        else {
                            with[index] = fromJson(el.getAsJsonObject());
                        }
                    }
                }

                msg = new TranslationMessage(json.get("translate").getAsString(), with);
            } else {
                throw new IllegalArgumentException("Unknown message type in json: " + json.toString());
            }

            MessageStyle style = new MessageStyle();
            if (json.has("color")) {
                style.setColor(ChatColor.byName(json.get("color").getAsString()));
            }

            for (ChatFormat format : ChatFormat.values()) {
                if ((json.has(format.toString())) && (json.get(format.toString()).getAsBoolean())) {
                    style.addFormat(format);
                }
            }

            if (json.has("clickEvent")) {
                JsonObject click = json.get("clickEvent").getAsJsonObject();
                style.setClickEvent(new ClickEvent(ClickAction.byName(click.get("action").getAsString()), click.get("value").getAsString()));
            }

            if (json.has("hoverEvent")) {
                JsonObject hover = json.get("hoverEvent").getAsJsonObject();
                style.setHoverEvent(new HoverEvent(HoverAction.byName(hover.get("action").getAsString()), fromJson(hover.get("value"))));
            }

            if (json.has("insertion")) {
                style.setInsertion(json.get("insertion").getAsString());
            }

            msg.setStyle(style);
            if (json.has("extra")) {
                JsonArray extraJson = json.get("extra").getAsJsonArray();
                List extra = new ArrayList();
                for (int index = 0; index < extraJson.size(); index++) {
                    JsonElement el = extraJson.get(index);
                    if (el.isJsonPrimitive())
                        extra.add(new TextMessage(el.getAsString()));
                    else {
                        extra.add(fromJson(el.getAsJsonObject()));
                    }
                }

                msg.setExtra(extra);
            }

            return msg;
        }
        throw new IllegalArgumentException("Cannot convert " + e.getClass().getSimpleName() + " to a message.");
    }

    public abstract String getText();

    public String getFullText() {
        StringBuilder build = new StringBuilder(getText());
        for (Message msg : this.extra) {
            build.append(msg.getFullText());
        }

        return build.toString();
    }

    public MessageStyle getStyle() {
        return this.style;
    }

    public Message setStyle(MessageStyle style) {
        this.style = style;
        return this;
    }

    public List<Message> getExtra() {
        return new ArrayList(this.extra);
    }

    public Message setExtra(List<Message> extra) {
        this.extra = new ArrayList(extra);
        for (Message msg : this.extra) {
            msg.getStyle().setParent(this.style);
        }

        return this;
    }

    public Message addExtra(Message message) {
        this.extra.add(message);
        message.getStyle().setParent(this.style);
        return this;
    }

    public Message removeExtra(Message message) {
        this.extra.remove(message);
        message.getStyle().setParent(new MessageStyle());
        return this;
    }

    public Message clearExtra() {
        for (Message msg : this.extra) {
            msg.getStyle().setParent(new MessageStyle());
        }

        this.extra.clear();
        return this;
    }

    public String toString() {
        return getFullText();
    }

    public abstract Message clone();

    public String toJsonString() {
        return toJson().toString();
    }

    public JsonElement toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("color", this.style.getColor().toString());
        for (ChatFormat format : this.style.getFormats()) {
            json.addProperty(format.toString(), Boolean.valueOf(true));
        }

        if (this.style.getClickEvent() != null) {
            JsonObject click = new JsonObject();
            click.addProperty("action", this.style.getClickEvent().getAction().toString());
            click.addProperty("value", this.style.getClickEvent().getValue());
            json.add("clickEvent", click);
        }

        if (this.style.getHoverEvent() != null) {
            JsonObject hover = new JsonObject();
            hover.addProperty("action", this.style.getHoverEvent().getAction().toString());
            hover.add("value", this.style.getHoverEvent().getValue().toJson());
            json.add("hoverEvent", hover);
        }

        if (this.style.getInsertion() != null) {
            json.addProperty("insertion", this.style.getInsertion());
        }

        if (this.extra.size() > 0) {
            JsonArray extra = new JsonArray();
            for (Message msg : this.extra) {
                extra.add(msg.toJson());
            }

            json.add("extra", extra);
        }

        return json;
    }
}