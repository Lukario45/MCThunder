package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.api.PotionEffect;
import net.mcthunder.api.PotionEffectType;

import java.util.Collection;
import java.util.HashMap;

public abstract class LivingEntity extends Entity {//TODO: set default max healths for each living entity type
    protected HashMap<PotionEffectType, PotionEffect> activeEffects = new HashMap<>();
    private PotionEffectType latest;
    private boolean alwaysShowName, hasAI;
    protected float health, maxHealth;
    private byte arrows;

    public LivingEntity(Location location) {
        super(location);
        this.maxHealth = 20;
        this.latest = null;
        this.metadata.setMetadata(2, this.customName);
        this.metadata.setMetadata(3, (byte) ((this.alwaysShowName = false) ? 1 : 0));
        this.metadata.setMetadata(6, this.health = this.maxHealth);
        this.metadata.setMetadata(7, 0);//No color until a potion is set
        this.metadata.setMetadata(8, (byte) 0);//False until potion effects are set
        this.metadata.setMetadata(9, this.arrows = (byte) 0);
        this.metadata.setMetadata(15, (byte) ((this.hasAI = false) ? 1 : 0));
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
}