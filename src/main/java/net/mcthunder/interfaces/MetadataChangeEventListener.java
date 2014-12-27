package net.mcthunder.interfaces;

import net.mcthunder.entity.Entity;

public interface MetadataChangeEventListener {
    public boolean removeDefaultListener();

    public void onMetadataChange(Entity entity);
}