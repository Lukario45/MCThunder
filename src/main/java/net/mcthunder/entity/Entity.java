package net.mcthunder.entity;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Location;
import net.mcthunder.api.MetadataConstants;
import net.mcthunder.api.MetadataMap;
import net.mcthunder.api.Utils;
import net.mcthunder.inventory.ItemStack;
import net.mcthunder.material.Material;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerDestroyEntitiesPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityMetadataPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityTeleportPacket;
import org.spacehq.packetlib.packet.Packet;

import java.util.Arrays;
import java.util.Collection;

public abstract class Entity {
    protected EntityType type;
    protected final int entityID;
    protected String customName;
    protected Location location;
    protected boolean onGround;
    protected short fireTicks, airLeft;
    protected boolean sneaking, sprinting, invisible;
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

    protected Entity(Location location) {
        this.location = location;
        this.sneaking = false;
        this.sprinting = false;
        this.invisible = false;
        this.customName = "";
        this.entityID = (int) Math.ceil(Math.random() * Integer.MAX_VALUE);
        this.metadata = new MetadataMap();
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.ON_FIRE, this.fireTicks > 0);
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.SNEAKING, this.sneaking);
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.SPRINTING, this.sprinting);
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.ARM_UP, false);//Eating, drinking, blocking
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.INVISIBLE, this.invisible);
        this.metadata.setMetadata(1, this.airLeft);
    }

    public void spawn() {//TODO: replace this to send from world
        long chunk = getChunk();
        for (Player p : MCThunder.getPlayers())
            if (p.getWorld().equals(this.location.getWorld()) && p.isColumnLoaded(chunk))
                for (Packet packet : getPackets())
                    if (packet != null)
                        p.sendPacket(packet);
    }

    public long getChunk() {
        if (this.location == null)
            return 0;
        return Utils.getLong((int) this.location.getX() >> 4, (int) this.location.getZ() >> 4);
    }

    public MetadataMap getMetadata() {
        return this.metadata;
    }

    public int getEntityID() {
        return this.entityID;
    }

    public abstract Packet getPacket();

    public Collection<Packet> getPackets() {
        return Arrays.asList(getPacket(), new ServerEntityMetadataPacket(this.entityID, this.getMetadata().getMetadataArray()));
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
            //refreshEntityID();//Should not be needed when they change world to one people have not been in
            Packet respawn = getPacket();
            if (respawn != null)
                for (Player p : MCThunder.getPlayers()) {
                    p.sendPacket(destroyEntitiesPacket);
                    if (p.getWorld().equals(l.getWorld()) && p.isColumnLoaded(chunk))//If they are in the new world
                        p.sendPacket(respawn);
                }
        } else {
            ServerEntityTeleportPacket packet = new ServerEntityTeleportPacket(getEntityID(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch(), this.onGround);
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
        this.sprinting = sprinting;
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.SPRINTING, this.sprinting);
    }

    public boolean isSneaking() {
        return this.sneaking;
    }

    public void setSneaking(boolean sneaking) {
        this.sneaking = sneaking;
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.SNEAKING, this.sneaking);
    }
}