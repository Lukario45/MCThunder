package net.mcthunder.handlers;

import net.mcthunder.MCThunder;
import net.mcthunder.api.LoggingLevel;
import net.mcthunder.entity.Player;
import org.spacehq.opennbt.NBTIO;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.Tag;

import java.io.File;
import java.io.IOException;

import static net.mcthunder.api.Utils.tellConsole;

/**
 * Created by Kevin on 10/13/2014.
 */
public class PlayerProfileHandler {
    public void checkPlayer(Player p) {
        tellConsole(LoggingLevel.INFO, p.getName() + " logged in with an ID of " + p.getUniqueID());
        File file = new File("PlayerFiles", p.getUniqueID() + ".dat");
        if (!file.exists()) {
            //tellConsole(LoggingLevel.DEBUG, "Player: " + p.getName() + "'s file does not exist yet, creating file!");
            try {
                file.createNewFile();
                p.setLocation(MCThunder.getWorld(MCThunder.getConfig().getWorldName()).getSpawnLocation());
                NBTIO.writeFile(p.getNBT(), file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            MCThunder.broadcast("&bWelcome &6" + p.getName() + " &bto the server!");
        }
    }

    public boolean tagExists(Player p, String tagName) {
        try {
            return NBTIO.readFile(new File("PlayerFiles", p.getUniqueID() + ".dat")).get(tagName) != null;
        } catch (IOException ignored) { }
        return false;
    }

    public void addAttribute(Player p, Tag t) {
        if (tagExists(p, t.getName()))
            tellConsole(LoggingLevel.WARNING, "Something tried to unintentionally overwriting an existing tag");
        else
            changeAttribute(p, t);
    }

    public void deleteAttribute(Player p, String tagName) {
        File file = new File("PlayerFiles", p.getUniqueID() + ".dat");
        try {
            CompoundTag tag = NBTIO.readFile(file);
            tag.remove(tagName);
            NBTIO.writeFile(tag, file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void changeAttribute(Player p, Tag t) {
        File file = new File("PlayerFiles", p.getUniqueID() + ".dat");
        try {
            CompoundTag tag = NBTIO.readFile(file);
            tag.put(t);
            NBTIO.writeFile(tag, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Tag getAttribute(Player p, String t) {
        if (tagExists(p, t))//No sense in telling the console if something tried to get a tag that does no exist
            try {
                return NBTIO.readFile(new File("PlayerFiles", p.getUniqueID() + ".dat")).get(t);
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }
}