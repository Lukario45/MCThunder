package net.mcthunder.inventory;

import org.spacehq.mc.protocol.data.game.values.window.WindowType;

public class ChestInventory extends Inventory {
    public ChestInventory(String name) {
        super(27, name, WindowType.CHEST);
    }
}