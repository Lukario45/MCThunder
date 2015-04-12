package net.mcthunder.events.interfaces;

import net.mcthunder.entity.Entity;

public interface MetadataChangeEventListenerInterface {
    public boolean removeDefaultListener();

    public void onMetadataChange(Entity entity);
}