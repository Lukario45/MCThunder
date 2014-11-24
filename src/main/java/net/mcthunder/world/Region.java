package net.mcthunder.world;

import net.mcthunder.api.Direction;
import net.mcthunder.api.Location;
import net.mcthunder.api.Vector;
import net.mcthunder.block.Sign;
import net.mcthunder.entity.Entity;
import net.mcthunder.entity.EntityType;
import net.mcthunder.entity.Player;
import org.spacehq.mc.protocol.data.game.Chunk;
import org.spacehq.mc.protocol.data.game.NibbleArray3d;
import org.spacehq.mc.protocol.data.game.ShortArray3d;
import org.spacehq.opennbt.NBTIO;
import org.spacehq.opennbt.tag.builtin.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Kevin on 10/21/2014.
 */
public class Region {
    private int x;
    private int z;
    private World world;
    private RegionFile regionFile = null;
    private boolean invalid;

    public Region(World w, long region) {
        this.x = (int) (region >> 32);
        this.z = (int) region;
        this.world = w;
        File f = new File("worlds/" + this.world.getName() + "/region/r." + this.x + "." + this.z + ".mca");
        this.invalid = !f.exists();
        if (this.invalid) {
            //Create the region file
            //Once this if statement actually does things the invalid boolean can be removed
        }
        this.regionFile = new RegionFile(f);
    }

