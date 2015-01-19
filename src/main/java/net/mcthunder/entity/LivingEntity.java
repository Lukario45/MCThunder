package net.mcthunder.entity;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Location;
import net.mcthunder.api.Modifier;
import net.mcthunder.api.PotionEffect;
import net.mcthunder.api.PotionEffectType;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.packet.ingame.server.entity.*;
import org.spacehq.opennbt.tag.builtin.*;
import org.spacehq.packetlib.packet.Packet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

public abstract class LivingEntity extends Entity {//TODO: set default max healths for each living entity type
    protected HashMap<PotionEffectType, PotionEffect> activeEffects = new HashMap<>();
    private ArrayList<Modifier> modifiers = new ArrayList<>();
    private boolean alwaysShowName = false, hasAI = true;
    protected PotionEffectType latest = null;
    protected float health, maxHealth;
    private byte arrows = 0;

    protected LivingEntity(Location location) {
        super(location);
        this.maxHealth = 20;
        this.metadata.setMetadata(2, this.customName);
        this.metadata.setMetadata(3, (byte) (this.alwaysShowName ? 1 : 0));
        this.metadata.setMetadata(6, this.health = this.maxHealth);
        this.metadata.setMetadata(7, 0);//No color until a potion is set
        this.metadata.setMetadata(8, (byte) 0);//False until potion effects are set
        this.metadata.setMetadata(9, this.arrows);
        this.metadata.setMetadata(15, (byte) (this.hasAI ? 1 : 0));
    }

    protected LivingEntity(World w, CompoundTag tag) {
        super(w, tag);
        FloatTag healF = tag.get("HealF");
        ShortTag health = tag.get("Health");
        FloatTag absorptionAmount = tag.get("AbsorptionAmount");
        ShortTag attackTime = tag.get("AttackTime");
        ShortTag hurtTime = tag.get("HurtTime");
        IntTag hurtByTimestamp = tag.get("HurtByTimestamp");
        ShortTag deathTime = tag.get("DeathTime");
        ListTag attributes = tag.get("Attributes");
        if (attributes != null)
            for (int j = 0; j < attributes.size(); j++) {
                CompoundTag attribute = attributes.get(j);
                StringTag name = attribute.get("Name");
                DoubleTag base = attribute.get("Base");
                ListTag modifiers = attribute.get("Modifiers");
                if (modifiers != null)
                    for (int k = 0; k < modifiers.size(); k++) {
                        CompoundTag modifier = modifiers.get(k);
                        StringTag modName = modifier.get("Name");
                        DoubleTag amount = modifier.get("Amount");
                        IntTag operation = modifier.get("Operation");
                        LongTag modUUIDMost = modifier.get("UUIDMost");
                        LongTag modUUIDLeast = modifier.get("UUIDLeast");
                        if (modName != null && name != null && amount != null && operation != null && modUUIDMost != null && modUUIDLeast != null)
                            this.modifiers.add(new Modifier(name.getValue(), modName.getValue(), amount.getValue(), operation.getValue(),
                                    modUUIDMost.getValue(), modUUIDLeast.getValue()));
                    }
            }
        ListTag activeEffects = tag.get("ActiveEffects");
        if (activeEffects != null)
            for (int j = 0; j < activeEffects.size(); j++) {
                CompoundTag activeEffect = activeEffects.get(j);
                ByteTag effectID = activeEffect.get("Id");
                ByteTag amplifier = activeEffect.get("Amplifier");
                IntTag duration = activeEffect.get("Duration");
                ByteTag ambient = activeEffect.get("Ambient");//1 true, 0 false
                ByteTag showParticles = activeEffect.get("ShowParticles");//1 true, 0 false
                if (effectID == null || amplifier == null || duration == null)
                    continue;
                PotionEffect potion = new PotionEffect(PotionEffectType.fromID(effectID.getValue()), duration.getValue(), amplifier.getValue());
                potion.setAmbient(ambient != null && ambient.getValue() == (byte) 1);
                potion.setShowParticles(showParticles != null && showParticles.getValue() == (byte) 1);
                this.activeEffects.put(this.latest = potion.getType(), potion);
            }
        ListTag equipment = tag.get("Equipment");
        if (equipment != null) {
            CompoundTag hand = equipment.get(0);
            CompoundTag boots = equipment.get(1);
            CompoundTag leggings = equipment.get(2);
            CompoundTag chestplate = equipment.get(3);
            CompoundTag helmet = equipment.get(4);
        }
        ListTag dropChances = tag.get("DropChances");
        if (dropChances != null) {
            FloatTag hand = dropChances.get(0);
            FloatTag boots = dropChances.get(1);
            FloatTag leggings = dropChances.get(2);
            FloatTag chestplate = dropChances.get(3);
            FloatTag helmet = dropChances.get(4);
        }
        ByteTag canPickUpLoot = tag.get("CanPickUpLoot");//1 true, 0 false
        ByteTag noAI = tag.get("NoAI");//1 true, 0 false
        ByteTag persistenceRequired = tag.get("PersistenceRequired");//1 true, 0 false
        ByteTag leashed = tag.get("Leashed");//1 true, 0 false
        CompoundTag leash = tag.get("Leash");
        if (leash != null) {
            LongTag leashUUIDMost = leash.get("UUIDMost");
            LongTag leashUUIDLeast = leash.get("UUIDLeast");
            IntTag leashX = leash.get("X");
            IntTag leashY = leash.get("Y");
            IntTag leashZ = leash.get("Z");
        }
        ByteTag customNameVisible = tag.get("CustomNameVisible");//1 true, 0 false
        this.maxHealth = healF == null ? health == null ? 20 : health.getValue() : healF.getValue();
        this.metadata.setMetadata(2, this.customName);
        this.metadata.setMetadata(3, (byte) ((this.alwaysShowName = customNameVisible != null && customNameVisible.getValue() == (byte) 1) ? 1 : 0));
        this.metadata.setMetadata(6, this.health = this.maxHealth);
        this.metadata.setMetadata(7, this.activeEffects.size() == 0 ? 0 : this.latest.getColor());//No color until a potion is set
        this.metadata.setMetadata(8, (byte) (this.activeEffects.size() == 0 ? 0 : this.activeEffects.get(this.latest).isAmbient() ? 1 : 0));//False until potion effects are set
        this.metadata.setMetadata(9, this.arrows);
        this.metadata.setMetadata(15, (byte) ((this.hasAI = noAI != null && noAI.getValue() == (byte) 0) ? 1 : 0));
    }

