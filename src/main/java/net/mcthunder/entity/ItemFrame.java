package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.inventory.ItemStack;
import net.mcthunder.material.Material;
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
        if (this.i.getType() != null && !this.i.getType().equals(Material.AIR))
            this.metadata.setMetadata(8, this.i.getItemStack());
        this.metadata.setMetadata(9, this.rotation = (byte) 0);
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnObjectPacket(this.entityID, ObjectType.ITEM_FRAME, this.location.getX(), this.location.getY(), this.location.getZ(),
                this.location.getYaw(), this.location.getPitch());
    }

    public void setItemStack(ItemStack i) {
        this.i = i;
        if (this.i.getType() != null && !this.i.getType().equals(Material.AIR))
            this.metadata.setMetadata(8, this.i.getItemStack());
        updateMetadata();
    }

    public ItemStack getItemStack() {
        return this.i;
    }

    public void setRotation(byte rotation) {
        this.metadata.setMetadata(9, this.rotation = rotation);
        updateMetadata();
    }

    public byte getRotation() {
        return this.rotation;
    }
}