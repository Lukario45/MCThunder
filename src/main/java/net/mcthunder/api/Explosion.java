package net.mcthunder.api;

public class Explosion {
    private boolean flicker, trail;
    private int[] colors, fadeColors;
    private byte type;

    public Explosion(boolean flicker, boolean trail, byte type, int[] colors, int[] fadeColors) {
        this.flicker = flicker;
        this.trail = trail;
        this.type = type;
        this.colors = colors;
        this.fadeColors = fadeColors;
    }

    public void setFlicker(boolean flicker) {
        this.flicker = flicker;
    }

    public boolean hasFlicker() {
        return this.flicker;
    }

    public void setTrail(boolean trail) {
        this.trail = trail;
    }

    public boolean hasTrail() {
        return this.trail;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getType() {
        return this.type;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    public int[] getColors() {
        return this.colors;
    }

    public void setFadeColors(int[] fadeColors) {
        this.fadeColors = fadeColors;
    }

    public int[] getFadeColors() {
        return this.fadeColors;
    }
}