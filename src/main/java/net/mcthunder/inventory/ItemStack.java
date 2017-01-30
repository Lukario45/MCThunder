package net.mcthunder.inventory;

import net.mcthunder.api.*;
import net.mcthunder.block.Material;
import org.spacehq.mc.auth.properties.Property;
import org.spacehq.opennbt.tag.builtin.*;

import java.util.*;

public class ItemStack {//TODO: Add a simpler way to modify blockEntityTag once I do more with tile entities
    private HashMap<Enchantment, Integer> storedEnchants = new HashMap<>(), enchantments = new HashMap<>();
    private ArrayList<Material> canBeDestroyed = new ArrayList<>(), canBePlacedOn = new ArrayList<>();
    private HashMap<PotionEffectType, PotionEffect> potionEffects = new HashMap<>();
    private int amount, repairCost = 0, generation = 0, color = 0, hideFlags = 0;
    private String ownerName = null, title = null, author = null, displayName;
    private List<String> pages = new ArrayList<>(), lore = new ArrayList<>();
    private boolean unbreakable = false, resolved = false, scaling = false;
    private ArrayList<Decoration> decorations = new ArrayList<>();
    private HashMap<String,Modifier> modifiers = new HashMap<>();
    private ArrayList<Explosion> explosions = new ArrayList<>();
    private ArrayList<Property> textures = new ArrayList<>();
    private CompoundTag blockEntityTag = null;
    private UUID ownerUUID = null;
    private byte flight = 0;
    private Material type;

    public ItemStack(Material type) {
        this(type, 1);
    }

    public ItemStack(Material type, int amount) {
        this(type, amount, null);
    }

