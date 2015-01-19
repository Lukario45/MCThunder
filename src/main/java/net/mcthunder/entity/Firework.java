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
    private int life = 0, lifeTime = 0;
    private ItemStack i;

    public Firework(Location location, ItemStack i) {
        super(location);
        this.type = EntityType.FIREWORKS_ROCKET;
        this.i = i;
        if (this.i.getType() != null && this.i.getType().getParent().equals(Material.FIREWORKS))
            this.metadata.setMetadata(8, this.i.getItemStack());
    }

    public Firework(World w, CompoundTag tag) {
        super(w, tag);
        IntTag life = tag.get("Life");
        IntTag lifeTime = tag.get("LifeTime");
        if (life != null)
            this.life = life.getValue();
        if (lifeTime != null)
            this.lifeTime = lifeTime.getValue();
        CompoundTag item = tag.get("FireworksItem");
        if (item != null) {
            ByteTag slot = item.get("Slot");//What is the point of this anyways
            int id;
            try {
                id = Material.fromString(((StringTag) item.get("id")).getValue().split("minecraft:")[1]).getID();
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

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        nbt.put(new IntTag("Life", this.life));
        nbt.put(new IntTag("LifeTime", this.lifeTime));
        CompoundTag item = new CompoundTag("FireworksItem");
        item.put(new ByteTag("Count", (byte) this.i.getAmount()));
        item.put(new ShortTag("Damage", this.i.getType().getData()));
        item.put(new StringTag("id", "minecraft:" + this.i.getType().getParent().getName().toLowerCase()));
        nbt.put(item);
        return nbt;
    }
}