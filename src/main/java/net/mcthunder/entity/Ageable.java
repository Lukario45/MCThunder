package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.api.Utils;
import net.mcthunder.world.World;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.opennbt.tag.builtin.StringTag;

import java.util.UUID;

public abstract class Ageable extends LivingEntity {
    protected int love = 0, age = 0, forcedAge = 0;
    protected boolean sitting = false;
    protected UUID ownerUUID = null;

    protected Ageable(Location location) {
        super(location);
        this.metadata.setMetadata(12, (byte) this.age);//age in ticks, negative implies child
    }

    protected Ageable(World w, CompoundTag tag) {
        super(w, tag);
        IntTag inLove = tag.get("InLove");
        IntTag age = tag.get("Age");
        IntTag forcedAge = tag.get("ForcedAge");
        StringTag owner = tag.get("Owner");//Legacy support for pre 1.8
        StringTag uuid = tag.get("OwnerUUID");
        ByteTag sitting = tag.get("Sitting");//1 true, 0 false
        if (inLove != null)
            this.love = inLove.getValue();
        if (age != null)
            this.age = age.getValue();
        if (forcedAge != null)
            this.forcedAge = forcedAge.getValue();
        if (forcedAge != null)
            this.forcedAge = forcedAge.getValue();
        if (uuid != null)
            this.ownerUUID = UUID.fromString(uuid.getValue());
        else if (owner != null)
            this.ownerUUID = Utils.getUUIDfromString(owner.getValue());
        this.sitting = sitting != null && sitting.getValue() == (byte) 1;
        this.metadata.setMetadata(12, (byte) this.age);//age in ticks, negative implies child
    }

    public void setAge(int age) {
        this.metadata.setMetadata(12, (byte) (this.age = age));
        updateMetadata();
    }

    public int getAge() {
        return this.age;
    }

    public void setLove(int love) {
        this.love = love;
    }

    public int getLove() {
        return this.love;
    }

    public void setForcedAge(int forcedAge) {
        this.forcedAge = forcedAge;
    }

    public int getForcedAge() {
        return this.forcedAge;
    }

    public void setOwnerUUID(UUID uuid) {
        this.ownerUUID = uuid;
    }

    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    public void setSitting(boolean sitting) {
        this.sitting = sitting;
    }

    public boolean isSitting() {
        return this.sitting;
    }

    public CompoundTag getNBT() {
        CompoundTag nbt = super.getNBT();
        nbt.put(new IntTag("InLove", this.love));
        nbt.put(new IntTag("Age", this.age));
        nbt.put(new IntTag("ForcedAge", this.forcedAge));
        if (this.ownerUUID != null)
            nbt.put(new StringTag("OwnerUUID", this.ownerUUID.toString()));
        nbt.put(new ByteTag("Sitting", (byte) (this.sitting ? 1 : 0)));
        return nbt;
    }
}