    public ItemStack(Material type, int amount, CompoundTag nbt) {
        this.type = type;
        this.amount = amount;
        this.displayName = this.type.plural(this.amount);
        if (nbt != null) {
            ByteTag unbreak = nbt.get("Unbreakable");
            this.unbreakable = unbreak != null && unbreak.getValue() == (byte) 1;
            ListTag canDestroy = nbt.get("CanDestroy");
            if (canDestroy != null)
                for (int i = 0; i < canDestroy.size(); i++) {
                    StringTag id = canDestroy.get(i);
                    Material mat = id == null ? null : Material.fromString(id.getValue().split("minecraft:")[1]);
                    if (mat != null && !this.canBeDestroyed.contains(mat))
                        this.canBeDestroyed.add(mat);
                }
            ListTag canPlaceOn = nbt.get("CanPlaceOn");
            if (canPlaceOn != null)
                for (int i = 0; i < canPlaceOn.size(); i++) {
                    StringTag id = canPlaceOn.get(i);
                    Material mat = id == null ? null : Material.fromString(id.getValue().split("minecraft:")[1]);
                    if (mat != null && !this.canBePlacedOn.contains(mat))
                        this.canBePlacedOn.add(mat);
                }
            this.blockEntityTag = nbt.get("BlockEntityTag");
            ListTag enchants = nbt.get("ench");
            if (enchants != null)
                for (int i = 0; i < enchants.size(); i++) {
                    CompoundTag enchant = enchants.get(i);
                    ShortTag id = enchant.get("id");
                    ShortTag lvl = enchant.get("lvl");
                    this.enchantments.put(Enchantment.fromID(id.getValue()), (int) lvl.getValue());
                }
            ListTag storedEnchantments = nbt.get("StoredEnchantments");
            if (storedEnchantments != null)
                for (int i = 0; i < storedEnchantments.size(); i++) {
                    CompoundTag enchant = storedEnchantments.get(i);
                    ShortTag id = enchant.get("id");
                    ShortTag lvl = enchant.get("lvl");
                    this.storedEnchants.put(Enchantment.fromID(id.getValue()), (int) lvl.getValue());
                }
            IntTag repair = nbt.get("RepairCost");
            this.repairCost = repair != null ? repair.getValue() : 0;
            ListTag modifiers = nbt.get("AttributeModifiers");
            if (modifiers != null)
                for (int i = 0; i < modifiers.size(); i++) {
                    CompoundTag modifier = modifiers.get(i);
                    StringTag attributeName = modifier.get("AttributeName");
                    StringTag modName = modifier.get("Name");
                    DoubleTag modAmount = modifier.get("Amount");
                    IntTag operation = modifier.get("Operation");
                    LongTag modUUIDMost = modifier.get("UUIDMost");
                    LongTag modUUIDLeast = modifier.get("UUIDLeast");
                    if (attributeName != null && modName != null && modAmount != null && operation != null && modUUIDMost != null && modUUIDLeast != null)
                        this.modifiers.put(attributeName.getValue().trim(), new Modifier(attributeName.getValue(), modName.getValue(), modAmount.getValue(), operation.getValue(),
                            modUUIDMost.getValue(), modUUIDLeast.getValue()));
                }
            ListTag customPotionEffects = nbt.get("CustomPotionEffects");
            if (customPotionEffects != null)
                for (int i = 0; i < customPotionEffects.size(); i++) {
                    CompoundTag potionEffect = customPotionEffects.get(i);
                    ByteTag id = potionEffect.get("Id");
                    ByteTag amplifier = potionEffect.get("Amplifier");
                    IntTag duration = potionEffect.get("Duration");
                    ByteTag ambient = potionEffect.get("Ambient");
                    ByteTag showParticles = potionEffect.get("ShowParticles");
                    if (id == null || amplifier == null || duration == null)
                        continue;
                    PotionEffect potion = new PotionEffect(PotionEffectType.fromID(id.getValue()), duration.getValue(), amplifier.getValue());
                    potion.setAmbient(ambient != null && ambient.getValue() == (byte) 1);
                    potion.setShowParticles(showParticles != null && showParticles.getValue() == (byte) 1);
                    this.potionEffects.put(potion.getType(), potion);
                }
            CompoundTag display = nbt.get("display");
            if (display != null) {
                IntTag color = display.get("color");
                if (color != null)
                    this.color = color.getValue();
                StringTag name = display.get("Name");
                if (name != null)
                    this.displayName = name.getValue();
                ListTag lore = display.get("Lore");
                if (lore != null)
                    for (int i = 0; i < lore.size(); i++)
                        this.lore.add(((StringTag) lore.get(i)).getValue());
            }
            IntTag hide = nbt.get("HideFlags");
            if (hide != null)
                this.hideFlags = hide.getValue();
            ByteTag resolved = nbt.get("resolved");
            this.resolved = resolved != null && resolved.getValue() == (byte) 1;
            IntTag generation = nbt.get("generation");
            if (generation != null)
                this.generation = generation.getValue();
            StringTag author = nbt.get("author");
            if (author != null)
                this.author = author.getValue();
            StringTag title = nbt.get("title");
            if (title != null)
                this.title = title.getValue();
            ListTag pages = nbt.get("pages");
            if (pages != null)
                for (int i = 0; i < pages.size(); i++)
                    this.pages.add(((StringTag) pages.get(i)).getValue());
            try {
                CompoundTag owner = nbt.get("SkullOwner");
                StringTag uuidString = (owner == null ? null : (StringTag) owner.get("Id"));
                if (uuidString != null)
                    this.ownerUUID = UUID.fromString(uuidString.getValue());
                if (owner != null)
                    this.ownerName = ((StringTag) owner.get("Name")).getValue();
                CompoundTag properties = (owner == null ? null : (CompoundTag) owner.get("Properties"));
                ListTag text = (properties == null ? null : (ListTag) properties.get("textures"));
                if (text != null)
                    for (int i = 0; i < text.size(); i++) {
                        CompoundTag texture = text.get(i);
                        StringTag signature = texture.get("Signature");
                        StringTag value = texture.get("Value");
                        this.textures.add(new Property("textures", value == null ? null : value.getValue(), signature == null ? null : signature.getValue()));
                    }
            } catch (Exception e) {
                StringTag skullOwner = nbt.get("SkullOwner");
                if (skullOwner != null)
                    this.ownerName = skullOwner.getValue();
            }
            CompoundTag explosion = nbt.get("Explosion");
            if (explosion != null) {
                ByteTag flicker = explosion.get("Flicker");
                ByteTag trail = explosion.get("Trail");
                ByteTag explosionType = explosion.get("Type");
                IntArrayTag colors = explosion.get("Colors");
                IntArrayTag fadeColors = explosion.get("FadeColors");
                if (flicker != null && trail != null && explosionType != null && colors != null && fadeColors != null)
                    this.explosions.add(new Explosion(flicker.getValue() == (byte) 1, trail.getValue() == (byte) 1, explosionType.getValue(),
                            colors.getValue(), fadeColors.getValue()));
            }
            CompoundTag fireworks = nbt.get("Fireworks");
            if (fireworks != null) {
                ByteTag flight = fireworks.get("Flight");
                this.flight = flight == null ? 0 : flight.getValue();
                ListTag explosions = fireworks.get("Explosions");
                if (explosions != null)
                    for (int i = 0; i < explosions.size(); i++) {
                        explosion = explosions.get(i);
                        ByteTag flicker = explosion.get("Flicker");
                        ByteTag trail = explosion.get("Trail");
                        ByteTag explosionType = explosion.get("Type");
                        IntArrayTag colors = explosion.get("Colors");
                        IntArrayTag fadeColors = explosion.get("FadeColors");
                        if (flicker != null && trail != null && explosionType != null && colors != null && fadeColors != null)
                            this.explosions.add(new Explosion(flicker.getValue() == (byte) 1, trail.getValue() == (byte) 1, explosionType.getValue(),
                                    colors.getValue(), fadeColors.getValue()));
                    }
            }
            ByteTag scaling = nbt.get("map_is_scaling");
            this.scaling = scaling != null && scaling.getValue() == (byte) 1;
            ListTag decorations = nbt.get("Decorations");
            if (decorations != null)
                for (int i = 0; i < decorations.size(); i++) {
                    CompoundTag decoration = decorations.get(i);
                    StringTag id = decoration.get("id");
                    ByteTag decorationType = decoration.get("type");
                    DoubleTag x = decoration.get("x");
                    DoubleTag z = decoration.get("z");
                    DoubleTag rotation = decoration.get("rot");
                    if (id != null && decorationType != null && x != null && z != null && rotation != null)
                        this.decorations.add(new Decoration(id.getValue(), decorationType.getValue(), x.getValue(), z.getValue(), rotation.getValue()));
                }
        }
        if (!this.modifiers.containsKey("generic.attackDamage")) {
            Modifier d = getDamage();
            if (d != null)
                modifiers.put(d.getAttributeName(), d);
        }
    }

