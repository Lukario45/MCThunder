package net.mcthunder.auth.response;

import net.mcthunder.auth.properties.PropertyMap;

public class User {
    private String id;
    private PropertyMap properties;

    public String getId() {
        return this.id;
    }

    public PropertyMap getProperties() {
        return this.properties;
    }
}