package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.material.Material;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.opennbt.tag.builtin.StringTag;
import org.spacehq.packetlib.packet.Packet;

public class Minecart extends Entity {
    protected int shakingPower, shakingDirection;
    protected float shakingMultiplier;
    protected Material blockType;
    protected boolean showBlock;

    public Minecart(Location location) {
        super(location);
        this.type = EntityType.MINECART;
        this.blockType = Material.AIR;
        this.metadata.setMetadata(17, this.shakingPower = 0);
        this.metadata.setMetadata(18, this.shakingDirection = 0);
        this.metadata.setMetadata(19, this.shakingMultiplier = 0);
        this.metadata.setMetadata(20, 0);
        this.metadata.setMetadata(21, (int) this.location.getY());
        this.metadata.setMetadata(22, (byte) ((this.showBlock = true) ? 1 : 0));
    }

    public Minecart(World w, CompoundTag tag) {
        super(w, tag);
        ByteTag customDisplayTile = tag.get("CustomDisplayTile");
        StringTag displayTile = tag.get("DisplayTile");
        IntTag displayData = tag.get("DisplayData");
        IntTag displayOffset = tag.get("DisplayOffset");
        StringTag minecartName = tag.get("CustomName");
        this.blockType = Material.AIR;
        this.metadata.setMetadata(17, this.shakingPower = 0);
        this.metadata.setMetadata(18, this.shakingDirection = 0);
        this.metadata.setMetadata(19, this.shakingMultiplier = 0);
        this.metadata.setMetadata(20, (this.blockType.getParent().getID().shortValue() << 16) | (this.blockType.getData()&0xFFFF));
        this.metadata.setMetadata(21, (int) this.location.getY());
        this.metadata.setMetadata(22, (byte) ((this.showBlock = true) ? 1 : 0));
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnObjectPacket(this.entityID, ObjectType.MINECART, this.location.getX(), this.location.getY(), this.location.getZ(),
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
        this.metadata.setMetadata(20, (this.blockType.getParent().getID().shortValue() << 16) | (this.blockType.getData()&0xFFFF));
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

    public CompoundTag getNBT() {//TODO: Return the nbt
        CompoundTag nbt = super.getNBT();
        return nbt;
    }
}