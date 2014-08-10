package net.mcthunder.auth.response;

import net.mcthunder.auth.properties.PropertyMap;

import java.util.UUID;

public class HasJoinedResponse extends Response {

    private UUID id;
    private PropertyMap properties;

    public UUID getId() {
        return this.id;
    }

    public PropertyMap getProperties() {
        return this.properties;
    }

}
