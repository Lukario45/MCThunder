package net.mcthunder.packet.ingame.server.world;

import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ServerNotifyClientPacket
        implements Packet {
    private Notification notification;
    private NotificationValue value;

    private ServerNotifyClientPacket() {
    }

    public ServerNotifyClientPacket(Notification notification, NotificationValue value) {
        this.notification = notification;
        this.value = value;
    }

    public Notification getNotification() {
        return this.notification;
    }

    public NotificationValue getValue() {
        return this.value;
    }

    public void read(NetIn in) throws IOException {
        this.notification = Notification.values()[in.readUnsignedByte()];
        this.value = floatToValue(in.readFloat());
    }

    public void write(NetOut out) throws IOException {
        out.writeByte(this.notification.ordinal());
        out.writeFloat(valueToFloat(this.value));
    }

    public boolean isPriority() {
        return false;
    }

    private NotificationValue floatToValue(float f) {
        if (this.notification == Notification.CHANGE_GAMEMODE) {
            if (f == 0.0F)
                return GameModeValue.SURVIVAL;
            if (f == 1.0F)
                return GameModeValue.CREATIVE;
            if (f == 2.0F)
                return GameModeValue.ADVENTURE;
        } else if (this.notification == Notification.DEMO_MESSAGE) {
            if (f == 0.0F)
                return DemoMessageValue.WELCOME;
            if (f == 101.0F)
                return DemoMessageValue.MOVEMENT_CONTROLS;
            if (f == 102.0F)
                return DemoMessageValue.JUMP_CONTROL;
            if (f == 103.0F)
                return DemoMessageValue.INVENTORY_CONTROL;
        } else {
            if (this.notification == Notification.RAIN_STRENGTH)
                return new RainStrengthValue((int) f);
            if (this.notification == Notification.THUNDER_STRENGTH) {
                return new ThunderStrengthValue((int) f);
            }
        }
        return null;
    }

    private float valueToFloat(NotificationValue value) {
        if (value == GameModeValue.SURVIVAL)
            return 0.0F;
        if (value == GameModeValue.CREATIVE)
            return 1.0F;
        if (value == GameModeValue.ADVENTURE) {
            return 2.0F;
        }

        if (value == DemoMessageValue.WELCOME)
            return 0.0F;
        if (value == DemoMessageValue.MOVEMENT_CONTROLS)
            return 101.0F;
        if (value == DemoMessageValue.JUMP_CONTROL)
            return 102.0F;
        if (value == DemoMessageValue.INVENTORY_CONTROL) {
            return 103.0F;
        }

        if ((value instanceof RainStrengthValue)) {
            return ((RainStrengthValue) value).getStrength();
        }

        if ((value instanceof ThunderStrengthValue)) {
            return ((ThunderStrengthValue) value).getStrength();
        }

        return 0.0F;
    }

    public static enum DemoMessageValue
            implements ServerNotifyClientPacket.NotificationValue {
        WELCOME,
        MOVEMENT_CONTROLS,
        JUMP_CONTROL,
        INVENTORY_CONTROL;
    }

    public static enum GameModeValue
            implements ServerNotifyClientPacket.NotificationValue {
        SURVIVAL,
        CREATIVE,
        ADVENTURE;
    }

    public static enum Notification {
        INVALID_BED,
        START_RAIN,
        STOP_RAIN,
        CHANGE_GAMEMODE,
        ENTER_CREDITS,
        DEMO_MESSAGE,
        ARROW_HIT_PLAYER,
        RAIN_STRENGTH,
        THUNDER_STRENGTH;
    }

    public static abstract interface NotificationValue {
    }

    public static class ThunderStrengthValue
            implements ServerNotifyClientPacket.NotificationValue {
        private float strength;

        public ThunderStrengthValue(float strength) {
            if (strength > 1.0F) {
                strength = 1.0F;
            }

            if (strength < 0.0F) {
                strength = 0.0F;
            }

            this.strength = strength;
        }

        public float getStrength() {
            return this.strength;
        }
    }

    public static class RainStrengthValue
            implements ServerNotifyClientPacket.NotificationValue {
        private float strength;

        public RainStrengthValue(float strength) {
            if (strength > 1.0F) {
                strength = 1.0F;
            }

            if (strength < 0.0F) {
                strength = 0.0F;
            }

            this.strength = strength;
        }

        public float getStrength() {
            return this.strength;
        }
    }
}