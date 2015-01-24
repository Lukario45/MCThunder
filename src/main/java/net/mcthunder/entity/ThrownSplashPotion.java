package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.api.PotionEffect;
import net.mcthunder.api.PotionEffectType;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.ObjectType;
import org.spacehq.mc.protocol.data.game.values.entity.SplashPotionData;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.opennbt.tag.builtin.StringTag;
import org.spacehq.packetlib.packet.Packet;

public class ThrownSplashPotion extends Projectile {
    private PotionEffect potionEffect = null;
    private String ownerName = "";
    private byte shake = 0;

    public ThrownSplashPotion(Location location) {
        super(location);
        this.type = EntityType.THROWN_SPLASH_POTION;
    }

    public ThrownSplashPotion(World w, CompoundTag tag) {
        super(w, tag);
        ByteTag shake = tag.get("shake");
        StringTag ownerName = tag.get("ownerName");
        if (shake != null)
            this.shake = shake.getValue();
        if (ownerName != null)
            this.ownerName = ownerName.getValue();
        CompoundTag potionEffect = tag.get("Potion");
        ByteTag id = potionEffect.get("Id");
        ByteTag amplifier = potionEffect.get("Amplifier");
        IntTag duration = potionEffect.get("Duration");
        ByteTag ambient = potionEffect.get("Ambient");
        ByteTag showParticles = potionEffect.get("ShowParticles");
        if (id == null || amplifier == null || duration == null)
            return;
        this.potionEffect = new PotionEffect(PotionEffectType.fromID(id.getValue()), duration.getValue(), amplifier.getValue());
        this.potionEffect.setAmbient(ambient != null && ambient.getValue() == (byte) 1);
        this.potionEffect.setShowParticles(showParticles != null && showParticles.getValue() == (byte) 1);
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnObjectPacket(this.entityID, ObjectType.POTION, new SplashPotionData(this.potionEffect.getType().getID()), this.location.getX(), this.location.getY(),
                this.location.getZ(), this.location.getYaw(), this.location.getPitch());
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

    public void setPotionEffect(PotionEffect potionEffect) {
        this.potionEffect = potionEffect;
    }

    public PotionEffect getPotionEffect() {
        return this.potionEffect;
    }

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        if (this.ownerName != null && !this.ownerName.equals(""))
            nbt.put(new StringTag("ownerName", this.ownerName));
        nbt.put(new ByteTag("shake", this.shake));
        if (this.potionEffect != null) {
            CompoundTag potion = new CompoundTag("Potion");
            potion.put(new ByteTag("Id", (byte) this.potionEffect.getType().getID()));
            potion.put(new ByteTag("Amplifier", (byte) this.potionEffect.getAmplifier()));
            potion.put(new IntTag("Duration", this.potionEffect.getDuration()));
            potion.put(new ByteTag("Ambient", (byte) (this.potionEffect.isAmbient() ? 1 : 0)));
            potion.put(new ByteTag("ShowParticles", (byte) (this.potionEffect.showParticles() ? 1 : 0)));
            nbt.put(potion);
        }
        return nbt;
    }
}