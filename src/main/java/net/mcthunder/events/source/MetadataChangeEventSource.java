package net.mcthunder.events.source;

import net.mcthunder.entity.Entity;
import net.mcthunder.events.MetadataChangeEvent;
import net.mcthunder.events.listeners.MetadataChangeEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MetadataChangeEventSource {
    private static boolean removeDefault = false;
    private List metadataChangeEventListeners = new ArrayList();

    public synchronized void addEventListener(MetadataChangeEventListener listener) {
        if (listener.removeDefaultListener() && !metadataChangeEventListeners.isEmpty() && !removeDefault) {
            metadataChangeEventListeners.remove(0);
            removeDefault = true;
        }
        metadataChangeEventListeners.add(listener);
    }

    public synchronized void removeEventListener(MetadataChangeEventListener listener) {
        metadataChangeEventListeners.remove(listener);
    }

    public synchronized void fireEvent(Entity entity) {
        MetadataChangeEvent event = new MetadataChangeEvent(this);
        Iterator iterator = metadataChangeEventListeners.iterator();
        while (iterator.hasNext())
            ((MetadataChangeEventListener) iterator.next()).onMetadataChange(entity);
    }
}