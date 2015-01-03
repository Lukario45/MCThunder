package net.mcthunder.entity;

import net.mcthunder.MCThunder;
import net.mcthunder.api.*;
import net.mcthunder.inventory.ItemStack;
import net.mcthunder.material.Material;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerDestroyEntitiesPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityMetadataPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityTeleportPacket;
import org.spacehq.opennbt.tag.builtin.*;
import org.spacehq.packetlib.packet.Packet;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

public abstract class Entity {
    protected EntityType type;
    protected final int entityID;
    protected String customName;
    protected Location location;
    protected Vector motion;
    protected Entity riding;
    protected short fireTicks, airLeft;
    protected boolean sneaking, sprinting, invisible, onGround;
    protected MetadataMap metadata;

    public static Entity fromType(EntityType t, Location l) {
        switch (t) {
            case ITEM:
                return new DroppedItem(l, new ItemStack(Material.AIR));//Should this be stone so it can be seen
            case XP_ORB:
                return new ExperienceOrb(l);
            case LEAD_KNOT:
                return new LeadKnot(l);
            case PAINTING:
                return new Painting(l);
            case ITEM_FRAME:
                return new ItemFrame(l, new ItemStack(Material.AIR));
            case ARMOR_STAND:
                return new ArmorStand(l);
            case ENDER_CRYSTAL:
                return new EnderCrystal(l);
            case ARROW:
                return new Arrow(l);
            case SNOWBALL:
                return new Snowball(l);
            case GHAST_FIREBALL:
                return new GhastFireball(l);
            case BLAZE_FIREBALL:
                return new BlazeFireball(l);
            case THROWN_ENDER_PEARL:
                return new ThrownEnderPearl(l);
            case THROWN_EYE_OF_ENDER:
                return new ThrownEyeOfEnder(l);
            case THROWN_SPLASH_POTION:
                return new ThrownSplashPotion(l);
            case THROWN_EXP_BOTTLE:
                return new ThrownExpBottle(l);
            case WITHER_SKULL:
                return new WitherSkull(l);
            case FIREWORKS_ROCKET:
                return new Firework(l, new ItemStack(Material.FIREWORKS));
            case PRIMED_TNT:
                return new PrimedTNT(l);
            case FALLING_SAND:
                return new FallingSand(l);
            case MINECART_COMMAND_BLOCK:
                return new CommandBlockMinecart(l);
            case BOAT:
                return new Boat(l);
            case MINECART:
                return new Minecart(l);
            case MINECART_CHEST:
                return new ChestMinecart(l);
            case MINECART_FURNACE:
                return new FurnaceMinecart(l);
            case MINECART_TNT:
                return new TNTMinecart(l);
            case MINECART_HOPPER:
                return new HopperMinecart(l);
            case MINECART_SPAWNER:
                return new SpawnerMinecart(l);
            case CREEPER:
                return new Creeper(l);
            case SKELETON:
                return new Skeleton(l);
            case SPIDER:
                return new Spider(l);
            case GIANT:
                return new Giant(l);
            case ZOMBIE:
                return new Zombie(l);
            case SLIME:
                return new Slime(l);
            case GHAST:
                return new Ghast(l);
            case ZOMBIE_PIGMAN:
                return new ZombiePigman(l);
            case ENDERMAN:
                return new Enderman(l);
            case CAVESPIDER:
                return new CaveSpider(l);
            case SILVERFISH:
                return new Silverfish(l);
            case BLAZE:
                return new Blaze(l);
            case MAGMA_CUBE:
                return new MagmaCube(l);
            case ENDER_DRAGON:
                return new EnderDragon(l);
            case WITHER:
                return new Wither(l);
            case WITCH:
                return new Witch(l);
            case ENDERMITE:
                return new Endermite(l);
            case GUARDIAN:
                return new Guardian(l);
            case BAT:
                return new Bat(l);
            case PIG:
                return new Pig(l);
            case SHEEP:
                return new Sheep(l);
            case COW:
                return new Cow(l);
            case CHICKEN:
                return new Chicken(l);
            case SQUID:
                return new Squid(l);
            case WOLF:
                return new Wolf(l);
            case MOOSHROOM:
                return new Mooshroom(l);
            case SNOW_GOLEM:
                return new SnowGolem(l);
            case OCELOT:
                return new Ocelot(l);
            case IRON_GOLEM:
                return new IronGolem(l);
            case HORSE:
                return new Horse(l);
            case RABBIT:
                return new Rabbit(l);
            case VILLAGER:
                return new Villager(l);
            case MOB: case MONSTER: case PLAYER://Cannot be created
                break;
        }
        return null;
    }

