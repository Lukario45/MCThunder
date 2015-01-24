package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.mc.protocol.data.game.values.entity.ProjectileData;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.DoubleTag;
import org.spacehq.opennbt.tag.builtin.ShortTag;
import org.spacehq.packetlib.packet.Packet;

public class Arrow extends Projectile {
    private boolean isCritical = false;
    private byte shake = 0;

    public Arrow(Location location) {
        super(location);
        this.type = EntityType.ARROW;
        this.metadata.setMetadata(16, (byte) (this.isCritical ? 1 : 0));
    }

    public Arrow(World w, CompoundTag tag) {
        super(w, tag);
        this.metadata.setMetadata(16, (byte) (this.isCritical ? 1 : 0));
        ByteTag shake = tag.get("shake");
        ByteTag inData = tag.get("inData");
        ByteTag pickup = tag.get("pickup");
        ByteTag player = tag.get("player");//1 true, 0 false
        ShortTag life = tag.get("life");
        DoubleTag damage = tag.get("damage");
        ByteTag inGround = tag.get("inGround");
        if (shake != null)
            this.shake = shake.getValue();
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnObjectPacket(this.entityID, ObjectType.ARROW, new ProjectileData(getOwnerID()), this.location.getX(), this.location.getY(), this.location.getZ(),
                this.location.getYaw(), this.location.getPitch());
    }

    public void setCritical(boolean isCritical) {
        this.metadata.setMetadata(16, (byte) ((this.isCritical = isCritical) ? 1 : 0));
        updateMetadata();
    }

    public boolean isCritical() {
        return this.isCritical;
    }

    public void setShake(byte shake) {
        this.shake = shake;
    }

    public byte getShake() {
        return this.shake;
    }

    public CompoundTag getNBT() {//TODO: Return the nbt
        CompoundTag nbt = super.getNBT();
        return nbt;
    }
}