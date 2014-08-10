package net.mcthunder.packet.ingame.client.window;

import net.mcthunder.game.essentials.ItemStack;
import net.mcthunder.packet.essentials.NetIn;
import net.mcthunder.packet.essentials.NetOut;
import net.mcthunder.packet.essentials.NetUtil;
import net.mcthunder.packet.essentials.Packet;

import java.io.IOException;

public class ClientWindowActionPacket
        implements Packet {
    private int windowId;
    private int slot;
    private ActionParam param;
    private int actionId;
    private Action action;
    private ItemStack clicked;

    private ClientWindowActionPacket() {
    }

    public ClientWindowActionPacket(int windowId, int actionId, int slot, ItemStack clicked, Action action, ActionParam param) {
        this.windowId = windowId;
        this.actionId = actionId;
        this.slot = slot;
        this.clicked = clicked;
        this.action = action;
        this.param = param;
    }

    public int getWindowId() {
        return this.windowId;
    }

    public int getActionId() {
        return this.actionId;
    }

    public int getSlot() {
        return this.slot;
    }

    public ItemStack getClickedItem() {
        return this.clicked;
    }

    public Action getAction() {
        return this.action;
    }

    public ActionParam getParam() {
        return this.param;
    }

    public void read(NetIn in) throws IOException {
        this.windowId = in.readByte();
        this.slot = in.readShort();
        byte param = in.readByte();
        this.actionId = in.readShort();
        byte id = in.readByte();
        this.action = Action.values()[id];
        this.clicked = NetUtil.readItem(in);
        this.param = valueToParam(param);
    }

    public void write(NetOut out) throws IOException {
        out.writeByte(this.windowId);
        out.writeShort(this.slot);
        out.writeByte(paramToValue(this.param));
        out.writeShort(this.actionId);
        out.writeByte(this.action.ordinal());
        NetUtil.writeItem(out, this.clicked);
    }

    public boolean isPriority() {
        return false;
    }

    private byte paramToValue(ActionParam param) throws IOException {
        if (param == ClickItemParam.LEFT_CLICK)
            return 0;
        if (param == ClickItemParam.RIGHT_CLICK) {
            return 1;
        }

        if (param == ShiftClickItemParam.LEFT_CLICK)
            return 0;
        if (param == ShiftClickItemParam.RIGHT_CLICK) {
            return 1;
        }

        if (param == MoveToHotbarParam.SLOT_1)
            return 0;
        if (param == MoveToHotbarParam.SLOT_2)
            return 1;
        if (param == MoveToHotbarParam.SLOT_3)
            return 2;
        if (param == MoveToHotbarParam.SLOT_4)
            return 3;
        if (param == MoveToHotbarParam.SLOT_5)
            return 4;
        if (param == MoveToHotbarParam.SLOT_6)
            return 5;
        if (param == MoveToHotbarParam.SLOT_7)
            return 6;
        if (param == MoveToHotbarParam.SLOT_8)
            return 7;
        if (param == MoveToHotbarParam.SLOT_9) {
            return 8;
        }

        if (param == CreativeGrabParam.GRAB) {
            return 2;
        }

        if (param == DropItemParam.DROP_FROM_SELECTED)
            return 0;
        if (param == DropItemParam.DROP_SELECTED_STACK)
            return 1;
        if (param == DropItemParam.LEFT_CLICK_OUTSIDE_NOT_HOLDING)
            return 0;
        if (param == DropItemParam.RIGHT_CLICK_OUTSIDE_NOT_HOLDING) {
            return 1;
        }

        if (param == SpreadItemParam.LEFT_MOUSE_BEGIN_DRAG)
            return 0;
        if (param == SpreadItemParam.LEFT_MOUSE_ADD_SLOT)
            return 1;
        if (param == SpreadItemParam.LEFT_MOUSE_END_DRAG)
            return 2;
        if (param == SpreadItemParam.RIGHT_MOUSE_BEGIN_DRAG)
            return 4;
        if (param == SpreadItemParam.RIGHT_MOUSE_ADD_SLOT)
            return 5;
        if (param == SpreadItemParam.RIGHT_MOUSE_END_DRAG) {
            return 6;
        }

        if (param == FillStackParam.FILL) {
            return 0;
        }

        throw new IOException("Unmapped action param: " + param);
    }

    private ActionParam valueToParam(byte value) throws IOException {
        if (this.action == Action.CLICK_ITEM) {
            if (value == 0)
                return ClickItemParam.LEFT_CLICK;
            if (value == 1) {
                return ClickItemParam.RIGHT_CLICK;
            }
        }

        if (this.action == Action.SHIFT_CLICK_ITEM) {
            if (value == 0)
                return ShiftClickItemParam.LEFT_CLICK;
            if (value == 1) {
                return ShiftClickItemParam.RIGHT_CLICK;
            }
        }

        if (this.action == Action.MOVE_TO_HOTBAR_SLOT) {
            if (value == 0)
                return MoveToHotbarParam.SLOT_1;
            if (value == 1)
                return MoveToHotbarParam.SLOT_2;
            if (value == 2)
                return MoveToHotbarParam.SLOT_3;
            if (value == 3)
                return MoveToHotbarParam.SLOT_4;
            if (value == 4)
                return MoveToHotbarParam.SLOT_5;
            if (value == 5)
                return MoveToHotbarParam.SLOT_6;
            if (value == 6)
                return MoveToHotbarParam.SLOT_7;
            if (value == 7)
                return MoveToHotbarParam.SLOT_8;
            if (value == 8) {
                return MoveToHotbarParam.SLOT_9;
            }
        }

        if ((this.action == Action.CREATIVE_GRAB_MAX_STACK) &&
                (value == 2)) {
            return CreativeGrabParam.GRAB;
        }

        if (this.action == Action.DROP_ITEM) {
            if (this.slot == -999) {
                if (value == 0)
                    return DropItemParam.LEFT_CLICK_OUTSIDE_NOT_HOLDING;
                if (value == 1)
                    return DropItemParam.RIGHT_CLICK_OUTSIDE_NOT_HOLDING;
            } else {
                if (value == 0)
                    return DropItemParam.DROP_FROM_SELECTED;
                if (value == 1) {
                    return DropItemParam.DROP_SELECTED_STACK;
                }
            }
        }

        if (this.action == Action.SPREAD_ITEM) {
            if (value == 0)
                return SpreadItemParam.LEFT_MOUSE_BEGIN_DRAG;
            if (value == 1)
                return SpreadItemParam.LEFT_MOUSE_ADD_SLOT;
            if (value == 2)
                return SpreadItemParam.LEFT_MOUSE_END_DRAG;
            if (value == 4)
                return SpreadItemParam.RIGHT_MOUSE_BEGIN_DRAG;
            if (value == 5)
                return SpreadItemParam.RIGHT_MOUSE_ADD_SLOT;
            if (value == 6) {
                return SpreadItemParam.RIGHT_MOUSE_END_DRAG;
            }
        }

        if ((this.action == Action.FILL_STACK) &&
                (value == 0)) {
            return FillStackParam.FILL;
        }

        throw new IOException("Unknown action param value: " + value);
    }

    public static enum FillStackParam
            implements ClientWindowActionPacket.ActionParam {
        FILL;
    }

    public static enum SpreadItemParam
            implements ClientWindowActionPacket.ActionParam {
        LEFT_MOUSE_BEGIN_DRAG,
        LEFT_MOUSE_ADD_SLOT,
        LEFT_MOUSE_END_DRAG,
        RIGHT_MOUSE_BEGIN_DRAG,
        RIGHT_MOUSE_ADD_SLOT,
        RIGHT_MOUSE_END_DRAG;
    }

    public static enum DropItemParam
            implements ClientWindowActionPacket.ActionParam {
        DROP_FROM_SELECTED,
        DROP_SELECTED_STACK,
        LEFT_CLICK_OUTSIDE_NOT_HOLDING,
        RIGHT_CLICK_OUTSIDE_NOT_HOLDING;
    }

    public static enum CreativeGrabParam
            implements ClientWindowActionPacket.ActionParam {
        GRAB;
    }

    public static enum MoveToHotbarParam
            implements ClientWindowActionPacket.ActionParam {
        SLOT_1,
        SLOT_2,
        SLOT_3,
        SLOT_4,
        SLOT_5,
        SLOT_6,
        SLOT_7,
        SLOT_8,
        SLOT_9;
    }

    public static enum ShiftClickItemParam
            implements ClientWindowActionPacket.ActionParam {
        LEFT_CLICK,
        RIGHT_CLICK;
    }

    public static enum ClickItemParam
            implements ClientWindowActionPacket.ActionParam {
        LEFT_CLICK,
        RIGHT_CLICK;
    }

    public static enum Action {
        CLICK_ITEM,
        SHIFT_CLICK_ITEM,
        MOVE_TO_HOTBAR_SLOT,
        CREATIVE_GRAB_MAX_STACK,
        DROP_ITEM,
        SPREAD_ITEM,
        FILL_STACK;
    }

    public static abstract interface ActionParam {
    }
}