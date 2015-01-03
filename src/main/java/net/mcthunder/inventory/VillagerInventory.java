package net.mcthunder.inventory;

import org.spacehq.mc.protocol.data.game.values.window.WindowType;

public class VillagerInventory extends Inventory {

    public VillagerInventory(String name) {
        super(3, name, WindowType.VILLAGER);//TODO: Check actual size
    }
}