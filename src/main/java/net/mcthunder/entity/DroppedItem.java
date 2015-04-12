package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.inventory.ItemStack;
import net.mcthunder.block.Material;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.ShortTag;
import org.spacehq.opennbt.tag.builtin.StringTag;
import org.spacehq.packetlib.packet.Packet;

public class DroppedItem extends Entity {
    private short age = 0, health = 5, pickupDelay = 0;
    private String owner = "", thrower = "";
    private ItemStack i;

    public DroppedItem(Location location, ItemStack i) {
        super(location);
        this.type = EntityType.ITEM;
        this.i = i;
        if (this.i.getType() != null && !this.i.getType().equals(Material.AIR))
            this.metadata.setMetadata(10, this.i.getItemStack());
    }

    public DroppedItem(World w, CompoundTag tag) {
        super(w, tag);
        ShortTag age = tag.get("Age");
        ShortTag health = tag.get("Health");
        ShortTag pickupDelay = tag.get("PickupDelay");
        StringTag owner = tag.get("Owner");
        StringTag thrower = tag.get("Thrower");
        if (age != null)
            this.age = age.getValue();
        this.health = health == null ? 1 : health.getValue();
        if (pickupDelay != null)
            this.pickupDelay = pickupDelay.getValue();
        if (owner != null)
            this.owner = owner.getValue();
        if (thrower != null)
            this.thrower = thrower.getValue();
        CompoundTag item = tag.get("Item");
        if (item != null) {
            ByteTag slot = item.get("Slot");//What is the point of this anyways for a dropped item
            int id;
            try {
                id = Material.fromString(((StringTag) item.get("id")).getValue().split("minecraft:")[1]).getID();
            } catch (Exception e) {//pre 1.8 loading should be ShortTag instead
                id = Integer.parseInt(((ShortTag) item.get("id")).getValue().toString());
            }
            this.i = new ItemStack(Material.fromData(id, ((ShortTag) item.get("Damage")).getValue()), ((ByteTag) item.get("Count")).getValue(), (CompoundTag) item.get("tag"));
        }
        if (this.i.getType() != null && !this.i.getType().equals(Material.AIR))
            this.metadata.setMetadata(10, this.i.getItemStack());
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnObjectPacket(this.entityID, ObjectType.ITEM, this.location.getX(), this.location.getY(), this.location.getZ(),
                this.location.getYaw(), this.location.getPitch());
    }

    public void setItemStack(ItemStack i) {
        this.i = i;
        if (this.i.getType() != null && !this.i.getType().equals(Material.AIR))
            this.metadata.setMetadata(10, this.i.getItemStack());
        updateMetadata();
    }

    public ItemStack getItemStack() {
        return this.i;
    }

    public short getAge() {
        return this.age;
    }

    public void setAge(short age) {
        this.age = age;
    }

    public short getHealth() {
        return this.health;
    }

    public void setHealth(short health) {
        this.health = health;
    }

    public short getPickupDelay() {
        return this.pickupDelay;
    }

    public void setPickupDelay(short pickupDelay) {
        this.pickupDelay = pickupDelay;
    }

    public String getOwner() {
        return this.owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getThrower() {
        return this.thrower;
    }

    public void setThrower(String thrower) {
        this.thrower = thrower;
    }

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        nbt.put(new ShortTag("Age", this.age));
        nbt.put(new ShortTag("Health", this.health));
        nbt.put(new ShortTag("PickupDelay", this.pickupDelay));
        if (this.owner != null && !this.owner.equals(""))
            nbt.put(new StringTag("Owner", this.owner));
        if (this.thrower != null && !this.thrower.equals(""))
            nbt.put(new StringTag("Thrower", this.thrower));
        CompoundTag item = new CompoundTag("Item");
        item.put(new ByteTag("Count", (byte) this.i.getAmount()));
        item.put(new ShortTag("Damage", this.i.getType().getData()));
        item.put(new StringTag("id", "minecraft:" + this.i.getType().getParent().getName().toLowerCase()));
        nbt.put(item);
        return nbt;
    }
}