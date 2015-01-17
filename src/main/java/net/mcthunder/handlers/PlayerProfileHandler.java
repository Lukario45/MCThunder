package net.mcthunder.handlers;

import net.mcthunder.MCThunder;
import net.mcthunder.api.LoggingLevel;
import net.mcthunder.entity.Player;
import org.spacehq.opennbt.NBTIO;
import org.spacehq.opennbt.tag.builtin.*;

import java.io.IOException;
import java.util.Arrays;

import static net.mcthunder.api.Utils.tellConsole;

/**
 * Created by Kevin on 10/13/2014.
 */
public class PlayerProfileHandler {
    public void checkPlayer(Player player) {
        tellConsole(LoggingLevel.INFO, player.getName() + " logged in with an ID of " + player.getUniqueID());
        if (!player.getPlayerFile().getNbtFile().exists()) {
            tellConsole(LoggingLevel.DEBUG, "Player: " + player.getName() + "'s file does not exist yet, creating file!");
            try {
                player.getPlayerFile().createFile();
                //NBTIO.writeFile(player.getNBT(), player.getPlayerFile().getNbtFile());
                //This will be in .getNBT() but until then temporary thing here
                CompoundTag tag = new CompoundTag("");
                ListTag pos = new ListTag("Pos", Arrays.<Tag>asList(new DoubleTag("X", player.getLocation().getX()),
                        new DoubleTag("Y", player.getLocation().getY()), new DoubleTag("Z", player.getLocation().getZ())));
                ListTag rotation = new ListTag("Rotation", Arrays.<Tag>asList(new FloatTag("Yaw", player.getLocation().getYaw()),
                        new FloatTag("Pitch", player.getLocation().getPitch())));
                tag.put(pos);
                tag.put(rotation);
                tag.put(new StringTag("SpawnWorld", player.getWorld().getName()));
                NBTIO.writeFile(tag, player.getPlayerFile().getNbtFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
            MCThunder.broadcast("&bWelcome &6" + player.getName() + " &bto the server!");
        }
    }

    public boolean tagExists(Player p, String tagName) {
        try {
            return NBTIO.readFile(p.getPlayerFile().getNbtFile()).get(tagName) != null;
        } catch (IOException ignored) { }
        return false;
    }

    public void addAttribute(Player player, Tag t) {
        if (tagExists(player, t.getName()))
            tellConsole(LoggingLevel.WARNING, "Something tried to unintentionally overwriting an existing tag");
        else
            try {
                CompoundTag tag = NBTIO.readFile(player.getPlayerFile().getNbtFile());
                tag.put(t);
                NBTIO.writeFile(tag, player.getPlayerFile().getNbtFile());
                //player.getPlayerFile().write(t);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public void deleteAttribute(Player player, String tagName) {
        try {
            player.getPlayerFile().write(new CompoundTag(tagName, null));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void changeAttribute(Player p, Tag t) {
        try {
            p.getPlayerFile().write(t);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Tag getAttribute(Player p, String t) {
        if (!tagExists(p, t))
            tellConsole(LoggingLevel.ERROR, "Something tried to get a nonexistant attribute from player file: " + t);
        else
            try {
                return NBTIO.readFile(p.getPlayerFile().getNbtFile()).get(t);
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }
}