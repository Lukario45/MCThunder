package net.mcthunder.inventory;

import org.spacehq.mc.protocol.data.game.window.WindowType;

public class DispenserInventory extends Inventory {
    public DispenserInventory(String name) {
        super(9, name, WindowType.DISPENSER);
    }
}