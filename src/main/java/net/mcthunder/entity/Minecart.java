package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.block.Material;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.MinecartType;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.opennbt.tag.builtin.StringTag;
import org.spacehq.packetlib.packet.Packet;

public class Minecart extends Entity {
    protected int shakingPower = 0, shakingDirection = 0, offset = 2;
    protected float shakingMultiplier = 0;
    protected Material blockType = Material.AIR;
    protected boolean showBlock = true;

    public Minecart(Location location) {
        super(location);
        this.type = EntityType.MINECART;
        this.metadata.setMetadata(17, this.shakingPower);
        this.metadata.setMetadata(18, this.shakingDirection);
        this.metadata.setMetadata(19, this.shakingMultiplier);
        this.metadata.setMetadata(20, 0);
        this.metadata.setMetadata(21, this.offset);
        this.metadata.setMetadata(22, (byte) (this.showBlock ? 1 : 0));
    }

    public Minecart(World w, CompoundTag tag) {
        super(w, tag);
        ByteTag customDisplayTile = tag.get("CustomDisplayTile");
        StringTag displayTile = tag.get("DisplayTile");
        IntTag displayData = tag.get("DisplayData");
        IntTag displayOffset = tag.get("DisplayOffset");
        StringTag minecartName = tag.get("CustomName");
        if (minecartName != null)
            this.customName = minecartName.getValue();
        if (displayTile != null)
            this.blockType = Material.fromString(displayTile.getValue().split("minecraft:")[1]);
        if (displayData != null)
            this.blockType = Material.fromData(this.blockType, (short) (int) displayData.getValue());
        if (displayOffset != null)
            this.offset = displayOffset.getValue();
        this.metadata.setMetadata(17, this.shakingPower);
        this.metadata.setMetadata(18, this.shakingDirection);
        this.metadata.setMetadata(19, this.shakingMultiplier);
        this.metadata.setMetadata(20, this.blockType.getID() + (this.blockType.getData() << 12));
        this.metadata.setMetadata(21, this.offset);
        this.metadata.setMetadata(22, (byte) ((this.showBlock = customDisplayTile != null && customDisplayTile.getValue() == (byte) 1) ? 1 : 0));
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnObjectPacket(this.entityID, ObjectType.MINECART, MinecartType.NORMAL, this.location.getX(), this.location.getY(), this.location.getZ(),
                this.location.getYaw(), this.location.getPitch());
    }

    public void setShakingPower(int shakingPower) {
        this.metadata.setMetadata(17, this.shakingPower = shakingPower);
        updateMetadata();
    }

    public int getShakingPower() {
        return this.shakingPower;
    }

    public void setShakingDirection(int shakingDirection) {
        this.metadata.setMetadata(18, this.shakingDirection = shakingDirection);
        updateMetadata();
    }

    public int getShakingDirection() {
        return this.shakingDirection;
    }

    public void setShakingMultiplier(float shakingMultiplier) {
        this.metadata.setMetadata(19, this.shakingMultiplier = shakingMultiplier);
        updateMetadata();
    }

    public float getShakingMultiplier() {
        return this.shakingMultiplier;
    }

    public void setBlockType(Material type) {
        this.blockType = type;
        this.metadata.setMetadata(20, this.blockType.getID() + (this.blockType.getData() << 12));
        updateMetadata();
    }

    public Material getBlockType() {
        return this.blockType;
    }

    public void setShowBlock(boolean showBlock) {
        this.metadata.setMetadata(22, (byte) ((this.showBlock = showBlock) ? 1 : 0));
        updateMetadata();
    }

    public boolean showBlock() {
        return this.showBlock;
    }

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        nbt.put(new ByteTag("CustomDisplayTile", (byte) (this.showBlock ? 1 : 0)));
        if (!this.blockType.equals(Material.AIR))
            nbt.put(new StringTag("DisplayTile", "minecraft:" + this.blockType.getParent().getName().toLowerCase()));
        if (this.blockType.getData() != 0)
            nbt.put(new IntTag("DisplayData", this.blockType.getData()));
        nbt.put(new IntTag("DisplayOffset", this.offset));
        if (this.customName != null && !this.customName.equals(""))
            nbt.put(new StringTag("CustomName", this.customName));
        return nbt;
    }
}