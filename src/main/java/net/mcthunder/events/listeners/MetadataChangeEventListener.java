package net.mcthunder.events.listeners;

import net.mcthunder.MCThunder;
import net.mcthunder.entity.Entity;
import net.mcthunder.entity.Player;
import org.spacehq.mc.protocol.data.game.EntityMetadata;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityMetadataPacket;

public class MetadataChangeEventListener implements net.mcthunder.interfaces.MetadataChangeEventListener {
    @Override
    public boolean removeDefaultListener() {
        return false;
    }

    @Override
    public void onMetadataChange(Entity entity) {
        EntityMetadata changes[] = entity.getMetadata().getChanges();
        if (changes == null)
            return;
        ServerEntityMetadataPacket pack = new ServerEntityMetadataPacket(entity.getEntityID(), changes);
        long chunk = entity.getChunk();
        for (Player p : MCThunder.getPlayers())
            if ((entity instanceof Player && !p.getUniqueID().equals(((Player) entity).getUniqueID())) && p.isColumnLoaded(chunk) && p.getWorld().equals(entity.getWorld()))
                p.sendPacket(pack);
    }
}