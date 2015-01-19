package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.world.World;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.opennbt.tag.builtin.StringTag;

import java.util.UUID;

public abstract class Ageable extends LivingEntity {
    private byte age = 0;

    protected Ageable(Location location) {
        super(location);
        this.metadata.setMetadata(12, this.age);//age in ticks, negative implies child
    }

    protected Ageable(World w, CompoundTag tag) {
        super(w, tag);
        IntTag inLove = tag.get("InLove");
        IntTag age = tag.get("Age");
        IntTag forcedAge = tag.get("ForcedAge");
        StringTag owner = tag.get("Owner");//Legacy support for pre 1.8
        UUID ownerUUID = tag.get("OwnerUUID") != null ? UUID.fromString(((StringTag) tag.get("OwnerUUID")).getValue()) : null;
        ByteTag sitting = tag.get("Sitting");//1 true, 0 false
        if (age != null)
            this.age = (byte)(int) age.getValue();
        this.metadata.setMetadata(12, this.age);//age in ticks, negative implies child
    }

    public void setAge(byte age) {
        this.metadata.setMetadata(12, this.age = age);
        updateMetadata();
    }

    public byte getAge() {
        return this.age;
    }

    public CompoundTag getNBT() {//TODO: Return the nbt
        CompoundTag nbt = super.getNBT();
        return nbt;
    }
}