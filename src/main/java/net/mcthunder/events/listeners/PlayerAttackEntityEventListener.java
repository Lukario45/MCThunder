package net.mcthunder.events.listeners;

import net.mcthunder.MCThunder;
import net.mcthunder.entity.Bot;
import net.mcthunder.entity.Entity;
import net.mcthunder.entity.LivingEntity;
import net.mcthunder.entity.Player;
import net.mcthunder.events.interfaces.PlayerAttackEntityEventListenerInterface;
import org.spacehq.mc.protocol.data.game.values.entity.EntityStatus;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityStatusPacket;

public class PlayerAttackEntityEventListener implements PlayerAttackEntityEventListenerInterface {
    @Override
    public boolean removeDefaultListener() {
        return false;
    }

    @Override
    public void onAttackEntity(Player player, Entity entity) {

        if (entity instanceof LivingEntity || entity instanceof Bot){
            LivingEntity e = (LivingEntity) player.getWorld().getEntityFromID(entity.getEntityID());
            float health = e.getHealth() - 1 - player.getHeldItem().getBonusDamnage();
            player.sendMessage("Health " + health);
            e.setHealth(health);
            if (e.getHealth() <= 0f) {
                player.getWorld().removeEntity(entity.getEntityID());
                 } else {
                ServerEntityStatusPacket packet = new ServerEntityStatusPacket(e.getEntityID(), EntityStatus.LIVING_HURT);
                for (Player p : MCThunder.getPlayers())
                    if (p.getWorld().equals(e.getWorld()) && p.isColumnLoaded(e.getChunk()))
                        p.sendPacket(packet);
            }
        } else
            player.sendMessage("Why attack a non living entity?");
    }
}