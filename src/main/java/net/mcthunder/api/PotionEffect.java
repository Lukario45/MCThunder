package net.mcthunder.api;

public class PotionEffect {
    private PotionEffectType type;
    private int duration;
    private int amplifier;

    public PotionEffect(PotionEffectType type, int duration, int amplifier) {
        this.type = type;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    public PotionEffectType getType() {
        return this.type;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getAmplifier() {
        return this.amplifier;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}