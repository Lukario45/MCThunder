package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.StringTag;
import org.spacehq.packetlib.packet.Packet;

public class ThrownSplashPotion extends Projectile {
    private String ownerName;
    private byte shake;

    public ThrownSplashPotion(Location location) {
        super(location);
        this.type = EntityType.THROWN_SPLASH_POTION;
        this.shake = 0;
        this.ownerName = "";
    }

    public ThrownSplashPotion(World w, CompoundTag tag) {
        super(w, tag);
        ByteTag shake = tag.get("shake");
        StringTag ownerName = tag.get("ownerName");
        this.shake = shake == null ? 0 : shake.getValue();
        this.ownerName = ownerName == null ? "" : ownerName.getValue();
        CompoundTag potion = tag.get("Potion");
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnObjectPacket(this.entityID, ObjectType.POTION, this.location.getX(), this.location.getY(), this.location.getZ(),
                this.location.getYaw(), this.location.getPitch());
    }

    public void setOwnerName(String name) {
        this.ownerName = name;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public void setShake(byte shake) {
        this.shake = shake;
    }

    public byte getShake() {
        return this.shake;
    }
}