package net.mcthunder.auth.response;

import net.mcthunder.auth.properties.PropertyMap;

import java.util.UUID;

public class MinecraftProfilePropertiesResponse extends Response {

    private UUID id;
    private String name;
    private PropertyMap properties;

    public UUID getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public PropertyMap getProperties() {
        return this.properties;
    }

}
