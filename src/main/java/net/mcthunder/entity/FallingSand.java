package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.block.Material;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.entity.type.object.FallingBlockData;
import org.spacehq.mc.protocol.data.game.entity.type.object.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.opennbt.tag.builtin.*;
import org.spacehq.packetlib.packet.Packet;

public class FallingSand extends Entity {
    private boolean dropItem = false, hurtEntities = false;
    private Material falling = Material.SAND;
    private int maxDamage = 1;
    private float damage = 1;
    private byte time = 1;

    public FallingSand(Location location) {
        super(location);
        this.type = EntityType.FALLING_SAND;
    }

    public FallingSand(World w, CompoundTag tag) {
        super(w, tag);
        IntTag tileID = tag.get("TileID");
        StringTag block = tag.get("Block");
        if (tileID != null)
            this.falling = Material.fromID(tileID.getValue());
        if (block != null)
            this.falling = Material.fromString(block.getValue().split("minecraft:")[1]);
        CompoundTag tileEntityData = tag.get("TileEntityData");//TODO: when I do more with tile entities
        ByteTag data = tag.get("Data");
        if (data != null)
            this.falling = Material.fromData(this.falling, data.getValue());
        ByteTag time = tag.get("Time");
        if (time != null)
            this.time = time.getValue();
        ByteTag dropItem = tag.get("DropItem");//1 true, 0 false
        this.dropItem = dropItem != null && dropItem.getValue() == (byte) 1;
        ByteTag hurtEntities = tag.get("HurtEntities");//1 true, 0 false
        this.hurtEntities = hurtEntities != null && hurtEntities.getValue() == (byte) 1;
        IntTag fallHurtMax = tag.get("FallHurtMax");
        if (fallHurtMax != null)
            this.maxDamage = fallHurtMax.getValue();
        FloatTag fallHurtAmount = tag.get("FallHurtAmount");
        if (fallHurtAmount != null)
            this.damage = fallHurtAmount.getValue();
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnObjectPacket(this.entityID, null, ObjectType.FALLING_BLOCK, new FallingBlockData(this.falling.getID() + (this.falling.getData() << 12), 0),
                this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch());
    }

    public void setFallingType(Material falling) {
        this.falling = falling;
    }

    public Material getFallingType() {
        return this.falling;
    }

    public void setDropItem(boolean drop) {
        this.dropItem = drop;
    }

    public boolean dropItem() {
        return this.dropItem;
    }

    public void setHurtEntities(boolean hurtEntities) {
        this.hurtEntities = hurtEntities;
    }

    public boolean hurtsEntities() {
        return this.hurtEntities;
    }

    public void setMaxDamage(int maxDamage) {
        this.maxDamage = maxDamage;
    }

    public int getMaxDamage() {
        return this.maxDamage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return this.damage;
    }

    public void setTime(byte time) {
        this.time = time;
    }

    public byte getTime() {
        return this.time;
    }

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        nbt.put(new StringTag("Block", "minecraft:" + this.falling.getParent().getName().toLowerCase()));
        //nbt.put(new CompoundTag("TileEntityData"));
        nbt.put(new ByteTag("Data", (byte) this.falling.getData()));
        nbt.put(new ByteTag("Time", this.time));
        nbt.put(new ByteTag("DropItem", (byte) (this.dropItem ? 1 : 0)));
        nbt.put(new ByteTag("HurtEntities", (byte) (this.hurtEntities ? 1 : 0)));
        nbt.put(new IntTag("FallHurtMax", this.maxDamage));
        nbt.put(new FloatTag("FallHurtAmount", this.damage));
        return nbt;
    }
}