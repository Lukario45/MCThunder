package net.mcthunder.events.listeners;

import net.mcthunder.MCThunder;
import net.mcthunder.entity.Entity;
import net.mcthunder.entity.EntityType;
import net.mcthunder.entity.LivingEntity;
import net.mcthunder.entity.Player;
import net.mcthunder.events.interfaces.PlayerAttackEntityEventListenerInterface;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerDestroyEntitiesPacket;

public class PlayerAttackEntityEventListener implements PlayerAttackEntityEventListenerInterface {
    @Override
    public boolean removeDefaultListener() {
        return false;
    }

    @Override
    public void onAttackEntity(Player player, Entity entity) {
        if (entity instanceof LivingEntity){
            LivingEntity e = (LivingEntity) player.getWorld().getEntityFromID(entity.getEntityID());
            float health = e.getHealth() - 1 - player.getHeldItem().getBonusDamnage();
            player.sendMessage("Health " + health);
            e.setHealth(health);
            if (e.getHealth() <= 0f)
                player.getWorld().removeEntity(entity.getEntityID());
        } else
            player.sendMessage("Why attack a non living entity?");
    }
}