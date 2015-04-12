package net.mcthunder.events.interfaces;

import net.mcthunder.entity.Entity;
import net.mcthunder.entity.Player;

public interface PlayerAttackEntityEventListenerInterface {
    public boolean removeDefaultListener();

    public void onAttackEntity(Player player, Entity entity);
}