    /*public static Entity fromType(EntityType t, CompoundTag tag) {//TODO: Implement the creation of entities by tag and setting metadata from tag
        switch (t) {
            case ITEM:
                return new DroppedItem(tag);
            case XP_ORB:
                return new ExperienceOrb(tag);
            case LEAD_KNOT:
                return new LeadKnot(tag);
            case PAINTING:
                return new Painting(tag);
            case ITEM_FRAME:
                return new ItemFrame(tag);
            case ARMOR_STAND:
                return new ArmorStand(tag);
            case ENDER_CRYSTAL:
                return new EnderCrystal(tag);
            case ARROW:
                return new Arrow(tag);
            case SNOWBALL:
                return new Snowball(tag);
            case GHAST_FIREBALL:
                return new GhastFireball(tag);
            case BLAZE_FIREBALL:
                return new BlazeFireball(tag);
            case THROWN_ENDER_PEARL:
                return new ThrownEnderPearl(tag);
            case THROWN_EYE_OF_ENDER:
                return new ThrownEyeOfEnder(tag);
            case THROWN_SPLASH_POTION:
                return new ThrownSplashPotion(tag);
            case THROWN_EXP_BOTTLE:
                return new ThrownExpBottle(tag);
            case WITHER_SKULL:
                return new WitherSkull(tag);
            case FIREWORKS_ROCKET:
                return new Firework(tag);
            case PRIMED_TNT:
                return new PrimedTNT(tag);
            case FALLING_SAND:
                return new FallingSand(tag);
            case MINECART_COMMAND_BLOCK:
                return new CommandBlockMinecart(tag);
            case BOAT:
                return new Boat(tag);
            case MINECART:
                return new Minecart(tag);
            case MINECART_CHEST:
                return new ChestMinecart(tag);
            case MINECART_FURNACE:
                return new FurnaceMinecart(tag);
            case MINECART_TNT:
                return new TNTMinecart(tag);
            case MINECART_HOPPER:
                return new HopperMinecart(tag);
            case MINECART_SPAWNER:
                return new SpawnerMinecart(tag);
            case CREEPER:
                return new Creeper(tag);
            case SKELETON:
                return new Skeleton(tag);
            case SPIDER:
                return new Spider(tag);
            case GIANT:
                return new Giant(tag);
            case ZOMBIE:
                return new Zombie(tag);
            case SLIME:
                return new Slime(tag);
            case GHAST:
                return new Ghast(tag);
            case ZOMBIE_PIGMAN:
                return new ZombiePigman(tag);
            case ENDERMAN:
                return new Enderman(tag);
            case CAVESPIDER:
                return new CaveSpider(tag);
            case SILVERFISH:
                return new Silverfish(tag);
            case BLAZE:
                return new Blaze(tag);
            case MAGMA_CUBE:
                return new MagmaCube(tag);
            case ENDER_DRAGON:
                return new EnderDragon(tag);
            case WITHER:
                return new Wither(tag);
            case WITCH:
                return new Witch(tag);
            case ENDERMITE:
                return new Endermite(tag);
            case GUARDIAN:
                return new Guardian(tag);
            case BAT:
                return new Bat(tag);
            case PIG:
                return new Pig(tag);
            case SHEEP:
                return new Sheep(tag);
            case COW:
                return new Cow(tag);
            case CHICKEN:
                return new Chicken(tag);
            case SQUID:
                return new Squid(tag);
            case WOLF:
                return new Wolf(tag);
            case MOOSHROOM:
                return new Mooshroom(tag);
            case SNOW_GOLEM:
                return new SnowGolem(tag);
            case OCELOT:
                return new Ocelot(tag);
            case IRON_GOLEM:
                return new IronGolem(tag);
            case HORSE:
                return new Horse(tag);
            case RABBIT:
                return new Rabbit(tag);
            case VILLAGER:
                return new Villager(tag);
            case MOB: case MONSTER: case PLAYER://Cannot be created
                break;
        }
        return null;
    }*/

