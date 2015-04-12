package net.mcthunder.entity;

import net.mcthunder.MCThunder;
import net.mcthunder.api.*;
import net.mcthunder.inventory.ItemStack;
import net.mcthunder.block.Material;
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
    private static int nextID = 0;
    protected boolean sneaking = false, sprinting = false, invisible = false, onGround = false;
    protected MetadataMap metadata = new MetadataMap();
    protected short fireTicks = -20, airLeft = 0;
    protected String customName = "";
    protected Entity riding = null;
    protected Location location;
    protected EntityType type;
    protected int entityID;
    private boolean isLiving;
    /*TODO: Finish storing and returning the information from the tags for the following entity types
    ArmorStand
    Arrow
    Entity
    Horse
    LivingEntity
    Player
    Projectile
    SpawnerMinecart
    Villager*/

    protected Entity(Location location) {
        this.entityID = getNextID();
        this.location = location;
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.ON_FIRE, this.fireTicks >= 0);
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.SNEAKING, this.sneaking);
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.SPRINTING, this.sprinting);
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.ARM_UP, false);//Eating, drinking, blocking
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.INVISIBLE, this.invisible);
        this.metadata.setMetadata(1, this.airLeft);
    }

    public EntityType getType(){
        return type;
    }

    protected Entity(World w, CompoundTag tag) {
        this.entityID = getNextID();
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
            this.location.setVector(new Vector(dX.getValue(), dY.getValue(), dZ.getValue()));
        }
        ShortTag fire = tag.get("Fire");
        if (fire != null)
            this.fireTicks = fire.getValue();
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.ON_FIRE, this.fireTicks >= 0);
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.SNEAKING, this.sneaking);
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.SPRINTING, this.sprinting);
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.ARM_UP, false);//Eating, drinking, blocking
        this.metadata.setBit(MetadataConstants.STATUS, MetadataConstants.StatusFlags.INVISIBLE, this.invisible);
        this.metadata.setMetadata(1, this.airLeft = ((ShortTag) tag.get("Air")).getValue());
        ByteTag onGround = tag.get("OnGround");//1 true, 0 false
        this.onGround = onGround != null && onGround.getValue() == (byte) 1;
        IntTag dim = tag.get("Dimension");
        if (dim != null)
            getWorld().setDimension(dim.getValue());//-1 nether, 0 overworld, 1 end
        ByteTag invulnerable = tag.get("Invulnerable");//1 true, 0 false
        IntTag portalCooldown = tag.get("PortalCooldown");
        LongTag uuidMost = tag.get("UUIDMost");
        LongTag uuidLeast = tag.get("UUIDLeast");
        UUID uuid = tag.get("UUID") != null ? UUID.fromString(((StringTag) tag.get("UUID")).getValue()) : null;
        StringTag customName = tag.get("CustomName");
        this.customName = customName != null ? customName.getValue() : "";
        ByteTag silent = tag.get("Silent");//1 true, 0 false
        CompoundTag riding = tag.get("Riding");
        if (riding != null)
            this.riding = Entity.fromTag(getWorld(), riding);
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

    public static int getNextID() {
        return nextID++;//Returns it and adds 1 incase they use it
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

    public Vector getMotion() {
        return this.location.getVector();
    }

    protected void updateMetadata() {
        MCThunder.getMetadataChangeEventSource().fireEvent(this);
    }

    public CompoundTag getNBT() {//TODO: Return the nbt as well as have all subclasses do as well
        //TODO: Correct the values that are just 0 to be based on variables
        CompoundTag nbt = new CompoundTag("");
        nbt.put(new StringTag("id", this.type.getSavegameID()));
        nbt.put(new ListTag("Pos", Arrays.<Tag>asList(new DoubleTag("X", this.location.getX()),
                new DoubleTag("Y", this.location.getY()), new DoubleTag("Z", this.location.getZ()))));
        nbt.put(new ListTag("Rotation", Arrays.<Tag>asList(new FloatTag("Yaw", this.location.getYaw()),
                new FloatTag("Pitch", this.location.getPitch()))));
        nbt.put(new ListTag("Motion", Arrays.<Tag>asList(new DoubleTag("dX", this.location.getVector().getdX()),
                new DoubleTag("dY", this.location.getVector().getdY()), new DoubleTag("dZ", this.location.getVector().getdZ()))));
        nbt.put(new ShortTag("Fire", this.fireTicks));
        nbt.put(new ByteTag("OnGround", (byte) (this.onGround ? 1 : 0)));
        nbt.put(new IntTag("Dimension", getWorld().getDimension()));
        nbt.put(new ByteTag("Invulnerable", (byte) 0));
        nbt.put(new IntTag("PortalCooldown", 0));
        nbt.put(new LongTag("UUIDMost", 0));
        nbt.put(new LongTag("UUIDLeast", 0));
        nbt.put(new StringTag("UUID", UUID.randomUUID().toString()));//unset at the moment
        if (this.customName != null && !this.customName.equals(""))
            nbt.put(new StringTag("CustomName", this.customName));
        nbt.put(new ByteTag("Silent", (byte) 0));
        if (this.riding != null)
            nbt.put(new CompoundTag("Riding", this.riding.getNBT().getValue()));
        CompoundTag commandStats = new CompoundTag("CommandStats");
        commandStats.put(new StringTag("SuccessCountName"));
        commandStats.put(new StringTag("SuccessCountObjective"));
        commandStats.put(new StringTag("AffectedBlocksName"));
        commandStats.put(new StringTag("AffectedBlocksObjective"));
        commandStats.put(new StringTag("AffectedEntitiesName"));
        commandStats.put(new StringTag("AffectedEntitiesObjective"));
        commandStats.put(new StringTag("AffectedItemsName"));
        commandStats.put(new StringTag("AffectedItemsObjective"));
        commandStats.put(new StringTag("QueryResultName"));
        commandStats.put(new StringTag("QueryResultObjective"));
        //nbt.put(commandStats);
        return nbt;
    }

    public static Entity fromType(EntityType t, Location l) {
        if (t == null)
            return null;
        switch (t) {
            case ITEM: return new DroppedItem(l, new ItemStack(Material.STONE));
            case XP_ORB: return new ExperienceOrb(l);
            case LEAD_KNOT: return new LeadKnot(l);
            case PAINTING: return new Painting(l);
            case ITEM_FRAME: return new ItemFrame(l, new ItemStack(Material.AIR));
            case ARMOR_STAND: return new ArmorStand(l);
            case ENDER_CRYSTAL: return new EnderCrystal(l);
            case ARROW: return new Arrow(l);
            case SNOWBALL: return new Snowball(l);
            case GHAST_FIREBALL: return new GhastFireball(l);
            case BLAZE_FIREBALL: return new BlazeFireball(l);
            case THROWN_ENDER_PEARL: return new ThrownEnderPearl(l);
            case THROWN_EYE_OF_ENDER: return new ThrownEyeOfEnder(l);
            case THROWN_SPLASH_POTION: return new ThrownSplashPotion(l);
            case THROWN_EXP_BOTTLE: return new ThrownExpBottle(l);
            case WITHER_SKULL: return new WitherSkull(l);
            case FIREWORKS_ROCKET: return new Firework(l, new ItemStack(Material.FIREWORKS));
            case PRIMED_TNT: return new PrimedTNT(l);
            case FALLING_SAND: return new FallingSand(l);
            case MINECART_COMMAND_BLOCK: return new CommandBlockMinecart(l);
            case BOAT: return new Boat(l);
            case MINECART: return new Minecart(l);
            case MINECART_CHEST: return new ChestMinecart(l);
            case MINECART_FURNACE: return new FurnaceMinecart(l);
            case MINECART_TNT: return new TNTMinecart(l);
            case MINECART_HOPPER: return new HopperMinecart(l);
            case MINECART_SPAWNER: return new SpawnerMinecart(l);
            case CREEPER: return new Creeper(l);
            case SKELETON: return new Skeleton(l);
            case SPIDER: return new Spider(l);
            case GIANT: return new Giant(l);
            case ZOMBIE: return new Zombie(l);
            case SLIME: return new Slime(l);
            case GHAST: return new Ghast(l);
            case ZOMBIE_PIGMAN: return new ZombiePigman(l);
            case ENDERMAN: return new Enderman(l);
            case CAVESPIDER: return new CaveSpider(l);
            case SILVERFISH: return new Silverfish(l);
            case BLAZE: return new Blaze(l);
            case MAGMA_CUBE: return new MagmaCube(l);
            case ENDER_DRAGON: return new EnderDragon(l);
            case WITHER: return new Wither(l);
            case WITCH: return new Witch(l);
            case ENDERMITE: return new Endermite(l);
            case GUARDIAN: return new Guardian(l);
            case BAT: return new Bat(l);
            case PIG: return new Pig(l);
            case SHEEP: return new Sheep(l);
            case COW: return new Cow(l);
            case CHICKEN: return new Chicken(l);
            case SQUID: return new Squid(l);
            case WOLF: return new Wolf(l);
            case MOOSHROOM: return new Mooshroom(l);
            case SNOW_GOLEM: return new SnowGolem(l);
            case OCELOT: return new Ocelot(l);
            case IRON_GOLEM: return new IronGolem(l);
            case HORSE: return new Horse(l);
            case RABBIT: return new Rabbit(l);
            case VILLAGER: return new Villager(l);
            case MOB: case MONSTER: case PLAYER://Cannot be created
                break;
        }
        return null;
    }

    public static Entity fromTag(World w, CompoundTag tag) {
        StringTag id = tag.get("id");
        EntityType t = id == null ? null : EntityType.fromSavegameId(id.getValue());
        if (t == null)
            return null;
        switch (t) {
            case ITEM: return new DroppedItem(w, tag);
            case XP_ORB: return new ExperienceOrb(w, tag);
            case LEAD_KNOT: return new LeadKnot(w, tag);
            case PAINTING: return new Painting(w, tag);
            case ITEM_FRAME: return new ItemFrame(w, tag);
            case ARMOR_STAND: return new ArmorStand(w, tag);
            case ENDER_CRYSTAL: return new EnderCrystal(w, tag);
            case ARROW: return new Arrow(w, tag);
            case SNOWBALL: return new Snowball(w, tag);
            case GHAST_FIREBALL: return new GhastFireball(w, tag);
            case BLAZE_FIREBALL: return new BlazeFireball(w, tag);
            case THROWN_ENDER_PEARL: return new ThrownEnderPearl(w, tag);
            case THROWN_EYE_OF_ENDER: return new ThrownEyeOfEnder(w, tag);
            case THROWN_SPLASH_POTION: return new ThrownSplashPotion(w, tag);
            case THROWN_EXP_BOTTLE: return new ThrownExpBottle(w, tag);
            case WITHER_SKULL: return new WitherSkull(w, tag);
            case FIREWORKS_ROCKET: return new Firework(w, tag);
            case PRIMED_TNT: return new PrimedTNT(w, tag);
            case FALLING_SAND: return new FallingSand(w, tag);
            case MINECART_COMMAND_BLOCK: return new CommandBlockMinecart(w, tag);
            case BOAT: return new Boat(w, tag);
            case MINECART: return new Minecart(w, tag);
            case MINECART_CHEST: return new ChestMinecart(w, tag);
            case MINECART_FURNACE: return new FurnaceMinecart(w, tag);
            case MINECART_TNT: return new TNTMinecart(w, tag);
            case MINECART_HOPPER: return new HopperMinecart(w, tag);
            case MINECART_SPAWNER: return new SpawnerMinecart(w, tag);
            case CREEPER: return new Creeper(w, tag);
            case SKELETON: return new Skeleton(w, tag);
            case SPIDER: return new Spider(w, tag);
            case GIANT: return new Giant(w, tag);
            case ZOMBIE: return new Zombie(w, tag);
            case SLIME: return new Slime(w, tag);
            case GHAST: return new Ghast(w, tag);
            case ZOMBIE_PIGMAN: return new ZombiePigman(w, tag);
            case ENDERMAN: return new Enderman(w, tag);
            case CAVESPIDER: return new CaveSpider(w, tag);
            case SILVERFISH: return new Silverfish(w, tag);
            case BLAZE: return new Blaze(w, tag);
            case MAGMA_CUBE: return new MagmaCube(w, tag);
            case ENDER_DRAGON: return new EnderDragon(w, tag);
            case WITHER: return new Wither(w, tag);
            case WITCH: return new Witch(w, tag);
            case ENDERMITE: return new Endermite(w, tag);
            case GUARDIAN: return new Guardian(w, tag);
            case BAT: return new Bat(w, tag);
            case PIG: return new Pig(w, tag);
            case SHEEP: return new Sheep(w, tag);
            case COW: return new Cow(w, tag);
            case CHICKEN: return new Chicken(w, tag);
            case SQUID: return new Squid(w, tag);
            case WOLF: return new Wolf(w, tag);
            case MOOSHROOM: return new Mooshroom(w, tag);
            case SNOW_GOLEM: return new SnowGolem(w, tag);
            case OCELOT: return new Ocelot(w, tag);
            case IRON_GOLEM: return new IronGolem(w, tag);
            case HORSE: return new Horse(w, tag);
            case RABBIT: return new Rabbit(w, tag);
            case VILLAGER: return new Villager(w, tag);
            case MOB: case MONSTER: case PLAYER://Cannot be created
                break;
        }
        return null;
    }
}