    private Modifier getDamage() {
        UUID temp = UUID.randomUUID();
        if (this.type.equals(Material.WOODEN_SHOVEL) || this.type.equals(Material.GOLDEN_SHOVEL))
            return new Modifier("generic.attackDamage", "generic.attackDamage",1,0,temp.getLeastSignificantBits(), temp.getMostSignificantBits());
        else if (this.type.equals(Material.WOODEN_PICKAXE) || this.type.equals(Material.GOLDEN_PICKAXE) || this.type.equals(Material.STONE_SHOVEL))
            return new Modifier("generic.attackDamage", "generic.attackDamage",2,0,temp.getLeastSignificantBits(), temp.getMostSignificantBits());
        else if (this.type.equals(Material.WOODEN_AXE) || this.type.equals(Material.GOLDEN_AXE) || this.type.equals(Material.STONE_PICKAXE) ||
                this.type.equals(Material.IRON_SHOVEL))
            return new Modifier("generic.attackDamage", "generic.attackDamage",3,0,temp.getLeastSignificantBits(), temp.getMostSignificantBits());
        else if (this.type.equals(Material.WOODEN_SWORD) || this.type.equals(Material.GOLDEN_SWORD) || this.type.equals(Material.STONE_AXE) ||
                this.type.equals(Material.IRON_PICKAXE) || this.type.equals(Material.DIAMOND_SHOVEL))
            return new Modifier("generic.attackDamage", "generic.attackDamage",4,0,temp.getLeastSignificantBits(), temp.getMostSignificantBits());
        else if (this.type.equals(Material.STONE_SWORD) || this.type.equals(Material.IRON_AXE) || this.type.equals(Material.DIAMOND_PICKAXE))
            return new Modifier("generic.attackDamage", "generic.attackDamage",5,0,temp.getLeastSignificantBits(), temp.getMostSignificantBits());
        else if (this.type.equals(Material.IRON_SWORD) || this.type.equals(Material.DIAMOND_AXE))
            return new Modifier("generic.attackDamage", "generic.attackDamage",6,0,temp.getLeastSignificantBits(), temp.getMostSignificantBits());
        else if (this.type.equals(Material.DIAMOND_SWORD))
            return new Modifier("generic.attackDamage", "generic.attackDamage",7,0,temp.getLeastSignificantBits(), temp.getMostSignificantBits());
        return null;
    }

