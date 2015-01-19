package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.packetlib.packet.Packet;

public class Guardian extends LivingEntity {
    private boolean isElder = false;

    public Guardian(Location location) {
        super(location);
        this.type = EntityType.GUARDIAN;
    }

    public Guardian(World w, CompoundTag tag) {
        super(w, tag);
        ByteTag elder = tag.get("Elder");//1 true, 0 false
        this.isElder = elder != null && elder.getValue() == (byte) 1;
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.GUARDIAN, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), getMotion().getdX(), getMotion().getdY(), getMotion().getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    public void setElder(boolean elder) {
        this.isElder = elder;
    }

    public boolean isElder() {
        return this.isElder;
    }

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        nbt.put(new ByteTag("Elder", (byte) (this.isElder ? 1 : 0)));
        return nbt;
    }
}