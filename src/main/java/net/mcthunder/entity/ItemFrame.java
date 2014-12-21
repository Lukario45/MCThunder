package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.inventory.ItemStack;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.packetlib.packet.Packet;

public class ItemFrame extends Entity {
    private ItemStack i;
    private byte rotation;

    public ItemFrame(Location location, ItemStack i) {
        super(location);
        this.type = EntityType.ITEM_FRAME;
        this.i = i;
        this.rotation = (byte) 0;
        this.metadata.setMetadata(8, this.i.getIS());
        this.metadata.setMetadata(9, this.rotation);
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnObjectPacket(this.entityID, ObjectType.ITEM_FRAME, this.location.getX(), this.location.getY(), this.location.getZ(),
                this.location.getYaw(), this.location.getPitch());
    }
}