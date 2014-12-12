package net.mcthunder.inventory;

import org.spacehq.mc.protocol.data.game.values.window.WindowType;

public class HopperInventory extends Inventory {
    public HopperInventory(String name) {
        super(5, name, WindowType.HOPPER);
    }
}