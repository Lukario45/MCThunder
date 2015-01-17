package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.inventory.ItemStack;
import net.mcthunder.material.Material;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.opennbt.tag.builtin.*;
import org.spacehq.packetlib.packet.Packet;

public class Firework extends Entity {
    private int life, lifeTime;
    private ItemStack i;

    public Firework(Location location, ItemStack i) {
        super(location);
        this.type = EntityType.FIREWORKS_ROCKET;
        this.i = i;
        this.life = 0;
        this.lifeTime = 0;
        if (this.i.getType() != null && this.i.getType().getParent().equals(Material.FIREWORKS))
            this.metadata.setMetadata(8, this.i.getItemStack());
    }

    public Firework(World w, CompoundTag tag) {
        super(w, tag);
        IntTag life = tag.get("Life");
        IntTag lifeTime = tag.get("LifeTime");
        this.life = life == null ? 0 : life.getValue();
        this.lifeTime = lifeTime == null ? 0 : lifeTime.getValue();
        CompoundTag item = tag.get("FireworksItem");
        if (item != null) {
            ByteTag slot = item.get("Slot");//What is the point of this anyways
            int id;
            try {
                id = Material.fromString(((StringTag) item.get("id")).getValue().split("minecraft:")[1]).getParent().getID();
            } catch (Exception e) {//pre 1.8 loading should be ShortTag instead
                id = Integer.parseInt(((ShortTag) item.get("id")).getValue().toString());
            }
            this.i = new ItemStack(Material.fromData(id, ((ShortTag) item.get("Damage")).getValue()), ((ByteTag) item.get("Count")).getValue(), (CompoundTag) item.get("tag"));
        }
        if (this.i.getType() != null && this.i.getType().getParent().equals(Material.FIREWORKS))
            this.metadata.setMetadata(8, this.i.getItemStack());
    }

    @Override
    public Packet getPacket() {//TODO: Also create particles
        return new ServerSpawnObjectPacket(this.entityID, ObjectType.FIREWORK_ROCKET, this.location.getX(), this.location.getY(), this.location.getZ(),
                this.location.getYaw(), this.location.getPitch());
    }

    public void setItemStack(ItemStack i) {
        this.i = i;
        if (this.i.getType() != null && this.i.getType().getParent().equals(Material.FIREWORKS))
            this.metadata.setMetadata(8, this.i.getItemStack());
        updateMetadata();
    }

    public ItemStack getItemStack() {
        return this.i;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getLife() {
        return this.life;
    }

    public void setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
    }

    public int getLifeTime() {
        return this.lifeTime;
    }

    public CompoundTag getNBT() {//TODO: Return the nbt
        CompoundTag nbt = super.getNBT();
        return nbt;
    }
}