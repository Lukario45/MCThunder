package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.material.Material;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.ShortTag;
import org.spacehq.packetlib.packet.Packet;

public class Enderman extends LivingEntity {
    private Material blockType;
    private boolean screaming;

    public Enderman(Location location) {
        super(location);
        this.type = EntityType.ENDERMAN;
        this.blockType = Material.AIR;
        this.metadata.setMetadata(16, this.blockType.getParent().getID().shortValue());
        this.metadata.setMetadata(17, (byte) this.blockType.getData());
        this.metadata.setMetadata(18, (byte) ((this.screaming = false) ? 1 : 0));
    }

    public Enderman(World w, CompoundTag tag) {
        super(w, tag);
        ShortTag carried = tag.get("carried");
        ShortTag carriedData = tag.get("carriedData");
        this.blockType = Material.fromData(carried == null ? 0 : carried.getValue(), carriedData == null ? 0 : carriedData.getValue());
        this.metadata.setMetadata(16, this.blockType.getParent().getID().shortValue());
        this.metadata.setMetadata(17, (byte) this.blockType.getData());
        this.metadata.setMetadata(18, (byte) ((this.screaming = false) ? 1 : 0));
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.ENDERMAN, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), getMotion().getdX(), getMotion().getdY(), getMotion().getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    public void setBlockType(Material type) {
        this.blockType = type;
        this.metadata.setMetadata(16, this.blockType.getParent().getID().shortValue());
        this.metadata.setMetadata(17, (byte) this.blockType.getData());
        updateMetadata();
    }

    public Material getBlockType() {
        return this.blockType;
    }

    public void setScreaming(boolean screaming) {
        this.metadata.setMetadata(18, (byte) ((this.screaming = screaming) ? 1 : 0));
        updateMetadata();
    }

    public boolean isScreaming() {
        return this.screaming;
    }

    public CompoundTag getNBT() {//TODO: Return the nbt
        CompoundTag nbt = super.getNBT();
        return nbt;
    }
}