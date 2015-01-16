package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.packetlib.packet.Packet;

public class Chicken extends Ageable {
    private boolean chickenJockey;
    private int layTime;

    public Chicken(Location location) {
        super(location);
        this.type = EntityType.CHICKEN;
        this.chickenJockey = false;
        this.layTime = 0;
    }

    public Chicken(World w, CompoundTag tag) {
        super(w, tag);
        ByteTag isChickenJockey = tag.get("IsChickenJockey");//1 true, 0 false
        IntTag eggLayTime = tag.get("EggLayTime");
        this.chickenJockey = isChickenJockey != null && isChickenJockey.getValue() == (byte) 1;
        this.layTime = eggLayTime == null ? 0 : eggLayTime.getValue();
    }

    public Packet getPacket() {
        return new ServerSpawnMobPacket(this.entityID, MobType.CHICKEN, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(),
                this.location.getPitch(), this.location.getYaw(), this.motion.getdX(), this.motion.getdY(), this.motion.getdZ(), getMetadata().getMetadataArray());
    }

    @Override
    public void ai() {

    }

    public boolean isChickenJockey() {
        return this.chickenJockey;
    }

    public void setChickenJockey(boolean chickenJockey) {
        this.chickenJockey = chickenJockey;
    }

    public int getLayTime() {
        return this.layTime;
    }

    public void setLayTime(int layTime) {
        this.layTime = layTime;
    }
}