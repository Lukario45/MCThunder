package net.mcthunder.inventory;

import org.spacehq.mc.protocol.data.game.window.WindowType;

public class CraftingInventory extends Inventory {
    public CraftingInventory(String name) {
        //use 0 for empty workbench 3*3 entry + 1 result
        super(0, name, WindowType.CRAFTING_TABLE);
    }
}