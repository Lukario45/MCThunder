package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.ShortTag;
import org.spacehq.packetlib.packet.Packet;

public class ZombiePigman extends Zombie {
    private short anger = 0;

    public ZombiePigman(Location location) {
        super(location);
        this.type = EntityType.ZOMBIE_PIGMAN;
    }

    public ZombiePigman(World w, CompoundTag tag) {
        super(w, tag);
        ShortTag anger = tag.get("Anger");
        if (anger != null)
            this.anger = anger.getValue();
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.ZOMBIE_PIGMAN, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), getMotion().getdX(), getMotion().getdY(), getMotion().getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    public void setAnger(short anger) {
        this.anger = anger;
    }

    public short getAnger() {
        return this.anger;
    }

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        nbt.put(new ShortTag("Anger", this.anger));
        return nbt;
    }
}