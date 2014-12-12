package net.mcthunder.inventory;

import org.spacehq.mc.protocol.data.game.values.window.WindowType;

public class BrewingStandInventory extends Inventory {
    public BrewingStandInventory(String name) {
        super(4, name, WindowType.BREWING_STAND);
    }
}