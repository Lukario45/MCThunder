package net.mcthunder.inventory;

import org.spacehq.mc.protocol.data.game.values.window.WindowType;

public class AnvilInventory extends Inventory {
    public AnvilInventory(String name) {
        //use 0 for empty anvil 2 entry + 1 result
        super(0, name, WindowType.ANVIL);
    }
}