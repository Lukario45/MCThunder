package net.mcthunder.inventory;

import net.mcthunder.block.Material;
import net.mcthunder.entity.Player;
import org.spacehq.mc.protocol.data.game.values.window.WindowType;
import org.spacehq.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;
import org.spacehq.opennbt.tag.builtin.*;

import java.util.ArrayList;

public class PlayerInventory extends Inventory {
    private Player p;

    public PlayerInventory(String name, Player p) {
        super(45, name, WindowType.GENERIC_INVENTORY);
        this.p = p;
    }

    public void add(ItemStack is) {
        for (int i = 36; i < this.size; i++)
            if (this.contents.get(i) == null || this.contents.get(i).getType().equals(Material.AIR)) {
                setSlot(i, is);
                return;
            } else if (this.contents.get(i).canStack(is)) {
                this.contents.get(i).setAmount(this.contents.get(i).getAmount() + is.getAmount());
                this.p.sendPacket(new ServerSetSlotPacket(0, i, this.contents.get(i).getItemStack()));
                return;
            }
        for (int i = 9; i < 35; i++)
            if (this.contents.get(i) == null || this.contents.get(i).getType().equals(Material.AIR)) {
                setSlot(i, is);
                break;
            } else if (this.contents.get(i).canStack(is)) {
                this.contents.get(i).setAmount(this.contents.get(i).getAmount() + is.getAmount());
                this.p.sendPacket(new ServerSetSlotPacket(0, i, this.contents.get(i).getItemStack()));
                break;
            }
    }

    public void setItems(ListTag items) {
        if (items == null)
            return;
        for (int i = 0; i < items.size(); i++) {
            CompoundTag item = items.get(i);
            if (item != null) {
                ByteTag slot = item.get("Slot");
                int id;
                try {
                    id = Material.fromString(((StringTag) item.get("id")).getValue().split("minecraft:")[1]).getID();
                } catch (Exception e) {//pre 1.8 loading should be ShortTag instead
                    id = Integer.parseInt(((ShortTag) item.get("id")).getValue().toString());
                }
                if (slot == null)
                    add(new ItemStack(Material.fromData(id, ((ShortTag) item.get("Damage")).getValue()), ((ByteTag) item.get("Count")).getValue(), (CompoundTag) item.get("tag")));
                else {
                    int s = slot.getValue();
                    if (s < 9)
                        s += 36;
                    else if (s == 100)
                        s = 8;
                    else if (s == 101)
                        s = 7;
                    else if (s == 102)
                        s = 6;
                    else if (s == 103)
                        s = 5;
                    setSlot(s, new ItemStack(Material.fromData(id, ((ShortTag) item.get("Damage")).getValue()), ((ByteTag) item.get("Count")).getValue(), (CompoundTag) item.get("tag")));
                }
            }
        }
    }

    public ListTag getItemList(String tagName) {
        if (tagName == null)
            return null;
        ArrayList<Tag> items = new ArrayList<>();
        for (int i = 0; i < this.contents.size(); i++) {
            ItemStack is = getItemAt(i);
            if (is == null || is.getType().equals(Material.AIR))
                continue;
            CompoundTag item = new CompoundTag("Item");
            int s = i;
            if (s == 5)
                s = 103;
            else if (s == 6)
                s = 102;
            else if (s == 7)
                s = 101;
            else if (s == 8)
                s = 100;
            else if (s > 35)
                s -= 36;
            item.put(new ByteTag("Slot", (byte) s));
            item.put(new StringTag("id", "minecraft:" + is.getType().getParent().getName().toLowerCase()));
            item.put(new ShortTag("Damage", is.getType().getData()));
            item.put(new ByteTag("Count", (byte) is.getAmount()));
            CompoundTag nbt = is.getNBT();
            if (nbt != null)
                item.put(nbt);
            items.add(item);
        }
        return new ListTag(tagName, items);
    }

    public void setSlot(int slot, ItemStack i) {
        this.contents.put(slot, i == null ? new ItemStack(Material.AIR) : i);
        this.p.sendPacket(i == null ? null : new ServerSetSlotPacket(0, slot, i.getItemStack()));
    }
    //0 = craft output
    //1-4 = craft input
    //5-8 = helm to boots
    //9-35 = inventory
    //36-44 = hotbar
}