package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.inventory.ItemStack;
import net.mcthunder.block.Material;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.HangingDirection;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.opennbt.tag.builtin.*;
import org.spacehq.packetlib.packet.Packet;

public class ItemFrame extends Entity {
    private HangingDirection direction = HangingDirection.SOUTH;
    private float itemDropChance = 1;
    private byte rotation = 0;
    private ItemStack i;

    public ItemFrame(Location location, ItemStack i) {
        super(location);
        this.type = EntityType.ITEM_FRAME;
        this.i = i;
        if (this.i.getType() != null && !this.i.getType().equals(Material.AIR))
            this.metadata.setMetadata(8, this.i.getItemStack());
        this.metadata.setMetadata(9, this.rotation);
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
        if (itemDropChance != null)
            this.itemDropChance = itemDropChance.getValue();
        ByteTag itemRotation = tag.get("ItemRotation");
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
        if (this.i.getType() != null && !this.i.getType().equals(Material.AIR))
            this.metadata.setMetadata(8, this.i.getItemStack());
        if (itemRotation != null)
            this.rotation = itemRotation.getValue();
        this.metadata.setMetadata(9, this.rotation);
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

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        nbt.put(new IntTag("TileX", (int) this.location.getX()));
        nbt.put(new IntTag("TileY", (int) this.location.getY()));
        nbt.put(new IntTag("TileZ", (int) this.location.getZ()));
        nbt.put(new ByteTag("Facing", (byte) (this.direction.equals(HangingDirection.SOUTH) ? 0 : this.direction.equals(HangingDirection.WEST) ? 1 :
                this.direction.equals(HangingDirection.NORTH) ? 2 : 4)));
        CompoundTag item = new CompoundTag("Item");
        item.put(new ByteTag("Count", (byte) this.i.getAmount()));
        item.put(new ShortTag("Damage", this.i.getType().getData()));
        item.put(new StringTag("id", "minecraft:" + this.i.getType().getParent().getName().toLowerCase()));
        nbt.put(item);
        nbt.put(new FloatTag("ItemDropChance", this.itemDropChance));
        nbt.put(new ByteTag("ItemRotation", this.rotation));
        return nbt;
    }
}