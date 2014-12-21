package net.mcthunder.entity;

import net.mcthunder.api.Location;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.packetlib.packet.Packet;

public class Minecart extends Entity {
    protected int shakingPower, shakingDirection;
    protected float shakingMultiplier;
    protected boolean showBlock;
    protected short block, blockData;

    public Minecart(Location location) {
        super(location);
        this.type = EntityType.MINECART;
        this.shakingPower = 0;
        this.shakingDirection = 0;
        this.shakingMultiplier = 0;
        this.block = 0;
        this.blockData = 0;
        this.showBlock = true;
        this.metadata.setMetadata(17, this.shakingPower);
        this.metadata.setMetadata(18, this.shakingDirection);
        this.metadata.setMetadata(19, this.shakingMultiplier);
        this.metadata.setMetadata(20, (this.block << 16) | (this.blockData&0xFFFF));
        this.metadata.setMetadata(21, (int) this.location.getY());
        this.metadata.setMetadata(22, (byte) (this.showBlock ? 1 : 0));
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnObjectPacket(this.entityID, ObjectType.MINECART, this.location.getX(), this.location.getY(), this.location.getZ(),
                this.location.getYaw(), this.location.getPitch());
    }
}