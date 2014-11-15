package net.mcthunder.handlers;

import com.Lukario45.NBTFile.NBTFile;
import net.mcthunder.MCThunder;
import net.mcthunder.api.LoggingLevel;
import net.mcthunder.api.Player;
import org.spacehq.opennbt.tag.builtin.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.Lukario45.NBTFile.Utilities.makeStringTag;
import static net.mcthunder.api.Utils.tellConsole;


/**
 * Created by Kevin on 10/13/2014.
 */
public class PlayerProfileHandler {
    public void checkPlayer(Player player) {
        tellConsole(LoggingLevel.DEBUG, player.getName() + " has an ID of " + player.getUniqueID().toString());
        File playerFile = new File("PlayerFiles", player.getUniqueID().toString() + ".dat");
        NBTFile playerNBTFile = new NBTFile(playerFile, "Player");
        if (!playerFile.exists()) {
            tellConsole(LoggingLevel.DEBUG, "Player: " + player.getName() + "'s file does not exist yet, creating file!");
            try {
                playerNBTFile.createFile();

                Map<String, Tag> map = new HashMap<>();
                map.put("World", new StringTag("World", player.getWorld().getName()));
                map.put("Dimension", new IntTag("Dimension", player.getWorld().getDimension()));
                map.put("X", new DoubleTag("X", player.getLocation().getX()));
                map.put("Y", new DoubleTag("Y", player.getLocation().getY()));
                map.put("Z", new DoubleTag("Z", player.getLocation().getZ()));
                map.put("Yaw", new FloatTag("Yaw", player.getLocation().getYaw()));
                map.put("Pitch", new FloatTag("Pitch", player.getLocation().getPitch()));
                addAttribute(player, new CompoundTag("SpawnPosition", map));
            } catch (IOException e) {
                e.printStackTrace();
            }
            MCThunder.broadcast("&bWelcome &6" + player.getName() + " &bto the server!");
        }
    }

    public boolean tagExists(Player p, Tag t) {
        try {
            return p.getPlayerFile().read(t.getName()) != null;
        } catch (IOException ignored) { }
        return false;
    }

    public void addAttribute(Player player, Tag t) {
        if (tagExists(player, t)) {
            tellConsole(LoggingLevel.WARNING, "Something tried to unintentionally overwriting an existing tag");
        } else {
            try {
                player.getPlayerFile().write(t);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void deleteAttribute(Player player, Tag t) {
        try {
            player.getPlayerFile().write(new CompoundTag(t.getName(), null));
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
        if (!tagExists(p, makeStringTag(t, null)))
            tellConsole(LoggingLevel.ERROR, "Something tried to get a nonexistant attribute from player file: " + t);
        else
            try {
                return p.getPlayerFile().read(t);
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }
}