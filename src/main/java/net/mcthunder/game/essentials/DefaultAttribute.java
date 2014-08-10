package net.mcthunder.game.essentials;

public class DefaultAttribute {
    public static final DefaultAttribute MAX_HEALTH = new DefaultAttribute("generic.maxHealth", 20.0D, 0.0D, 1.7976931348623157E+308D);
    public static final DefaultAttribute FOLLOW_RANGE = new DefaultAttribute("generic.followRange", 32.0D, 0.0D, 2048.0D);
    public static final DefaultAttribute KNOCKBACK_RESISTANCE = new DefaultAttribute("generic.knockbackResistance", 0.0D, 0.0D, 1.0D);
    public static final DefaultAttribute MOVEMENT_SPEED = new DefaultAttribute("generic.movementSpeed", 0.699999988079071D, 0.0D, 1.7976931348623157E+308D);
    public static final DefaultAttribute ATTACK_DAMAGE = new DefaultAttribute("generic.attackStrength", 2.0D, 0.0D, 1.7976931348623157E+308D);
    public static final DefaultAttribute HORSE_JUMP_STRENGTH = new DefaultAttribute("generic.maxHealth", 0.7D, 0.0D, 2.0D);
    public static final DefaultAttribute ZOMBIE_SPAWN_REINFORCEMENTS_CHANCE = new DefaultAttribute("generic.maxHealth", 0.0D, 0.0D, 1.0D);
    private String key;
    private double def;
    private double min;
    private double max;

    private DefaultAttribute(String key, double def, double min, double max) {
        this.key = key;
        this.def = def;
        this.min = min;
        this.max = max;
    }

    public String getKey() {
        return this.key;
    }

    public double getDefault() {
        return this.def;
    }

    public double getMin() {
        return this.min;
    }

    public double getMax() {
        return this.max;
    }
}