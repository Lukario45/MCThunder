package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.inventory.ItemStack;
import net.mcthunder.material.Material;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.HangingDirection;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.opennbt.tag.builtin.*;
import org.spacehq.packetlib.packet.Packet;

public class ItemFrame extends Entity {
    private HangingDirection direction;
    private float itemDropChance;
    private byte rotation;
    private ItemStack i;

    public ItemFrame(Location location, ItemStack i) {
        super(location);
        this.type = EntityType.ITEM_FRAME;
        this.direction = HangingDirection.SOUTH;
        this.i = i;
        if (this.i.getType() != null && !this.i.getType().equals(Material.AIR))
            this.metadata.setMetadata(8, this.i.getItemStack());
        this.metadata.setMetadata(9, this.rotation = (byte) 0);
        this.itemDropChance = 1;
    }

    public ItemFrame(World w, CompoundTag tag) {
        super(w, tag);
        IntTag tileX = tag.get("TileX");//Is retrieving this needed
        IntTag tileY = tag.get("TileY");//Is retrieving this needed
        IntTag tileZ = tag.get("TileZ");//Is retrieving this needed
        ByteTag facing = tag.get("Facing");
        byte temp = facing == null ? 0 : facing.getValue();
        this.direction = temp == 0 ? HangingDirection.SOUTH : temp == (byte) 1 ? HangingDirection.WEST : temp == (byte) 2 ?
                HangingDirection.NORTH : HangingDirection.EAST;
        CompoundTag item = tag.get("Item");
        FloatTag itemDropChance = tag.get("ItemDropChance");
        this.itemDropChance = itemDropChance == null ? 1 : itemDropChance.getValue();
        ByteTag itemRotation = tag.get("ItemRotation");
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
        if (this.i.getType() != null && !this.i.getType().equals(Material.AIR))
            this.metadata.setMetadata(8, this.i.getItemStack());
        this.metadata.setMetadata(9, this.rotation = itemRotation == null ? (byte) 0 : itemRotation.getValue());
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

    public void setItemDropChance(float itemDropChance) {
        this.itemDropChance = itemDropChance;
    }

    public float getItemDropChance() {
        return this.itemDropChance;
    }

    public void setHangingDirection(HangingDirection direction) {
        this.direction = direction;
    }

    public HangingDirection getHangingDirection() {
        return this.direction;
    }
}