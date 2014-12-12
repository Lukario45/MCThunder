package net.mcthunder.inventory;

import org.spacehq.mc.protocol.data.game.values.window.WindowType;

public class PlayerInventory extends Inventory {
    public PlayerInventory(int size, String name) {
        super(size, name, WindowType.GENERIC_INVENTORY);
    }
    //0 = craft output
    //1-4 = craft input
    //5-8 = helm to boots
    //9-35 = inventory
    //36-44 = hotbar
}