    public void saveChunk(long l) {
        if (this.invalid)
            return;
        int x = (int) (l >> 32);
        int z = (int) l;
        while (x < 0)
            x += 32;
        while (x > 32)
            x -= 32;
        while (z < 0)
            z += 32;
        while (z > 32)
            z -= 32;
        if (x > 32 || z > 32 || x < 0 || z < 0)
            return;
        DataOutputStream out = this.regionFile.getChunkDataOutputStream(x, z);
        DataInputStream in = this.regionFile.getChunkDataInputStream(x, z);
        Tag tag = null;
        if (in != null && out != null) {
            try {
                tag = NBTIO.readTag(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            CompoundTag compoundTag = (CompoundTag) tag;
            if (compoundTag == null)
                return;
            CompoundTag level = compoundTag.get("Level");
            ListTag sections = level.get("Sections");
            Column c = this.world.getColumn(l);
            Chunk[] chunks = c.getChunks();
            Map<String,Tag> values = compoundTag.getValue();
            Map<String,Tag> levelInfo = level.getValue();
            ArrayList<Tag> newSections = new ArrayList<>();
            for (int i = 0; i < sections.size(); i++) {//Loop through all 16 chunks in a verticle fashion
                CompoundTag chunkz = sections.get(i);
                Map<String,Tag> cv = chunkz.getValue();
                ByteArrayTag blocks = chunkz.get("Blocks");
                ByteArrayTag blockLight = chunkz.get("BlockLight");
                ByteArrayTag skyLight = chunkz.get("SkyLight");
                ByteArrayTag data = chunkz.get("Data");
                //ByteArrayTag add = chunkz.get("Add");
                if (chunks[i] != null) {
                    if (chunks[i].getBlockLight() != null)
                        blockLight.setValue(chunks[i].getBlockLight().getData());
                    if (chunks[i].getSkyLight() != null)
                        skyLight.setValue(chunks[i].getSkyLight().getData());
                    if(chunks[i].getBlocks() != null)
                        for (int cY = 0; cY < 16; cY++) //Loop through the Y axis
                            for (int cZ = 0; cZ < 16; cZ++) //Loop through z
                                for (int cX = 0; cX < 16; cX++) { //Loop through x
                                    int index = 256*cY + 16*cZ + cX;
                                    blocks.setValue(index, Byte.parseByte(chunks[i].getBlocks().getBlock(cX, cY, cZ) + ""));
                                    if(index%2 == 0)
                                        data.setValue(index/2, (byte) (chunks[i].getBlocks().getData(cX, cY, cZ) << 4 | getValue(data, index + 1)));
                                    else
                                        data.setValue(index/2, (byte) (getValue(data, index - 1) << 4 | chunks[i].getBlocks().getData(cX, cY, cZ)));
                                }
                }
                cv.put("Blocks", blocks);
                cv.put("BlockLight", blockLight);
                cv.put("SkyLight", skyLight);
                cv.put("Data", data);
                //cv.put("Add", add);//How to calculate from value if even needed
                chunkz.setValue(cv);
                newSections.add(chunkz);
            }
            sections.setValue(newSections);
            levelInfo.put("Sections", sections);
            level.setValue(levelInfo);
            values.put("Level", level);
            compoundTag.setValue(values);
            try {
                NBTIO.writeTag(out, compoundTag);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void readChunk(long l) {
        if (this.invalid || this.world.isColumnLoaded(l))
            return;
        int x = (int) (l >> 32);
        int z = (int) l;
        while (x < 0)
            x += 32;
        while (x > 32)
            x -= 32;
        while (z < 0)
            z += 32;
        while (z > 32)
            z -= 32;
        if (x > 32 || z > 32 || x < 0 || z < 0)
            return;
        DataInputStream in = this.regionFile.getChunkDataInputStream(x, z);
        if (in == null) {//Chunk needs to be created or is corrupted and should be regenerated

        } else {
            Tag tag = null;
            try {
                tag = NBTIO.readTag(in);
            } catch (IOException e) {
                e.printStackTrace();
            }
            CompoundTag compoundTag = (CompoundTag) tag;
            if (compoundTag == null)
                return;
            CompoundTag level = compoundTag.get("Level");
            ListTag sections = level.get("Sections");
            ByteArrayTag biomes = level.get("Biomes");
            ListTag entities = level.get("Entities");
            for (int i = 0; i < entities.size(); i++) {//TODO: cleanup reading and spawning
                CompoundTag entity = entities.get(i);
                StringTag id = entity.get("id");
                EntityType type = id == null ? null : EntityType.fromSavegameId(id.getValue());
                if (type == null)
                    continue;
                Entity e;
                Location loc = null;
                Vector vec = null;
                ListTag pos = entity.get("Pos");
                if (pos != null) {
                    DoubleTag posX = pos.get(0);
                    DoubleTag posY = pos.get(1);
                    DoubleTag posZ = pos.get(2);
                    loc = new Location(this.world, posX.getValue(), posY.getValue(), posZ.getValue());
                }
                ListTag rotation = entity.get("Rotation");
                if (rotation != null && loc != null) {
                    FloatTag yaw = rotation.get(0);
                    FloatTag pitch = rotation.get(1);
                    loc.setYaw(yaw.getValue());
                    loc.setPitch(pitch.getValue());
                }
                ListTag motion = entity.get("Motion");
                if (motion != null && loc != null) {
                    DoubleTag dX = motion.get(0);
                    DoubleTag dY = motion.get(1);
                    DoubleTag dZ = motion.get(2);
                    loc.setVector(new Vector(dX.getValue(), dY.getValue(), dZ.getValue()));
                }
                ShortTag fire = entity.get("Fire");
                ShortTag air = entity.get("Air");
                ByteTag onGround = entity.get("OnGround");//1 true, 0 false
                IntTag dimension = entity.get("Dimension");//-1 nether, 0 overworld, 1 end
                this.world.setDimension(dimension.getValue());
                ByteTag invulnerable = entity.get("Invulnerable");//1 true, 0 false
                IntTag portalCooldown = entity.get("PortalCooldown");
                LongTag uuidMost = entity.get("UUIDMost");
                LongTag uuidLeast = entity.get("UUIDLeast");
                UUID uuid = entity.get("UUID") != null ? UUID.fromString(((StringTag) entity.get("UUID")).getValue()) : null;
                StringTag customName = entity.get("CustomName");
                ByteTag customNameVisible = entity.get("CustomNameVisible");//1 true, 0 false
                ByteTag silent = entity.get("Silent");//1 true, 0 false
                CompoundTag riding = entity.get("Riding");
                CompoundTag commandStats = entity.get("CommandStats");
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
                if (type == null || type.isCreature()) {
                    FloatTag healF = entity.get("HealF");
                    ShortTag health = entity.get("Health");
                    FloatTag absorptionAmount = entity.get("AbsorptionAmount");
                    ShortTag attackTime = entity.get("AttackTime");
                    ShortTag hurtTime = entity.get("HurtTime");
                    IntTag hurtByTimestamp = entity.get("HurtByTimestamp");
                    ShortTag deathTime = entity.get("DeathTime");
                    ListTag attributes = entity.get("Attributes");
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
                                }
                        }
                    ListTag activeEffects = entity.get("ActiveEffects");
                    if (activeEffects != null)
                        for (int j = 0; j < activeEffects.size(); j++) {
                            CompoundTag activeEffect = activeEffects.get(j);
                            ByteTag effectID = activeEffect.get("Id");
                            ByteTag amplifier = activeEffect.get("Amplifier");
                            IntTag duration = activeEffect.get("Duration");
                            ByteTag ambient = activeEffect.get("Ambient");//1 true, 0 false
                            ByteTag showParticles = activeEffect.get("ShowParticles");//1 true, 0 false
                        }
                    ListTag equipment = entity.get("Equipment");
                    if (equipment != null) {
                        CompoundTag hand = equipment.get(0);
                        CompoundTag boots = equipment.get(1);
                        CompoundTag leggings = equipment.get(2);
                        CompoundTag chestplate = equipment.get(3);
                        CompoundTag helmet = equipment.get(4);
                    }
                    ListTag dropChances = entity.get("DropChances");
                    if (dropChances != null) {
                        FloatTag hand = dropChances.get(0);
                        FloatTag boots = dropChances.get(1);
                        FloatTag leggings = dropChances.get(2);
                        FloatTag chestplate = dropChances.get(3);
                        FloatTag helmet = dropChances.get(4);
                    }
                    ByteTag canPickUpLoot = entity.get("CanPickUpLoot");//1 true, 0 false
                    ByteTag noAI = entity.get("NoAI");//1 true, 0 false
                    ByteTag persistenceRequired = entity.get("PersistenceRequired");//1 true, 0 false
                    ByteTag leashed = entity.get("Leashed");//1 true, 0 false
                    CompoundTag leash = entity.get("Leash");
                    if (leash != null) {
                        LongTag leashUUIDMost = leash.get("UUIDMost");
                        LongTag leashUUIDLeast = leash.get("UUIDLeast");
                        IntTag leashX = leash.get("X");
                        IntTag leashY = leash.get("Y");
                        IntTag leashZ = leash.get("Z");
                    }
                    //Mobs that can breed will be null if cannot breed
                    IntTag inLove = entity.get("InLove");
                    IntTag age = entity.get("Age");
                    IntTag forcedAge = entity.get("ForcedAge");
                    StringTag owner = entity.get("Owner");//Legacy support for pre 1.8
                    UUID ownerUUID = entity.get("OwnerUUID") != null ? UUID.fromString(((StringTag) entity.get("OwnerUUID")).getValue()) : null;
                    ByteTag sitting = entity.get("Sitting");//1 true, 0 false
                    if (type == null) {
                        //Then do nothing because it is a player
                    } else if (type.equals(EntityType.BAT)) {
                        ByteTag batFlags = entity.get("BatFlags");//1 hanging, 0 flying
                    } else if (type.equals(EntityType.CHICKEN)) {
                        ByteTag isChickenJockey = entity.get("IsChickenJockey");//1 true, 0 false
                        IntTag eggLayTime = entity.get("EggLayTime");
                    } else if (type.equals(EntityType.CREEPER)) {
                        ByteTag powered = entity.get("powered");//1 true, 0 false
                        ByteTag explosionRadius = entity.get("ExplosionRadius");
                        ShortTag fuse = entity.get("Fuse");
                        ByteTag ignited = entity.get("ignited");//1 true, 0 false
                    } else if (type.equals(EntityType.ENDERMAN)) {
                        ShortTag carried = entity.get("carried");
                        ShortTag carriedData = entity.get("carriedData");
                    } else if (type.equals(EntityType.ENDERMITE)) {
                        IntTag lifeTime = entity.get("Lifetime");
                        ByteTag playerSpawned = entity.get("PlayerSpawned");
                    } else if (type.equals(EntityType.HORSE)) {
                        ByteTag bred = entity.get("Bred");
                        ByteTag chestedHorse = entity.get("ChestedHorse");//1 true, 0 false
                        ByteTag eatingHaystack = entity.get("EatingHaystack");//1 true, 0 false
                        ByteTag hasReproduced = entity.get("HasReproduced");//1 true, 0 false
                        ByteTag tame = entity.get("Tame");//1 true, 0 false
                        IntTag temper = entity.get("Temper");
                        IntTag horseType = entity.get("Type");
                        IntTag variant = entity.get("Variant");
                        StringTag tamerName = entity.get("OwnerName");
                        UUID tamerUUID = UUID.fromString(((StringTag) entity.get("OwnerUUID")).getValue());
                        ListTag items = entity.get("Items");
                        if (items != null)
                            for (int j = 0; j < items.size(); j++) {
                                CompoundTag item = items.get(j);
                            }
                        CompoundTag armorItem = entity.get("ArmorItem");
                        CompoundTag saddleItem = entity.get("SaddleItem");
                        ByteTag saddle = entity.get("Saddle");//1 true, 0 false
                    } else if (type.equals(EntityType.GHAST)) {
                        IntTag explosionPower = entity.get("ExplosionPower");
                    } else if (type.equals(EntityType.GUARDIAN)) {
                        ByteTag elder = entity.get("Elder");//1 true, 0 false
                    } else if (type.equals(EntityType.OCELOT)) {
                        IntTag catType = entity.get("CatType");
                    } else if (type.equals(EntityType.PIG)) {
                        ByteTag saddle = entity.get("Saddle");//1 true, 0 false
                    } else if (type.equals(EntityType.RABBIT)) {
                        IntTag rabbitType = entity.get("RabbitType");
                        IntTag moreCarrotTicks = entity.get("MoreCarrotTicks");
                    } else if (type.equals(EntityType.SHEEP)) {
                        ByteTag sheared = entity.get("Sheared");//1 true, 0 false
                        ByteTag color = entity.get("Color");
                    } else if (type.equals(EntityType.SKELETON)) {
                        ByteTag skeletonType = entity.get("SkeletonType");
                    } else if (type.equals(EntityType.SLIME) || type.equals(EntityType.MAGMA_CUBE)) {
                        IntTag size = entity.get("Size");
                        ByteTag wasOnGround = entity.get("wasOnGround");//1 true, 0 false
                    } else if (type.equals(EntityType.WITHER)) {
                        IntTag invul = entity.get("Invul");
                    } else if (id.getValue().equals(EntityType.WOLF.getSavegameID())) {
                        ByteTag angry = entity.get("Angry");//1 true, 0 false
                        ByteTag collarColor = entity.get("CollarColor");
                    } else if (type.equals(EntityType.VILLAGER)) {
                        IntTag profession = entity.get("Profession");
                        IntTag riches = entity.get("Riches");
                        IntTag career = entity.get("Career");
                        IntTag careerLevel = entity.get("CareerLevel");
                        ByteTag willing = entity.get("Willing");//1 true, 0 false
                        ListTag inventory = entity.get("Inventory");
                        if (inventory != null)
                            for (int j = 0; j < inventory.size(); j++) {
                                CompoundTag item = inventory.get(j);
                            }
                        CompoundTag offers = entity.get("Offers");
                        ListTag recipes = offers.get("Recipes");
                        if (recipes != null)
                            for (int j = 0; j < recipes.size(); j++) {
                                CompoundTag recipe = recipes.get(j);
                                ByteTag rewardExp = recipe.get("rewardExp");//1 true, 0 false
                                IntTag maxUses = recipe.get("maxUses");
                                IntTag uses = recipe.get("uses");
                                CompoundTag buy = recipe.get("buy");
                                CompoundTag buyB = recipe.get("buyB");
                                CompoundTag sell = recipe.get("sell");
                            }
                    } else if (type.equals(EntityType.IRON_GOLEM)) {
                        ByteTag playerCreated = entity.get("PlayerCreated");//1 true, 0 false
                    } else if (type.equals(EntityType.ZOMBIE)) {
                        ByteTag isVillager = entity.get("IsVillager");//1 true, 0 false
                        ByteTag isBaby = entity.get("IsBaby");//1 true, 0 false
                        IntTag conversionTime = entity.get("ConversionTime");
                        ByteTag canBreakDoors = entity.get("CanBreakDoors");//1 true, 0 false
                    } else if (type.equals(EntityType.ZOMBIE_PIGMAN)) {
                        ByteTag isVillager = entity.get("IsVillager");//1 true, 0 false
                        ByteTag isBaby = entity.get("IsBaby");//1 true, 0 false
                        IntTag conversionTime = entity.get("ConversionTime");
                        ByteTag canBreakDoors = entity.get("CanBreakDoors");//1 true, 0 false
                        ShortTag anger = entity.get("Anger");
                    }
                } else if (type.isProjectile()) {
                    ShortTag xTile = entity.get("xTile");
                    ShortTag yTile = entity.get("yTile");
                    ShortTag zTile = entity.get("zTile");
                    ByteTag inTile = entity.get("inTile");//1 true, 0 false
                    if (!type.equals(EntityType.BLAZE_FIREBALL) && !type.equals(EntityType.GHAST_FIREBALL) && !type.equals(EntityType.WITHER_SKULL)) {
                        ByteTag shake = entity.get("shake");
                    }
                    if (type.equals(EntityType.ARROW)) {
                        ByteTag inData = entity.get("inData");
                        ByteTag pickup = entity.get("pickup");
                        ByteTag player = entity.get("player");//1 true, 0 false
                        ShortTag life = entity.get("life");
                        DoubleTag damage = entity.get("damage");
                        ByteTag inGround = entity.get("inGround");
                    } else if (type.equals(EntityType.BLAZE_FIREBALL) || type.equals(EntityType.GHAST_FIREBALL) || type.equals(EntityType.WITHER_SKULL)) {
                        ListTag direction = entity.get("direction");
                        if (direction != null) {
                            DoubleTag dX = direction.get(0);
                            DoubleTag dY = direction.get(1);
                            DoubleTag dZ = direction.get(2);
                        }
                        if (type.equals(EntityType.GHAST_FIREBALL)) {
                            IntTag explosionPower = entity.get("ExplosionPower");
                        }
                    } else if (type.equals(EntityType.SNOWBALL) || type.equals(EntityType.THROWN_ENDER_PEARL) || type.equals(EntityType.THROWN_EXP_BOTTLE)) {
                        StringTag ownerName = entity.get("ownerName");
                    } else if (type.equals(EntityType.THROWN_SPLASH_POTION)) {
                        StringTag ownerName = entity.get("ownerName");
                        CompoundTag potion = entity.get("Potion");
                    }
                } else if (type.isItem()) {
                    ShortTag age = entity.get("Age");
                    if (type.equals(EntityType.ITEM)) {
                        ShortTag health = entity.get("Health");
                        ShortTag pickupDelay = entity.get("PickupDelay");
                        StringTag owner = entity.get("Owner");
                        StringTag thrower = entity.get("Thrower");
                        CompoundTag item = entity.get("Item");
                    } else if (type.equals(EntityType.XP_ORB)) {
                        ByteTag health = entity.get("Health");
                        ShortTag value = entity.get("Value");
                    }
                } else if (type.isVehicle()) {
                    if (!type.equals(EntityType.BOAT)) {
                        ByteTag customDisplayTile = entity.get("CustomDisplayTile");
                        StringTag displayTile = entity.get("DisplayTile");
                        IntTag displayData = entity.get("DisplayData");
                        IntTag displayOffset = entity.get("DisplayOffset");
                        StringTag minecartName = entity.get("CustomName");
                    }
                    if (type.equals(EntityType.MINECART_CHEST)) {
                        ListTag items = entity.get("Items");
                        if (items != null)
                            for (int j = 0; j < items.size(); j++) {
                                CompoundTag item = items.get(j);
                            }
                    } else if (type.equals(EntityType.MINECART_HOPPER)) {
                        IntTag transferCooldown = entity.get("TransferCooldown");
                        ListTag items = entity.get("Items");
                        if (items != null)
                            for (int j = 0; j < items.size(); j++) {
                                CompoundTag item = items.get(j);
                            }
                    } else if (type.equals(EntityType.MINECART_FURNACE)) {
                        IntTag transferCooldown = entity.get("TransferCooldown");
                    } else if (type.equals(EntityType.MINECART_TNT)) {
                        IntTag tntFuse = entity.get("TNTFuse");
                    } else if (type.equals(EntityType.MINECART_SPAWNER)) {
                        ListTag spawnPotentials = entity.get("SpawnPotentials");
                        if(spawnPotentials != null)
                            for (int j = 0; j < spawnPotentials.size(); j++) {
                                CompoundTag spawnPotential = spawnPotentials.get(j);
                                StringTag entityType = spawnPotential.get("Type");
                                IntTag weight = spawnPotential.get("Weight");
                                CompoundTag properties = spawnPotential.get("Properties");
                            }
                        StringTag entityID = entity.get("EntityId");
                        CompoundTag spawnData = entity.get("SpawnData");
                        ShortTag spawnCount = entity.get("SpawnCount");
                        ShortTag spawnRange = entity.get("SpawnRange");
                        ShortTag delay = entity.get("Delay");
                        ShortTag minSpawnDelay = entity.get("MinSpawnDelay");
                        ShortTag maxSpawnDelay = entity.get("MaxSpawnDelay");
                        ShortTag maxNearbyEntities = entity.get("MaxNearbyEntities");
                        ShortTag requiredPlayerRange = entity.get("RequiredPlayerRange");
                    } else if (type.equals(EntityType.MINECART_COMMAND_BLOCK)) {
                        StringTag command = entity.get("Command");
                        IntTag successCount = entity.get("SuccessCount");
                        StringTag lastOutput = entity.get("LastOutput");
                        ByteTag trackOutput = entity.get("TrackOutput");//1 true, 0 false
                    }
                } else if (type.isDynamic()) {
                    if (type.equals(EntityType.PRIMED_TNT)) {
                        ByteTag fuse = entity.get("Fuse");
                    } else {//type.equals(EntityType.FALLING_SAND)
                        IntTag tileID = entity.get("TileID");
                        StringTag block = entity.get("Block");
                        CompoundTag tileEntityData = entity.get("TileEntityData");
                        ByteTag data = entity.get("Data");
                        ByteTag time = entity.get("Time");
                        ByteTag dropItem = entity.get("DropItem");//1 true, 0 false
                        ByteTag hurtEntities = entity.get("HurtEntities");//1 true, 0 false
                        IntTag fallHurtMax = entity.get("FallHurtMax");
                        FloatTag fallHurtAmount = entity.get("FallHurtAmount");
                    }
                } else if (type.equals(EntityType.ARMOR_STAND)) {
                    IntTag disabledSlots = entity.get("DisabledSlots");
                    ListTag equipment = entity.get("Equipment");
                    if (equipment != null) {
                        CompoundTag hand = equipment.get(0);
                        CompoundTag boots = equipment.get(1);
                        CompoundTag leggings = equipment.get(2);
                        CompoundTag chestplate = equipment.get(3);
                        CompoundTag helmet = equipment.get(4);
                    }
                    ByteTag marker = entity.get("Marker");//1 true, 0 false
                    ByteTag invisible = entity.get("Invisible");//1 true, 0 false
                    ByteTag noBasePlate = entity.get("NoBasePlate");//1 true, 0 false
                    ByteTag noGravity = entity.get("NoGravity");//1 true, 0 false
                    CompoundTag pose = entity.get("Pose");
                    ListTag body = pose.get("Body");
                    if (body != null) {
                        FloatTag xRot = body.get(0);
                        FloatTag yRot = body.get(1);
                        FloatTag zRot = body.get(2);
                    }
                    ListTag leftArm = pose.get("LeftArm");
                    if (leftArm != null) {
                        FloatTag xRot = leftArm.get(0);
                        FloatTag yRot = leftArm.get(1);
                        FloatTag zRot = leftArm.get(2);
                    }
                    ListTag rightArm = pose.get("RightArm");
                    if (rightArm != null) {
                        FloatTag xRot = rightArm.get(0);
                        FloatTag yRot = rightArm.get(1);
                        FloatTag zRot = rightArm.get(2);
                    }
                    ListTag leftLeg = pose.get("LeftLeg");
                    if (leftLeg != null) {
                        FloatTag xRot = leftLeg.get(0);
                        FloatTag yRot = leftLeg.get(1);
                        FloatTag zRot = leftLeg.get(2);
                    }
                    ListTag rightLeg = pose.get("RightLeg");
                    if (rightLeg != null) {
                        FloatTag xRot = rightLeg.get(0);
                        FloatTag yRot = rightLeg.get(1);
                        FloatTag zRot = rightLeg.get(2);
                    }
                    ListTag head = pose.get("Head");
                    if (head != null) {
                        FloatTag xRot = head.get(0);
                        FloatTag yRot = head.get(1);
                        FloatTag zRot = head.get(2);
                    }
                    ByteTag showArms = entity.get("ShowArms");//1 true, 0 false
                    ByteTag small = entity.get("Small");//1 true, 0 false
                } else if (type.equals(EntityType.FIREWORKS_ROCKET)) {
                    IntTag life = entity.get("Life");
                    IntTag lifeTime = entity.get("LifeTime");
                    CompoundTag fireworksItem = entity.get("FireworksItem");
                } else if (type.equals(EntityType.PAINTING) || type.equals(EntityType.ITEM_FRAME)) {
                    IntTag tileX = entity.get("TileX");//Pre 1.8 compatibility
                    IntTag tileY = entity.get("TileY");//Pre 1.8 compatibility
                    IntTag tileZ = entity.get("TileZ");//Pre 1.8 compatibility
                    ByteTag facing = entity.get("Facing");
                    if (type.equals(EntityType.PAINTING)) {
                        StringTag motive = entity.get("Motive");
                    } else {//type.equals(EntityType.ITEM_FRAME)
                        CompoundTag item = entity.get("Item");
                        FloatTag itemDropChance = entity.get("ItemDropChance");
                        ByteTag itemRotation = entity.get("ItemRotation");
                    }
                }
                e = new Entity(loc, type);
                this.world.loadEntity(e);
            }
            ListTag tileEntities = level.get("TileEntities");
            for (int i = 0; i < tileEntities.size(); i++) {
                CompoundTag tile = tileEntities.get(i);
                StringTag id = tile.get("id");
                IntTag tileX = tile.get("x");
                IntTag tileY = tile.get("y");
                IntTag tileZ = tile.get("z");
                Location loc = (tileX == null || tileY == null || tileZ == null) ? null : new Location(this.world, tileX.getValue(), tileY.getValue(), tileZ.getValue());
                if (id.getValue().equals("Airportal")) {//End Portal

                } else if (id.getValue().equals("Banner")) {
                    IntTag base = tile.get("Base");
                    ListTag patterns = tile.get("Patterns");
                    if (patterns != null)
                        for (int j = 0; j < patterns.size(); j++) {
                            CompoundTag pattern = patterns.get(j);
                            IntTag color = pattern.get("Color");
                            StringTag location = pattern.get("Pattern");
                        }
                } else if (id.getValue().equals("Beacon")) {
                    StringTag lock = tile.get("Lock");
                    IntTag levels = tile.get("Levels");
                    IntTag primary = tile.get("Primary");
                    IntTag secondary = tile.get("Secondary");
                } else if (id.getValue().equals("Cauldron")) {//Brewing Stand
                    StringTag customName = tile.get("CustomName");
                    StringTag lock = tile.get("Lock");
                    ListTag items = tile.get("Items");//4 slots
                    IntTag brewTime = tile.get("BrewTime");
                } else if (id.getValue().equals("Chest")) {//Chest and Trapped Chest
                    StringTag customName = tile.get("CustomName");
                    StringTag lock = tile.get("Lock");
                    ListTag items = tile.get("Items");//27 slots
                } else if (id.getValue().equals("Comparator")) {
                    IntTag outputSignal = tile.get("OutputSignal");
                } else if (id.getValue().equals("Control")) {//Command Block
                    StringTag customName = tile.get("CustomName");
                    StringTag command = tile.get("Command");
                    IntTag successCount = tile.get("SuccessCount");
                    StringTag lastOutput = tile.get("LastOutput");
                    ByteTag trackOutput = tile.get("TrackOutput");//1 true, 0 false
                    CompoundTag commandStats = tile.get("CommandStats");
                    StringTag successCountName = commandStats.get("SuccessCountName");
                    StringTag successCountObjective = commandStats.get("SuccessCountObjective");
                    StringTag affectedBlocksName = commandStats.get("AffectedBlocksName");
                    StringTag affectedBlocksObjective = commandStats.get("AffectedBlocksObjective");
                    StringTag affectedEntitiesName = commandStats.get("AffectedEntitiesName");
                    StringTag affectedEntitiesObjective = commandStats.get("AffectedEntitiesObjective");
                    StringTag affectedItemsName = commandStats.get("AffectedItemsName");
                    StringTag affectedItemsObjective = commandStats.get("AffectedItemsObjective");
                    StringTag queryResultName = commandStats.get("QueryResultName");
                    StringTag queryResultObjective = commandStats.get("QueryResultObjective");
                } else if (id.getValue().equals("DLDetector")) {//Daylight Sensor

                } else if (id.getValue().equals("Dropper") || id.getValue().equals("Trap")) {//Trap = dispenser
                    StringTag customName = tile.get("CustomName");
                    StringTag lock = tile.get("Lock");
                    ListTag items = tile.get("Items");//9 slots
                } else if (id.getValue().equals("EnchantTable")) {
                    StringTag customName = tile.get("CustomName");
                } else if (id.getValue().equals("EnderChest")) {

                } else if (id.getValue().equals("FlowerPot")) {
                    StringTag item = tile.get("Item");
                    IntTag data = tile.get("Data");
                } else if (id.getValue().equals("Furnace")) {
                    StringTag customName = tile.get("CustomName");
                    StringTag lock = tile.get("Lock");
                    ShortTag burnTime = tile.get("BurnTime");
                    ShortTag cookTime = tile.get("CookTime");
                    ShortTag cookTimeTotal = tile.get("CookTimeTotal");
                    ListTag items = tile.get("Items");//3 slots
                } else if (id.getValue().equals("Hopper")) {
                    StringTag customName = tile.get("CustomName");
                    StringTag lock = tile.get("Lock");
                    ListTag items = tile.get("Items");//5 slots
                    IntTag transferCooldown = tile.get("TransferCooldown");
                } else if (id.getValue().equals("MobSpawner")) {
                    ListTag spawnPotentials = tile.get("SpawnPotentials");
                    if(spawnPotentials != null)
                        for (int j = 0; j < spawnPotentials.size(); j++) {
                            CompoundTag spawnPotential = spawnPotentials.get(j);
                            StringTag type = spawnPotential.get("Type");
                            IntTag weight = spawnPotential.get("Weight");
                            CompoundTag properties = spawnPotential.get("Properties");
                        }
                    StringTag entityID = tile.get("EntityId");
                    CompoundTag spawnData = tile.get("SpawnData");
                    ShortTag spawnCount = tile.get("SpawnCount");
                    ShortTag spawnRange = tile.get("SpawnRange");
                    ShortTag delay = tile.get("Delay");
                    ShortTag minSpawnDelay = tile.get("MinSpawnDelay");
                    ShortTag maxSpawnDelay = tile.get("MaxSpawnDelay");
                    ShortTag maxNearbyEntities = tile.get("MaxNearbyEntities");
                    ShortTag requiredPlayerRange = tile.get("RequiredPlayerRange");
                } else if (id.getValue().equals("Music")) {//Note Block
                    ByteTag note = tile.get("note");
                } else if (id.getValue().equals("Piston")) {//Piston Moving
                    IntTag blockId = tile.get("blockId");
                    IntTag blockData = tile.get("blockData");
                    IntTag facing = tile.get("facing");
                    FloatTag progress = tile.get("progress");
                    ByteTag extending = tile.get("extending");//1 true, 0 false
                } else if (id.getValue().equals("RecordPlayer")) {//Jukebox
                    IntTag record = tile.get("Record");
                    CompoundTag recordItem = tile.get("RecordItem");
                } else if (id.getValue().equals("Sign")) {
                    StringTag line1 = tile.get("Text1");
                    StringTag line2 = tile.get("Text2");
                    StringTag line3 = tile.get("Text3");
                    StringTag line4 = tile.get("Text4");
                    String[] lines = new String[4];
                    lines[0] = line1.getValue();
                    lines[1] = line2.getValue();
                    lines[2] = line3.getValue();
                    lines[3] = line4.getValue();
                    this.world.addSign(new Sign(loc, lines));
                } else if (id.getValue().equals("Skull")) {//Head
                    ByteTag skullType = tile.get("SkullType");
                    StringTag extraType = tile.get("ExtraType");//Legacy support for pre 1.8
                    ByteTag rotation = tile.get("Rot");
                    CompoundTag owner = tile.get("Owner");
                    UUID uuid = UUID.fromString(((StringTag) owner.get("Id")).getValue());
                    StringTag name = owner.get("Name");
                    CompoundTag properties = owner.get("Properties");
                    ListTag textures = properties.get("textures");
                    if (textures != null)
                        for (int j = 0; j < textures.size(); j++) {
                            CompoundTag texture = textures.get(j);
                            StringTag signature = texture.get("Signature");
                            StringTag value = texture.get("Value");
                        }
                }
            }
            Chunk[] chunks = new Chunk[16];
            for (int i = 0; i < sections.size(); i++) {//Loop through all 16 chunks in a verticle fashion
                CompoundTag chunkz = sections.get(i);
                ByteArrayTag blocks = chunkz.get("Blocks");
                ByteArrayTag blockLight = chunkz.get("BlockLight");
                ByteArrayTag skyLight = chunkz.get("SkyLight");
                ByteArrayTag data = chunkz.get("Data");
                ByteArrayTag add = chunkz.get("Add");
                ShortArray3d block = new ShortArray3d(4096);
                for (int cY = 0; cY < 16; cY++) //Loop through the Y axis
                    for (int cZ = 0; cZ < 16; cZ++) //Loop through z
                        for (int cX = 0; cX < 16; cX++) { //Loop through x
                            int index = 256*cY + 16*cZ + cX;
                            byte blockValue = blocks.getValue(index);
                            if (blockValue >= 0)//Figure out why sometimes it gives negative values...
                                block.setBlockAndData(cX, cY, cZ, blockValue + (add != null ? getValue(add, index) << 8 : 0), getValue(data, index));
                        }
                chunks[i] = new Chunk(block, new NibbleArray3d(blockLight.getValue()), new NibbleArray3d(skyLight.getValue()));
            }
            this.world.addColumn(new Column(l, chunks, biomes.getValue()));
        }
    }

    public void readChunk(long l, Player p, Direction dir, boolean removeOld) {
        if (this.invalid)
            return;
        if (p.isColumnLoaded(l) && !removeOld)
            return;
        if (this.world.isColumnLoaded(l)) {
            p.addColumn(l, dir, removeOld);
            return;
        }
        readChunk(l);
        if (this.world.isColumnLoaded(l))
            p.addColumn(l, dir, removeOld);
    }

    private int getValue(ByteArrayTag array, int index) {//From pseudo-code on minecraft wiki Chunk Format page
        return (index%2 == 0 ? array.getValue(index/2) : array.getValue(index/2) >> 4)&0x0F;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }
}