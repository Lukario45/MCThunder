package net.mcthunder.events.listeners;

import net.mcthunder.MCThunder;
import net.mcthunder.entity.Entity;
import net.mcthunder.entity.EntityType;
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
        if (entity.getType().isCreature() || entity instanceof Player){
            float health = (float) entity.getMetadata().getMetadata(6);
            health = health - player.getHeldItem().getType().getAttackDamnage();
            player.sendMessage("Health " + health);
           player.getWorld().getEntityFromID(entity.getEntityID()).getMetadata().setMetadata(6, health);
            if ((float) player.getWorld().getEntityFromID(entity.getEntityID()).getMetadata().getMetadata(6) <= 0f){
                ServerDestroyEntitiesPacket destroyEntitiesPacket = new ServerDestroyEntitiesPacket(entity.getEntityID());
                player.sendPacket(destroyEntitiesPacket);
               player.getWorld().removeEntity(entity.getEntityID());


            }

        } else {
            player.sendMessage("Why attack a non living entity?");
        }



    }
}