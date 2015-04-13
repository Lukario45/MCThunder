package net.mcthunder.events.listeners;

import net.mcthunder.MCThunder;
import net.mcthunder.entity.Entity;
import net.mcthunder.entity.LivingEntity;
import net.mcthunder.entity.Player;
import net.mcthunder.events.interfaces.PlayerAttackEntityEventListenerInterface;
import org.spacehq.mc.protocol.data.game.values.entity.EntityStatus;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityStatusPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerUpdateHealthPacket;

public class PlayerAttackEntityEventListener implements PlayerAttackEntityEventListenerInterface {
    private float health;
    @Override
    public boolean removeDefaultListener() {
        return false;
    }

    @Override
    public void onAttackEntity(Player player, Entity entity) {
        if (entity instanceof LivingEntity && entity.getType().getName().equals("PLAYER")){
            LivingEntity e = (LivingEntity) player.getWorld().getEntityFromID(entity.getEntityID());
            health = e.getHealth() - 1 - player.getHeldItem().getBonusDamnage();
            e.setHealth(health);
            if (e.getHealth() <= 0f) {
                player.getWorld().removeEntity(entity.getEntityID());
                 }
            else {
                ServerEntityStatusPacket packet = new ServerEntityStatusPacket(e.getEntityID(), EntityStatus.LIVING_HURT);
                for (Player p : MCThunder.getPlayers())
                    if (p.getWorld().equals(e.getWorld()) && p.isColumnLoaded(e.getChunk()))
                        p.sendPacket(packet);
            }
        } else if (entity.getType().getName().equals("PLAYER")){
            Entity e = player.getWorld().getEntityFromID(entity.getEntityID());
            health = (float) e.getMetadata().getMetadata(6) - 1 - player.getHeldItem().getBonusDamnage();
            e.getMetadata().setMetadata(6,health);
            for (Player p: MCThunder.getPlayers()){
                if (p.getEntityID() == entity.getEntityID()){
                    ServerUpdateHealthPacket updateHealthPacket = new ServerUpdateHealthPacket(health,20,5.0f);
                    p.sendPacket(updateHealthPacket);
                }
            }

        }
        else
            player.sendMessage("Why attack a non living entity?");
    }
}