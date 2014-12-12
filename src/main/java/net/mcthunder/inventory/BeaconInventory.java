package net.mcthunder.inventory;

import org.spacehq.mc.protocol.data.game.values.window.WindowType;

public class BeaconInventory extends Inventory {
    public BeaconInventory(String name) {
        super(1, name, WindowType.BEACON);
    }
}