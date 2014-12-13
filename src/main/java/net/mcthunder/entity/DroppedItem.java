package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.inventory.ItemStack;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityMetadataPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.packetlib.packet.Packet;

import java.util.Arrays;
import java.util.Collection;

public class DroppedItem extends Entity {
    private ItemStack i;

    public DroppedItem(Location location, ItemStack i) {
        super(location, EntityType.ITEM);
        this.i = i;
        this.metadata.setMetadata(10, this.i.getIS());
    }

    public Packet getPacket() {
        return new ServerSpawnObjectPacket(this.entityID, ObjectType.ITEM, this.location.getX(), this.location.getY(), this.location.getZ(),
                this.location.getYaw(), this.location.getPitch());
    }

    public Collection<Packet> getPackets() {//Order of packets does matter
        return Arrays.asList(getPacket(), new ServerEntityMetadataPacket(this.entityID, this.getMetadata().getMetadataArray()));
    }
}