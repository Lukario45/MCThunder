package net.mcthunder.inventory;

import org.spacehq.mc.protocol.data.game.window.WindowType;

public class FurnaceInventory extends Inventory {
    public FurnaceInventory(String name) {
        super(3, name, WindowType.FURNACE);
    }
}