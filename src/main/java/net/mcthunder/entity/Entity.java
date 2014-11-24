package net.mcthunder.entity;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Location;
import net.mcthunder.api.Player;
import org.spacehq.mc.protocol.data.game.EntityMetadata;
import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.mc.protocol.data.game.values.entity.Art;
import org.spacehq.mc.protocol.data.game.values.entity.HangingDirection;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.*;
import org.spacehq.packetlib.packet.Packet;

public class Entity {
    //TODO make one for each type of entity with also their ai and spawning things as well as fix entity id and things

    public static void spawn(Location l, EntityType type) {
        int entityID = (int) Math.ceil(Math.random() * Integer.MAX_VALUE);
        Packet packet = null;
        if (type == null)
            return;
        else if (type.equals(EntityType.PAINTING))
            packet = new ServerSpawnPaintingPacket(entityID, Art.ALBAN, new Position((int)l.getX(), (int)l.getY(), (int)l.getZ()), HangingDirection.EAST);
        else if (type.equals(EntityType.XP_ORB))
            packet = new ServerSpawnExpOrbPacket(entityID, l.getX(), l.getY(), l.getZ(), 0);
        else if (type.isCreature())
            packet = new ServerSpawnMobPacket(entityID, MobType.valueOf(type.getName()), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch(), 0, 0, 0, 0, new EntityMetadata[0]);
        if (packet != null) {
            for (Player p : MCThunder.getPlayers())
                if (p.getWorld().equals(l.getWorld()))
                    p.sendPacket(packet);
        }
    }
}