    public abstract void ai();

    public void setCustomName(String name) {
        this.metadata.setMetadata(2, this.customName = name);
        updateMetadata();
    }

    public String getName() {
        return this.customName;
    }

    public void setAlwaysShowName(boolean show) {
        this.metadata.setMetadata(3, (byte) ((this.alwaysShowName = show) ? 1 : 0));
        updateMetadata();
    }

    public boolean alwaysShowName() {
        return this.alwaysShowName;
    }

    public void setHealth(float health) {
        this.metadata.setMetadata(6, this.health = health);
        updateMetadata();
    }

    public float getHealth() {
        return this.health;
    }

    public void addPotionEffect(PotionEffect p) {
        this.latest = p.getType();
        this.activeEffects.put(p.getType(), p);
        this.metadata.setMetadata(7, p.getType().getColor());
        this.metadata.setMetadata(8, (byte) (p.isAmbient() ? 1 : 0));
        updateMetadata();
        //TODO: Have the duration tick down and then run out also for all potion effect stuff have it actually send effects and things to player
    }

    public void removePotionEffect(PotionEffectType p) {
        this.activeEffects.remove(p);
        if (this.latest != null && p.equals(this.latest)) {
            if (this.activeEffects.isEmpty()) {
                this.latest = null;
                this.metadata.setMetadata(7, 0);
                this.metadata.setMetadata(8, (byte) 0);
                updateMetadata();
                return;
            }
            this.latest = (PotionEffectType) this.activeEffects.keySet().toArray()[0];
            this.metadata.setMetadata(7, this.latest.getColor());
            this.metadata.setMetadata(8, (byte) (this.activeEffects.get(this.latest).isAmbient() ? 1 : 0));
            updateMetadata();
        }
    }

    public void clearPotionEffects() {
        this.activeEffects.clear();
        this.latest = null;
        this.metadata.setMetadata(7, 0);
        this.metadata.setMetadata(8, (byte) 0);
        updateMetadata();
    }

    public Collection<PotionEffect> getActiveEffects() {
        return this.activeEffects.values();
    }

