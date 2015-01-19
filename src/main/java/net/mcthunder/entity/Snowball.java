package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.StringTag;
import org.spacehq.packetlib.packet.Packet;

public class Snowball extends Projectile {
    private String ownerName = "";
    private byte shake = 0;

    public Snowball(Location location) {
        super(location);
        this.type = EntityType.SNOWBALL;
    }

    public Snowball(World w, CompoundTag tag) {
        super(w, tag);
        ByteTag shake = tag.get("shake");
        StringTag ownerName = tag.get("ownerName");
        if (shake != null)
            this.shake = shake.getValue();
        if (ownerName != null)
            this.ownerName = ownerName.getValue();
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnObjectPacket(this.entityID, ObjectType.SNOWBALL, this.location.getX(), this.location.getY(), this.location.getZ(),
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

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        if (this.ownerName != null && !this.ownerName.equals(""))
            nbt.put(new StringTag("ownerName", this.ownerName));
        nbt.put(new ByteTag("shake", this.shake));
        return nbt;
    }
}