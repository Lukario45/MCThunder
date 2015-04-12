package net.mcthunder.events.source;

import net.mcthunder.entity.Entity;
import net.mcthunder.entity.Player;
import net.mcthunder.events.PlayerAttackEntityEvent;
import net.mcthunder.events.interfaces.PlayerAttackEntityEventListenerInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 11/12/2014.
 */
public class PlayerAttackEntityEventSource {
    private List<PlayerAttackEntityEventListenerInterface> playerAttackEntityEventListeners = new ArrayList<>();

    public synchronized void addEventListener(PlayerAttackEntityEventListenerInterface listener) {
        this.playerAttackEntityEventListeners.add(listener);
    }

    public synchronized void removeEventListener(PlayerAttackEntityEventListenerInterface listener) {
        this.playerAttackEntityEventListeners.remove(listener);
    }

    public synchronized void fireEvent(Player player, Entity entity) throws ClassNotFoundException {
        PlayerAttackEntityEvent event = new  PlayerAttackEntityEvent(this);
        for (Object  playerAttackEntityEventListener : playerAttackEntityEventListeners)
            ((PlayerAttackEntityEventListenerInterface) playerAttackEntityEventListener).onAttackEntity(player, entity);
    }
}