    protected void moveForward(double distance) {
        if (this.location == null)
            return;
        Location old = getLocation();
        this.location.setX(this.location.getX() - distance * Math.sin(Math.toRadians(this.location.getYaw())));
        this.location.setZ(this.location.getZ() + distance * Math.cos(Math.toRadians(this.location.getYaw())));
        Packet update;
        int packed = (int) (distance * 32);
        if (packed > Byte.MAX_VALUE || packed < Byte.MIN_VALUE)
            update = new ServerEntityTeleportPacket(this.entityID, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch(), isOnGround());
        else
            update = new ServerEntityPositionPacket(this.entityID, this.location.getX() - old.getX(), 0, this.location.getZ() - old.getZ(), isOnGround());
        long chunk = getChunk();
        for (Player p : MCThunder.getPlayers())
            if (p.getWorld().equals(getWorld()) && p.isColumnLoaded(chunk))
                p.sendPacket(update);
    }

    protected void moveBackward(double distance) {
        moveForward(-distance);
    }

    protected void moveRight(double distance) {
        if (this.location == null)
            return;
        Location old = getLocation();
        this.location.setX(this.location.getX() - distance * Math.cos(Math.toRadians(this.location.getYaw())));
        this.location.setZ(this.location.getZ() + distance * Math.sin(Math.toRadians(this.location.getYaw())));
        Packet update;
        int packed = (int) (distance * 32);
        if (packed > Byte.MAX_VALUE || packed < Byte.MIN_VALUE)
            update = new ServerEntityTeleportPacket(this.entityID, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch(), isOnGround());
        else
            update = new ServerEntityPositionPacket(this.entityID, this.location.getX() - old.getX(), 0, this.location.getZ() - old.getZ(), isOnGround());
        long chunk = getChunk();
        for (Player p : MCThunder.getPlayers())
            if (p.getWorld().equals(getWorld()) && p.isColumnLoaded(chunk))
                p.sendPacket(update);
    }

    protected void moveLeft(double distance) {
        moveRight(-distance);
    }

    protected void moveUp(double distance) {
        if (this.location == null)
            return;
        Location old = getLocation();
        this.location.setY(this.location.getY() + distance);
        Packet update;
        int packed = (int) (distance * 32);
        if (packed > Byte.MAX_VALUE || packed < Byte.MIN_VALUE)
            update = new ServerEntityTeleportPacket(this.entityID, this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch(), isOnGround());
        else
            update = new ServerEntityPositionPacket(this.entityID, 0, this.location.getY() - old.getY(), 0, isOnGround());
        long chunk = getChunk();
        for (Player p : MCThunder.getPlayers())
            if (p.getWorld().equals(getWorld()) && p.isColumnLoaded(chunk))
                p.sendPacket(update);
    }

    protected void moveDown(double distance) {
        moveUp(-distance);
    }

    protected void turnRight(double angle) {
        if (this.location == null)
            return;
        this.location.setYaw((float) ((this.location.getYaw() + angle) % 360));
        Collection<Packet> packets = Arrays.asList(new ServerEntityRotationPacket(this.entityID, this.location.getYaw(), this.location.getPitch(), isOnGround()),
                new ServerEntityHeadLookPacket(this.entityID, this.location.getYaw()));
        long chunk = getChunk();
        for (Player p : MCThunder.getPlayers())
            if (p.getWorld().equals(getWorld()) && p.isColumnLoaded(chunk))
                for (Packet update : packets)
                    p.sendPacket(update);
    }

    protected void turnLeft(double angle) {
        turnRight(-angle);
    }

    protected void lookUp(double angle) {
        if (this.location == null)
            return;
        this.location.setPitch((float) ((this.location.getPitch() + 90 + angle) % 180) - 90);
        Packet update = new ServerEntityRotationPacket(this.entityID, this.location.getYaw(), this.location.getPitch(), isOnGround());
        long chunk = getChunk();
        for (Player p : MCThunder.getPlayers())
            if (p.getWorld().equals(getWorld()) && p.isColumnLoaded(chunk))
                p.sendPacket(update);
    }

    protected void lookDown(double angle) {
        lookUp(-angle);
    }

    public Collection<Packet> getPackets() {
        return Arrays.asList(getPacket(), new ServerEntityMetadataPacket(this.entityID, getMetadata().getMetadataArray()),
                new ServerEntityPositionRotationPacket(this.entityID, 0, 0, 0, this.location.getYaw(), this.location.getPitch(), isOnGround()),
                new ServerEntityHeadLookPacket(this.entityID, this.location.getYaw()));
    }

    private byte getArrows() {
        return this.arrows;
    }

