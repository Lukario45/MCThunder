package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.packetlib.packet.Packet;

public class Guardian extends LivingEntity {
    private boolean isElder;

    public Guardian(Location location) {
        super(location);
        this.type = EntityType.GUARDIAN;
        this.isElder = false;
    }

    public Guardian(World w, CompoundTag tag) {
        super(w, tag);
        ByteTag elder = tag.get("Elder");//1 true, 0 false
        this.isElder = false;
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.GUARDIAN, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), this.motion.getdX(), this.motion.getdY(), this.motion.getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }
}