    public Material getType() {
        return this.type;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String name) {
        this.displayName = name;
    }

    public void setType(Material newType) {
        this.type = newType;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int newAmount) {
        this.amount = newAmount;
    }

    public void addTexture(Property texture) {
        if (!this.textures.contains(texture))
            this.textures.add(texture);
    }

    public void removeTexture(Property texture) {
        if (this.textures.contains(texture))
            this.textures.remove(texture);
    }

    public Collection<Property> getTextures() {
        return this.textures;
    }

    public void addCanBeDestroyed(Material m) {
        if (!this.canBeDestroyed.contains(m))
            this.canBeDestroyed.add(m);
    }

    public void removeCanBeDestroyed(Material m) {
        if (this.canBeDestroyed.contains(m))
            this.canBeDestroyed.remove(m);
    }

    public Collection<Material> getCanBeDestroyed() {
        return this.canBeDestroyed;
    }

    public void addCanBePlacedOn(Material m) {
        if (!this.canBePlacedOn.contains(m))
            this.canBePlacedOn.add(m);
    }

    public void removeCanBePlacedOn(Material m) {
        if (this.canBePlacedOn.contains(m))
            this.canBePlacedOn.remove(m);
    }

    public Collection<Modifier> getModifiers() {
        return this.modifiers.values();
    }

    public Modifier getModifier(String name) {
        return this.modifiers.get(name);
    }

    public void addModifier(Modifier m) {
        this.modifiers.put(m.getAttributeName(), m);
    }

    public void removeModifier(Modifier m) {
        this.modifiers.remove(m.getAttributeName());
    }

    public Collection<Explosion> getExplosions() {
        return this.explosions;
    }

    public void addExplosion(Explosion e) {
        if (!this.explosions.contains(e))
            this.explosions.add(e);
    }

    public void removeExplosion(Explosion e) {
        if (this.explosions.contains(e))
            this.explosions.remove(e);
    }

    public Collection<Decoration> getDecorations() {
        return this.decorations;
    }

    public void addDecoration(Decoration d) {
        if (!this.decorations.contains(d))
            this.decorations.add(d);
    }

    public void removeDecoration(Decoration d) {
        if (this.decorations.contains(d))
            this.decorations.remove(d);
    }

    public Collection<String> getPages() {
        return this.pages;
    }

    public void setPages(List<String> pages) {
        if (pages != null)
            this.pages = pages;
    }

    public Collection<String> getLore() {
        return this.lore;
    }

    public void setLore(List<String> lore) {
        if (lore != null)
            this.lore = lore;
    }

    public float getBonusDamnage() {
        return (float) (this.modifiers.get("generic.attackDamage") == null ? 0 : this.modifiers.get("generic.attackDamage").getAmount());
    }

    public Collection<Material> getCanBePlacedOn() {
        return this.canBePlacedOn;
    }

    public void addEnchantment(Enchantment e, int level) {
        this.enchantments.put(e, level);
    }

    public void removeEnchantment(Enchantment e) {
        this.enchantments.remove(e);
    }

    public boolean hasEnchantment(Enchantment e) {
        return this.enchantments.containsKey(e);
    }

    public Collection<Enchantment> getEnchantments() {
        return this.enchantments.keySet();
    }

    public void addStoredEnchantment(Enchantment e, int level) {
        this.storedEnchants.put(e, level);
    }

    public void removeStoredEnchantment(Enchantment e) {
        this.storedEnchants.remove(e);
    }

    public boolean hasStoredEnchantment(Enchantment e) {
        return this.storedEnchants.containsKey(e);
    }

    public Collection<Enchantment> getStoredEnchantments() {
        return this.storedEnchants.keySet();
    }