    private void setArrows(byte number) {
        this.arrows = number;
    }

    public Collection<Modifier> getModifiers() {
        return this.modifiers;
    }

    public void addModifier(Modifier m) {
        if (!this.modifiers.contains(m))
            this.modifiers.add(m);
    }

    public void removeModifier(Modifier m) {
        if (this.modifiers.contains(m))
            this.modifiers.remove(m);
    }

    public CompoundTag getNBT() {//TODO: Correct the values that are just 0 to be based on variables
        CompoundTag nbt = super.getNBT();
        nbt.put(new FloatTag("HealF", this.health));
        nbt.put(new FloatTag("AbsorptionAmount", this.activeEffects.containsKey(PotionEffectType.ABSORPTION) ? this.activeEffects.get(PotionEffectType.ABSORPTION).getAmplifier() : 0));
        nbt.put(new ShortTag("AttackTime", (short) 0));
        nbt.put(new ShortTag("HurtTime", (short) 0));
        nbt.put(new IntTag("HurtByTimestamp", 0));
        nbt.put(new ShortTag("DeathTime", (short) 0));
        ArrayList<Tag> temp = new ArrayList<>();
        for (int i = 0; i < 1/*attributes.size()*/; i++) {
            CompoundTag attribute = new CompoundTag("Attribute");
            attribute.put(new StringTag("Name"));
            attribute.put(new DoubleTag("Base"));
            ArrayList<Tag> mods = new ArrayList<>();
            for (Modifier mod : this.modifiers) {
                CompoundTag modifier = new CompoundTag("Modifier");//Is this proper tag name and does it matter
                modifier.put(new StringTag("AttributeName", mod.getAttributeName()));
                modifier.put(new StringTag("Name", mod.getName()));
                modifier.put(new DoubleTag("Amount", mod.getAmount()));
                modifier.put(new IntTag("Operation", mod.getOperation()));
                modifier.put(new LongTag("UUIDMost", mod.getUUIDMost()));
                modifier.put(new LongTag("UUIDLeast", mod.getUUIDLeast()));
                mods.add(modifier);
            }
            if (!mods.isEmpty())
                attribute.put(new ListTag("Modifiers", mods));
            temp.add(attribute);
        }
        if (!temp.isEmpty())
            nbt.put(new ListTag("Attributes", temp));
        temp.clear();
        for (PotionEffect p : this.activeEffects.values()) {
            CompoundTag potion = new CompoundTag("customPotionEffect");//Is this proper tag name and does it matter
            potion.put(new ByteTag("Id", (byte) p.getType().getID()));
            potion.put(new ByteTag("Amplifier", (byte) p.getAmplifier()));
            potion.put(new IntTag("Duration", p.getDuration()));
            potion.put(new ByteTag("Ambient", (byte) (p.isAmbient() ? 1 : 0)));
            potion.put(new ByteTag("ShowParticles", (byte) (p.showParticles() ? 1 : 0)));
            temp.add(potion);
        }
        if (!temp.isEmpty())
            nbt.put(new ListTag("ActiveEffects", temp));

        nbt.put(new ListTag("Equipment", Arrays.<Tag>asList(new CompoundTag("Hand"), new CompoundTag("Boots"), new CompoundTag("Leggings"),
                new CompoundTag("Chestplate"), new CompoundTag("Helmet"))));
        nbt.put(new ListTag("DropChances", Arrays.<Tag>asList(new FloatTag("Hand", 0), new FloatTag("Boots", 0), new FloatTag("Leggings", 0),
                new FloatTag("Chestplate", 0), new FloatTag("Helmet", 0))));
        nbt.put(new ByteTag("CanPickUpLoot", (byte) 0));
        nbt.put(new ByteTag("NoAI", (byte) (this.hasAI ? 0 : 1)));
        nbt.put(new ByteTag("PersistenceRequired", (byte) 0));
        nbt.put(new ByteTag("Leased", (byte) 0));
        CompoundTag leash = new CompoundTag("Leash");
        leash.put(new LongTag("UUIDMost", 0));
        leash.put(new LongTag("UUIDLeast", 0));
        leash.put(new IntTag("X", 0));
        leash.put(new IntTag("Y", 0));
        leash.put(new IntTag("Z", 0));
        nbt.put(leash);
        nbt.put(new ByteTag("CustomNameVisible", (byte) (this.alwaysShowName ? 1 : 0)));
        return nbt;
    }
}