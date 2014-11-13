package net.mcthunder.handlers;

import net.mcthunder.MCThunder;
import net.mcthunder.api.LoggingLevel;
import net.mcthunder.api.Player;
import org.spacehq.opennbt.NBTIO;
import org.spacehq.opennbt.tag.builtin.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.mcthunder.api.Utils.tellConsole;


/**
 * Created by Kevin on 10/13/2014.
 */
public class PlayerProfileHandler {
    public void checkPlayer(Player player) {
        tellConsole(LoggingLevel.DEBUG, player.getName() + " has an ID of " + player.getUniqueID().toString());
        File playerFile = new File("PlayerFiles", player.getUniqueID().toString() + ".dat");
        if (!playerFile.exists()) {
            tellConsole(LoggingLevel.DEBUG, "Player: " + player.getName() + "'s file does not exist yet, creating file!");
            try {
                playerFile.createNewFile();
                player.writePlayerTag();
                Map<String, Tag> map = new HashMap<>();
                map.put("World", new StringTag("World", player.getWorld().getName()));
                map.put("Dimension", new IntTag("Dimension", player.getWorld().getDimension()));
                map.put("X", new DoubleTag("X", player.getLocation().getX()));
                map.put("Y", new DoubleTag("Y", player.getLocation().getY()));
                map.put("Z", new DoubleTag("Z", player.getLocation().getZ()));
                map.put("Yaw", new FloatTag("Yaw", player.getLocation().getYaw()));
                map.put("Pitch", new FloatTag("Pitch", player.getLocation().getPitch()));
                addCompundAttribute(player, new CompoundTag("SpawnPosition", map));
            } catch (IOException e) {
                e.printStackTrace();
            }
            MCThunder.broadcast("&bWelcome &6" + player.getName() + " &bto the server!");
        }
    }

    public boolean tagExists(Player p, String name) {
        try {
            return NBTIO.readFile(p.getPlayerFile()).get(name) != null;
        } catch (IOException ignored) { }
        return false;
    }

    public void addAttribute(Player player, String attribute, byte attributeData) {//Byte Tag
        if (tagExists(player, attribute))
            tellConsole(LoggingLevel.ERROR, "Someting tried to make multiple attributes in player file!");
        else
            player.addTagToMap(attribute, new ByteTag(attribute, attributeData));
    }

    public void addAttribute(Player player, String attribute, byte[] attributeData) {//Byte Array Tag
        if (tagExists(player, attribute))
            tellConsole(LoggingLevel.ERROR, "Someting tried to make multiple attributes in player file!");
        else
            player.addTagToMap(attribute, new ByteArrayTag(attribute, attributeData));
    }

    public void addCompundAttribute(Player player, CompoundTag c) {
        if (tagExists(player, c.getName()))
            tellConsole(LoggingLevel.ERROR, "Someting tried to make multiple attributes in player file!");
        else
            player.addTagToMap(c.getName(), c);
    }

    public void addAttribute(Player player, String attribute) {//Compund Tag
        if (tagExists(player, attribute))
            tellConsole(LoggingLevel.ERROR, "Someting tried to make multiple attributes in player file!");
        else
            player.addTagToMap(attribute, new CompoundTag(attribute));
    }

    public void addAttribute(Player player, String attribute, double attributeData) {//Double Tag
        if (tagExists(player, attribute))
            tellConsole(LoggingLevel.ERROR, "Someting tried to make multiple attributes in player file!");
        else
            player.addTagToMap(attribute, new DoubleTag(attribute, attributeData));
    }

    public void addAttribute(Player player, String attribute, float attributeData) {//Float Tag
        if (tagExists(player, attribute))
            tellConsole(LoggingLevel.ERROR, "Someting tried to make multiple attributes in player file!");
        else
            player.addTagToMap(attribute, new FloatTag(attribute, attributeData));
    }

    public void addAttribute(Player player, String attribute, int[] attributeData) {//Int Array Tag
        if (tagExists(player, attribute))
            tellConsole(LoggingLevel.ERROR, "Someting tried to make multiple attributes in player file!");
        else
            player.addTagToMap(attribute, new IntArrayTag(attribute, attributeData));
    }

    public void addAttribute(Player player, String attribute, int attributeData) {//Int Tag
        if (tagExists(player, attribute))
            tellConsole(LoggingLevel.ERROR, "Someting tried to make multiple attributes in player file!");
        else
            player.addTagToMap(attribute, new IntTag(attribute, attributeData));
    }

    public void addAttribute(Player player, String attribute, List attributeData) {//List Tag
        if (tagExists(player, attribute))
            tellConsole(LoggingLevel.ERROR, "Someting tried to make multiple attributes in player file!");
        else
            player.addTagToMap(attribute, new ListTag(attribute, attributeData));
    }

    public void addAttribute(Player player, String attribute, short attributeData) {//Short tag
        if (tagExists(player, attribute))
            tellConsole(LoggingLevel.ERROR, "Someting tried to make multiple attributes in player file!");
        else
            player.addTagToMap(attribute, new ShortTag(attribute, attributeData));
    }

    public void addAttribute(Player player, String attribute, String attributeData) {//String Tag
        if (tagExists(player, attribute))
            tellConsole(LoggingLevel.ERROR, "Someting tried to make multiple attributes in player file!");
        else
            player.addTagToMap(attribute, new StringTag(attribute, attributeData));
    }

    public void deleteAttribute(Player player, String attribute) {

    }

    public void changeCompundAttribute(Player player, CompoundTag c) {
        player.addTagToMap(c.getName(), c);
    }

    public void changeAttribute(Player player, String attribute, byte attributeData) {//Byte Tag
        player.addTagToMap(attribute, new ByteTag(attribute, attributeData));
    }

    public void changeAttribute(Player player, String attribute, byte[] attributeData) {//Byte Array Tag
        player.addTagToMap(attribute, new ByteArrayTag(attribute, attributeData));
    }
    public void changeAttribute(Player player, String attribute, double attributeData) {//Double Tag
        player.addTagToMap(attribute, new DoubleTag(attribute, attributeData));
    }

    public void changeAttribute(Player player, String attribute, float attributeData) {//Float Tag
        player.addTagToMap(attribute, new FloatTag(attribute, attributeData));
    }

    public void changeAttribute(Player player, String attribute, int[] attributeData) {//Int Array Tag
        player.addTagToMap(attribute, new IntArrayTag(attribute, attributeData));
    }

    public void changeAttribute(Player player, String attribute, int attributeData) {//Int Tag
        player.addTagToMap(attribute, new IntTag(attribute, attributeData));
    }

    public void changeAttribute(Player player, String attribute, List attributeData) {//List Tag
        player.addTagToMap(attribute, new ListTag(attribute, attributeData));
    }

    public void changeAttribute(Player player, String attribute, short attributeData) {//Short tag
        player.addTagToMap(attribute, new ShortTag(attribute, attributeData));
    }

    public void changeAttribute(Player player, String attribute, String attributeData) {//String Tag
        player.addTagToMap(attribute, new StringTag(attribute, attributeData));
    }

    public Tag getAttribute(Player p, String attribute) {
        if (!tagExists(p, attribute))
            tellConsole(LoggingLevel.ERROR, "Something tried to get a nonexistant attribute from player file: " + attribute);
        else
            try {
                return NBTIO.readFile(p.getPlayerFile()).get(attribute);
            } catch (IOException e) {
                e.printStackTrace();
            }
        return null;
    }
}