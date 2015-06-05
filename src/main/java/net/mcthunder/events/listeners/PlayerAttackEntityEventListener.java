package net.mcthunder.events.listeners;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Location;
import net.mcthunder.entity.Bot;
import net.mcthunder.entity.Entity;
import net.mcthunder.entity.LivingEntity;
import net.mcthunder.entity.Player;
import net.mcthunder.events.interfaces.PlayerAttackEntityEventListenerInterface;
import org.spacehq.mc.protocol.data.game.values.entity.EntityStatus;
import org.spacehq.mc.protocol.data.game.values.entity.player.GameMode;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityStatusPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerPlaySoundPacket;
import org.spacehq.packetlib.packet.Packet;

public class PlayerAttackEntityEventListener implements PlayerAttackEntityEventListenerInterface {
    @Override
    public boolean removeDefaultListener() {
        return false;
    }

    @Override
    public void onAttackEntity(Player player, Entity entity) {
        if (entity instanceof LivingEntity) {
            LivingEntity e = (LivingEntity) entity;
            if (e instanceof Player && ((Player) e).getGameMode().equals(GameMode.CREATIVE))
                return;
            e.setHealth(e.getHealth() - 1 - player.getHeldItem().getBonusDamnage());
            Packet status = e.getHealth() <= 0f ? new ServerEntityStatusPacket(entity.getEntityID(), EntityStatus.DEAD) :
                    new ServerEntityStatusPacket(e.getEntityID(), EntityStatus.LIVING_HURT);
            Location l = entity.getLocation();
            Packet sound = new ServerPlaySoundPacket(e.getHealth() <= 0f ? e.getDeathSound() : e.getHurtSound(), l.getX(), l.getY(), l.getZ(), 1, 1);
            for (Player p : MCThunder.getPlayers())
                if (p.getWorld().equals(e.getWorld()) && (p.isColumnLoaded(e.getChunk()) || e.getHealth() <= 0f)) {
                    p.sendPacket(status);
                    p.sendPacket(sound);
                }
            if (e.getHealth() <= 0f) {
                if (entity instanceof Player)
                    ((Player) entity).respawn();
                else if (!(entity instanceof Bot))
                    player.getWorld().removeEntity(entity.getEntityID());
            }
        }
        //Message about attacking non living entity is stupid as paintings armor stands boats minecarts itemframes... are nonliving
    }
}