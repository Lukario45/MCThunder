package net.mcthunder.inventory;

import net.mcthunder.entity.Player;
import net.mcthunder.material.Material;
import org.spacehq.mc.protocol.data.game.values.window.WindowType;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;

public class PlayerInventory extends Inventory {
    private Player p;

    public PlayerInventory(int size, String name, Player p) {
        super(size, name, WindowType.GENERIC_INVENTORY);
        this.p = p;
    }

    public void add(ItemStack is) {
        for (int i = 36; i < 44; i++)
            if (this.contents.get(i) == null || this.contents.get(i).getType().equals(Material.AIR)) {
                setSlot(i, is);
                return;
            } else {//if (this.contents.get(i).canStack(is))
                //combine them
            }
        for (int i = 9; i < 35; i++)
            if (this.contents.get(i) == null || this.contents.get(i).getType().equals(Material.AIR)) {
                setSlot(i, is);
                break;
            } else {//if (this.contents.get(i).canStack(is))
                //combine them
            }
    }

    public void setSlot(int slot, ItemStack i) {
        this.contents.put(slot, i == null ? new ItemStack(Material.AIR) : i);
        this.p.sendPacket(new ServerSetSlotPacket(0, slot, i.getIS()));
    }
    //0 = craft output
    //1-4 = craft input
    //5-8 = helm to boots
    //9-35 = inventory
    //36-44 = hotbar
}