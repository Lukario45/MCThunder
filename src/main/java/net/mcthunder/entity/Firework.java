package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.inventory.ItemStack;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.packetlib.packet.Packet;

public class Firework extends Entity {
    private ItemStack i;

    public Firework(Location location, ItemStack i) {
        super(location);
        this.type = EntityType.FIREWORKS_ROCKET;
        this.metadata.setMetadata(8, (this.i = i).getIS());
    }

    @Override
    public Packet getPacket() {//TODO: Also create particles
        return new ServerSpawnObjectPacket(this.entityID, ObjectType.FIREWORK_ROCKET, this.location.getX(), this.location.getY(), this.location.getZ(),
                this.location.getYaw(), this.location.getPitch());
    }

    public void setItemStack(ItemStack i) {
        this.metadata.setMetadata(8, (this.i = i).getIS());
        updateMetadata();
    }

    public ItemStack getItemStack() {
        return this.i;
    }
}