    protected Entity(Location location) {
        this.entityID = (int) Math.ceil(Math.random() * Integer.MAX_VALUE);
        this.location = location;
        this.riding = null;
        if (this.location != null)
            this.location.setVector(this.motion = new Vector(0, 0, 0));
        this.customName = "";
        this.metadata = new MetadataMap();
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.ON_FIRE, this.fireTicks > 0);
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.SNEAKING, this.sneaking = false);
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.SPRINTING, this.sprinting = false);
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.ARM_UP, false);//Eating, drinking, blocking
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.INVISIBLE, this.invisible = false);
        this.metadata.setMetadata(1, this.airLeft = 0);
    }

    protected Entity(World w, CompoundTag tag) {
        this.entityID = (int) Math.ceil(Math.random() * Integer.MAX_VALUE);
        StringTag id = tag.get("id");
        EntityType type = id == null ? null : EntityType.fromSavegameId(id.getValue());
        if (type == null)
            return;
        ListTag pos = tag.get("Pos");
        if (pos != null) {
            DoubleTag posX = pos.get(0);
            DoubleTag posY = pos.get(1);
            DoubleTag posZ = pos.get(2);
            this.location = new Location(w, posX.getValue(), posY.getValue(), posZ.getValue());
        }
        ListTag rotation = tag.get("Rotation");
        if (rotation != null && this.location != null) {
            FloatTag yaw = rotation.get(0);
            FloatTag pitch = rotation.get(1);
            this.location.setYaw(yaw.getValue());
            this.location.setPitch(pitch.getValue());
        }
        ListTag motion = tag.get("Motion");
        if (motion != null && this.location != null) {
            DoubleTag dX = motion.get(0);
            DoubleTag dY = motion.get(1);
            DoubleTag dZ = motion.get(2);
            this.location.setVector(this.motion = new Vector(dX.getValue(), dY.getValue(), dZ.getValue()));
        }
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.ON_FIRE, (this.fireTicks = ((ShortTag) tag.get("Fire")).getValue()) > 0);
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.SNEAKING, this.sneaking = false);
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.SPRINTING, this.sprinting = false);
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.ARM_UP, false);//Eating, drinking, blocking
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.INVISIBLE, this.invisible = false);
        this.metadata.setMetadata(1, this.airLeft = ((ShortTag) tag.get("Air")).getValue());
        ByteTag onGround = tag.get("OnGround");//1 true, 0 false
        getWorld().setDimension(((IntTag) tag.get("Dimension")).getValue());//-1 nether, 0 overworld, 1 end
        ByteTag invulnerable = tag.get("Invulnerable");//1 true, 0 false
        IntTag portalCooldown = tag.get("PortalCooldown");
        LongTag uuidMost = tag.get("UUIDMost");
        LongTag uuidLeast = tag.get("UUIDLeast");
        UUID uuid = tag.get("UUID") != null ? UUID.fromString(((StringTag) tag.get("UUID")).getValue()) : null;
        StringTag customName = tag.get("CustomName");
        this.customName = customName != null ? customName.getValue() : "";
        ByteTag customNameVisible = tag.get("CustomNameVisible");//1 true, 0 false
        ByteTag silent = tag.get("Silent");//1 true, 0 false
        CompoundTag riding = tag.get("Riding");
        CompoundTag commandStats = tag.get("CommandStats");
        StringTag successCountName = commandStats != null ? (StringTag) commandStats.get("SuccessCountName") : null;
        StringTag successCountObjective = commandStats != null ? (StringTag) commandStats.get("SuccessCountObjective") : null;
        StringTag affectedBlocksName = commandStats != null ? (StringTag) commandStats.get("AffectedBlocksName") : null;
        StringTag affectedBlocksObjective = commandStats != null ? (StringTag) commandStats.get("AffectedBlocksObjective") : null;
        StringTag affectedEntitiesName = commandStats != null ? (StringTag) commandStats.get("AffectedEntitiesName") : null;
        StringTag affectedEntitiesObjective = commandStats != null ? (StringTag) commandStats.get("AffectedEntitiesObjective") : null;
        StringTag affectedItemsName = commandStats != null ? (StringTag) commandStats.get("AffectedItemsName") : null;
        StringTag affectedItemsObjective = commandStats != null ? (StringTag) commandStats.get("AffectedItemsObjective") : null;
        StringTag queryResultName = commandStats != null ? (StringTag) commandStats.get("QueryResultName") : null;
        StringTag queryResultObjective = commandStats != null ? (StringTag) commandStats.get("QueryResultObjective") : null;
    }

    public long getChunk() {
        return this.location == null ? 0 : Utils.getLong((int) this.location.getX() >> 4, (int) this.location.getZ() >> 4);
    }

    public MetadataMap getMetadata() {//Should we clone the return
        return this.metadata;
    }

    public int getEntityID() {
        return this.entityID;
    }

    public abstract Packet getPacket();

    public Collection<Packet> getPackets() {
        return Arrays.asList(getPacket(), new ServerEntityMetadataPacket(this.entityID, getMetadata().getMetadataArray()));
    }

    public Location getLocation() {
        return this.location.clone();
    }

    public void setLocation(Location location) {
        this.location = location.clone();
    }

    public void setX(double x) {
        this.location.setX(x);
    }

    public void setY(double y) {
        this.location.setY(y);
    }

    public void setZ(double z) {
        this.location.setZ(z);
    }

    public void setYaw(float yaw) {
        this.location.setYaw(yaw);
    }

    public void setPitch(float pitch) {
        this.location.setPitch(pitch);
    }

    public World getWorld() {
        return this.location.getWorld();
    }

    public void setWorld(World w) {
        this.location.setWorld(w);
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public void teleport(Location l) {
        long chunk = getChunk();
        if (!l.getWorld().equals(getWorld())) {
            ServerDestroyEntitiesPacket destroyEntitiesPacket = new ServerDestroyEntitiesPacket(getEntityID());
            Packet respawn = getPacket();
            if (respawn != null)
                for (Player p : MCThunder.getPlayers()) {
                    p.sendPacket(destroyEntitiesPacket);
                    if (p.getWorld().equals(l.getWorld()) && p.isColumnLoaded(chunk))
                        p.sendPacket(respawn);
                }
        } else {
            ServerEntityTeleportPacket packet = new ServerEntityTeleportPacket(getEntityID(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch(), isOnGround());
            for (Player p : MCThunder.getPlayers())
                if (p.getWorld().equals(l.getWorld()) && p.isColumnLoaded(chunk))
                    p.sendPacket(packet);
        }
        setLocation(l);
    }

    public boolean isSprinting() {
        return this.sprinting;
    }

    public void setSprinting(boolean sprinting) {
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.SPRINTING, this.sprinting = sprinting);
        updateMetadata();
    }

    public boolean isSneaking() {
        return this.sneaking;
    }

    public void setSneaking(boolean sneaking) {
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.SNEAKING, this.sneaking = sneaking);
        updateMetadata();
    }

    public void setAirLeft(short air) {
        this.metadata.setMetadata(1, this.airLeft = air);
        updateMetadata();
    }

    protected void updateMetadata() {
        MCThunder.getMetadataChangeEventSource().fireEvent(this);
    }
}