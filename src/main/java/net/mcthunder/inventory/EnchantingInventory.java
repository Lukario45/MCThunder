package net.mcthunder.inventory;

import org.spacehq.mc.protocol.data.game.window.WindowType;

public class EnchantingInventory extends Inventory {
    public EnchantingInventory(String name) {
        //use 0 for empty enchantment table 2 entry
        super(0, name, WindowType.ENCHANTING_TABLE);
    }
}
