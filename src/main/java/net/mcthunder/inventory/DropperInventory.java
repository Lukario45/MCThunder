package net.mcthunder.inventory;

import org.spacehq.mc.protocol.data.game.window.WindowType;

public class DropperInventory extends Inventory {
    public DropperInventory(String name) {
        super(9, name, WindowType.DROPPER);
    }
}