    public int getRepairCost() {
        return this.repairCost;
    }

    public void setRepairCost(int repairCost) {
        this.repairCost = repairCost < 0 ? 0 : repairCost;
    }

    public CompoundTag getBlockEntityTag() {
        return this.blockEntityTag;
    }

    public void setBlockEntityTag(CompoundTag blockEntityTag) {
        this.blockEntityTag = blockEntityTag;
    }

    public int getGeneration() {
        return this.generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation < 0 ? 0 : generation > 3 ? 3 : generation;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setColor(int red, int green, int blue) {
        setColor(red << 16 + green << 8 + blue);
    }

    public boolean isUnbreakable() {
        return this.unbreakable;
    }

    public void setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
    }

    public boolean isResolved() {
        return this.resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    public boolean isMapScaling() {
        return this.scaling;
    }

    public void setMapScaling(boolean scaling) {
        this.scaling = scaling;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    public void setOwnerUUID(UUID uuid) {
        this.ownerUUID = uuid;
    }

    public byte getFlight() {
        return this.flight;
    }

    public void setFlight(byte flight) {
        this.flight = flight;
    }

    public boolean hideEnchants() {
        return this.hideFlags % 2 == 1;
    }

    public void setHideEnchants(boolean hide) {
        if (this.hideFlags % 2 == 1 && !hide)
            this.hideFlags--;
        else if (this.hideFlags % 2 == 0 && hide)
            this.hideFlags++;
    }

    public boolean hideAttributeModifiers() {
        return this.hideFlags % 4 >= 2;
    }

    public void setHideAttributeModifiers(boolean hide) {
        if (this.hideFlags % 4 >= 2 && !hide)
            this.hideFlags -= 2;
        else if (this.hideFlags % 4 < 2 && hide)
            this.hideFlags += 2;
    }

    public boolean hideUnbreakable() {
        return this.hideFlags % 8 >= 4;
    }

    public void setHideUnbreakable(boolean hide) {
        if (this.hideFlags % 8 >= 4 && !hide)
            this.hideFlags -= 4;
        else if (this.hideFlags % 8 < 4 && hide)
            this.hideFlags += 4;
    }

    public boolean hideCanDestroy() {
        return this.hideFlags % 16 >= 8;
    }

    public void setHideCanDestroy(boolean hide) {
        if (this.hideFlags % 16 >= 8 && !hide)
            this.hideFlags -= 8;
        else if (this.hideFlags % 16 < 8 && hide)
            this.hideFlags += 8;
    }

    public boolean hideCanPlaceOn() {
        return this.hideFlags % 32 >= 16;
    }

    public void setHideCanPlaceOn(boolean hide) {
        if (this.hideFlags % 32 >= 16 && !hide)
            this.hideFlags -= 16;
        else if (this.hideFlags % 32 < 16 && hide)
            this.hideFlags += 16;
    }

    public boolean hideMisc() {
        return this.hideFlags - 32 >= 0;
    }

    public void setHideMisc(boolean hide) {
        if (this.hideFlags - 32 >= 0 && !hide)
            this.hideFlags -= 32;
        else if (this.hideFlags - 32 < 0 && hide)
            this.hideFlags += 32;
    }

    public boolean canStack(ItemStack other) {//TODO: Check max stack size
        CompoundTag nbt = getNBT();
        CompoundTag otherNBT = other.getNBT();
        return ((nbt == null && otherNBT == null) || (nbt != null && nbt.equals(otherNBT))) && this.type.equals(other.getType());
    }

    public CompoundTag getNBT() {
        if (this.type == null || this.type.equals(Material.AIR))
            return null;
        CompoundTag nbt = new CompoundTag("tag");
        if (Material.unbreakingList().contains(this.type.getParent()))
            nbt.put(new ByteTag("Unbreakable", (byte) (this.unbreakable ? 1 : 0)));
        ArrayList<Tag> temp = new ArrayList<>();
        for (Material canDestroy : this.canBeDestroyed)
            temp.add(new StringTag("id", "minecraft:" + canDestroy.getParent().getName().toLowerCase()));
        if (!temp.isEmpty())
            nbt.put(new ListTag("CanDestroy", temp));
        if (this.type.isBlock()) {
            temp.clear();
            for (Material canPlaceOn : this.canBePlacedOn)
                temp.add(new StringTag("id", "minecraft:" + canPlaceOn.getParent().getName().toLowerCase()));
            if (!temp.isEmpty())
                nbt.put(new ListTag("CanPlaceOn", temp));
            if (this.blockEntityTag != null)
                nbt.put(this.blockEntityTag);
        }
        temp.clear();
        for (Enchantment e : this.enchantments.keySet()) {
            CompoundTag enchant = new CompoundTag("enchantment");//Is this proper tag name and does it matter
            enchant.put(new ShortTag("id", (short) e.getID()));
            enchant.put(new ShortTag("lvl", (short) (int) this.enchantments.get(e)));
            temp.add(enchant);
        }
        if (!temp.isEmpty())
            nbt.put(new ListTag("ench", temp));
        temp.clear();
        for (Enchantment e : this.storedEnchants.keySet()) {
            CompoundTag enchant = new CompoundTag("storedEnchantment");//Is this proper tag name and does it matter
            enchant.put(new ShortTag("id", (short) e.getID()));
            enchant.put(new ShortTag("lvl", (short) (int) this.storedEnchants.get(e)));
            temp.add(enchant);
        }
        if (!temp.isEmpty())
            nbt.put(new ListTag("StoredEnchantments", temp));
        if (this.repairCost > 0)
            nbt.put(new IntTag("RepairCost", this.repairCost));
        temp.clear();
        for (Modifier mod : this.modifiers.values()) {
            CompoundTag modifier = new CompoundTag("Modifier");//Is this proper tag name and does it matter
            modifier.put(new StringTag("AttributeName", mod.getAttributeName()));
            modifier.put(new StringTag("Name", mod.getName()));
            modifier.put(new DoubleTag("Amount", mod.getAmount()));
            modifier.put(new IntTag("Operation", mod.getOperation()));
            modifier.put(new LongTag("UUIDMost", mod.getUUIDMost()));
            modifier.put(new LongTag("UUIDLeast", mod.getUUIDLeast()));
            temp.add(modifier);
        }
        if (!temp.isEmpty())
            nbt.put(new ListTag("AttributeModifiers", temp));
        if (this.type.isPotion()) {
            temp.clear();
            for (PotionEffect p : this.potionEffects.values()) {
                CompoundTag potion = new CompoundTag("customPotionEffect");//Is this proper tag name and does it matter
                potion.put(new ByteTag("Id", (byte) p.getType().getID()));
                potion.put(new ByteTag("Amplifier", (byte) p.getAmplifier()));
                potion.put(new IntTag("Duration", p.getDuration()));
                potion.put(new ByteTag("Ambient", (byte) (p.isAmbient() ? 1 : 0)));
                potion.put(new ByteTag("ShowParticles", (byte) (p.showParticles() ? 1 : 0)));
                temp.add(potion);
            }
            if (!temp.isEmpty())
                nbt.put(new ListTag("CustomPotionEffects", temp));
        }
        CompoundTag display = new CompoundTag("display");
        if (this.type.getParent().equals(Material.LEATHER_BOOTS) || this.type.getParent().equals(Material.LEATHER_CHESTPLATE) ||
                this.type.getParent().equals(Material.LEATHER_HELMET) || this.type.getParent().equals(Material.LEATHER_LEGGINGS))
            display.put(new IntTag("color", this.color));
        if (!this.displayName.equals(this.type.plural(this.amount)))
            display.put(new StringTag("Name", this.displayName));
        temp.clear();
        for (String piece : this.lore)
            temp.add(new StringTag("lore", piece));
        if (!temp.isEmpty())
            display.put(new ListTag("Lore", temp));
        if (!display.getValue().isEmpty())
            nbt.put(display);
        if (this.hideFlags > 0)
            nbt.put(new IntTag("HideFlags", this.hideFlags));
        if (this.type.getParent().equals(Material.WRITTEN_BOOK) || this.type.getParent().equals(Material.WRITABLE_BOOK)) {
            nbt.put(new ByteTag("resolved", (byte) (this.resolved ? 1 : 0)));
            nbt.put(new IntTag("generation", this.generation));
            if (this.author != null)
                nbt.put(new StringTag("author", this.author));
            if (this.title != null)
                nbt.put(new StringTag("title", this.title));
            temp.clear();
            for (String page : this.pages)
                temp.add(new StringTag("page", page));
            if (!temp.isEmpty())
                nbt.put(new ListTag("pages", temp));
        }
        if (this.type.getParent().equals(Material.SKULL) && this.type.getData() == 3) {
            CompoundTag owner = new CompoundTag("SkullOwner");
            if (this.ownerUUID != null)
                owner.put(new StringTag("Id", this.ownerUUID.toString()));
            if (this.ownerName != null)
                owner.put(new StringTag("Name", this.ownerName));
            CompoundTag properties = new CompoundTag("Properties");
            temp.clear();
            for (Property prop : this.textures) {
                CompoundTag texture = new CompoundTag("texture");
                if (prop.getValue() != null)
                    texture.put(new StringTag("Value", prop.getValue()));
                if (prop.hasSignature())
                    texture.put(new StringTag("Signature", prop.getSignature()));
                temp.add(texture);
            }
            if (!temp.isEmpty())
                properties.put(new ListTag("textures", temp));
            owner.put(properties);
            nbt.put(owner);
        }
        if (this.type.getParent().equals(Material.FIREWORK_CHARGE)) {
            CompoundTag explosion = new CompoundTag("Explosion");//Is this proper tag name and does it matter
            Explosion e = this.explosions.get(0);
            explosion.put(new ByteTag("Flicker", (byte) (e.hasFlicker() ? 1 : 0)));
            explosion.put(new ByteTag("Trail", (byte) (e.hasTrail() ? 1 : 0)));
            explosion.put(new ByteTag("Type", e.getType()));
            explosion.put(new IntArrayTag("Colors", e.getColors()));
            explosion.put(new IntArrayTag("FadeColors", e.getFadeColors()));
            nbt.put(explosion);
        } else if (this.type.getParent().equals(Material.FIREWORKS)) {
            CompoundTag fireworks = new CompoundTag("Fireworks");
            fireworks.put(new ByteTag("Flight", this.flight));
            temp.clear();
            for (Explosion e : this.explosions) {
                CompoundTag explosion = new CompoundTag("Explosion");//Is this proper tag name and does it matter
                explosion.put(new ByteTag("Flicker", (byte) (e.hasFlicker() ? 1 : 0)));
                explosion.put(new ByteTag("Trail", (byte) (e.hasTrail() ? 1 : 0)));
                explosion.put(new ByteTag("Type", e.getType()));
                explosion.put(new IntArrayTag("Colors", e.getColors()));
                explosion.put(new IntArrayTag("FadeColors", e.getFadeColors()));
                temp.add(explosion);
            }
            if (!temp.isEmpty())
                fireworks.put(new ListTag("Explosions", temp));
            nbt.put(fireworks);
        }
        if (this.type.getParent().equals(Material.MAP) || this.type.getParent().equals(Material.FILLED_MAP)) {
            nbt.put(new ByteTag("map_is_scaling", (byte) (this.scaling ? 1 : 0)));
            temp.clear();
            for (Decoration decoration : this.decorations) {
                CompoundTag d = new CompoundTag("Decoration");//Is this proper tag name and does it matter
                d.put(new StringTag("id", decoration.getID()));
                d.put(new ByteTag("type", decoration.getType()));
                d.put(new DoubleTag("x", decoration.getX()));
                d.put(new DoubleTag("z", decoration.getZ()));
                d.put(new DoubleTag("rot", decoration.getRotation()));
                temp.add(d);
            }
            if (!temp.isEmpty())
                nbt.put(new ListTag("Decorations", temp));
        }
        return nbt;
    }

    public org.spacehq.mc.protocol.data.game.entity.metadata.ItemStack getItemStack() {
        CompoundTag nbt = getNBT();
        return nbt == null ? null : new org.spacehq.mc.protocol.data.game.entity.metadata.ItemStack(this.type.getID(), this.amount, this.type.getData(), nbt.getValue().isEmpty() ? null : nbt);
    }
}