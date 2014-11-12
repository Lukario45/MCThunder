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
    File playerFile;
    public void checkPlayer(Player player) {
        String playerID = player.getUniqueID().toString();
        String playerName = player.getName();
        tellConsole(LoggingLevel.DEBUG, playerName + " has an ID of " + playerID);
        playerFile = new File("PlayerFiles", playerID + ".dat");
        if (!playerFile.exists()) {
            tellConsole(LoggingLevel.DEBUG, "Player: " + playerName + "'s file does not exist yet, creating file!");
            try {
                playerFile.createNewFile();

                Map<String, Tag> map = new HashMap<String, Tag>();
                StringTag worldName = new StringTag("World", player.getWorld().getName());
                map.put(worldName.getName(), worldName);
                IntTag dim = new IntTag("Dimension", 0);
                map.put(dim.getName(), dim);
                DoubleTag x = new DoubleTag("X", player.getLocation().getX());
                map.put(x.getName(), x);
                DoubleTag y = new DoubleTag("Y", player.getLocation().getY());
                map.put(y.getName(), y);
                DoubleTag z = new DoubleTag("Z", player.getLocation().getZ());
                map.put(z.getName(), z);
                FloatTag pitch = new FloatTag("Pitch", player.getLocation().getPitch());
                map.put(pitch.getName(), pitch);
                FloatTag yaw = new FloatTag("Yaw", player.getLocation().getYaw());
                map.put(yaw.getName(), yaw);
                CompoundTag spawnPosition = new CompoundTag("SpawnPosition", map);
                addCompundAttribute(player, spawnPosition);



            } catch (IOException e) {
                e.printStackTrace();
            }
            MCThunder.broadcast("&bWelcome &6" + playerName + " &bto the server!");
        }
    }

    public boolean tagExists(Player p, String name) {
        try {
            CompoundTag player = NBTIO.readFile(p.getPlayerFile());
            Tag t = player.get(name);
            if (t == null) {
                return false;
            } else {
                return true;
            }

        } catch (IOException e) {

            return false;
        }


    }

    public void addAttribute(Player player, String attribute, byte attributeData) {//Byte Tag
        if (tagExists(player, attribute)) {
            tellConsole(LoggingLevel.ERROR, "Someting tried to make multiple attributes in player file!");
            return;
        }
        ByteTag tag = new ByteTag(attribute, attributeData);
        player.addTagToMap(attribute, tag);
        CompoundTag compundTag = new CompoundTag("Player", player.getTagMap());
        try {
            NBTIO.writeFile(compundTag, player.getPlayerFile());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addAttribute(Player player, String attribute, byte[] attributeData) {//Byte Array Tag
        if (tagExists(player, attribute)) {
            tellConsole(LoggingLevel.ERROR, "Someting tried to make multiple attributes in player file!");
            return;
        }
        ByteArrayTag tag = new ByteArrayTag(attribute, attributeData);
        player.addTagToMap(attribute, tag);
        CompoundTag compundTag = new CompoundTag("Player", player.getTagMap());
        try {
            NBTIO.writeFile(compundTag, player.getPlayerFile());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addCompundAttribute(Player player, CompoundTag c) {
        if (tagExists(player, c.getName())) {
            tellConsole(LoggingLevel.ERROR, "Someting tried to make multiple attributes in player file!");
            return;
        }
        //CompoundTag tag = new CompoundTag(attribute);
        player.addTagToMap(c.getName(), c);
        CompoundTag compundTag = new CompoundTag("Player", player.getTagMap());
        try {
            NBTIO.writeFile(compundTag, player.getPlayerFile());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addAttribute(Player player, String attribute) {//Compund Tag
        if (tagExists(player, attribute)) {
            tellConsole(LoggingLevel.ERROR, "Someting tried to make multiple attributes in player file!");
            return;
        }
        CompoundTag tag = new CompoundTag(attribute);
        player.addTagToMap(attribute, tag);
        CompoundTag compundTag = new CompoundTag("Player", player.getTagMap());
        try {
            NBTIO.writeFile(compundTag, player.getPlayerFile());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addAttribute(Player player, String attribute, double attributeData) {//Double Tag
        if (tagExists(player, attribute)) {
            tellConsole(LoggingLevel.ERROR, "Someting tried to make multiple attributes in player file!");
            return;
        }
        DoubleTag tag = new DoubleTag(attribute, attributeData);
        player.addTagToMap(attribute, tag);
        CompoundTag compundTag = new CompoundTag("Player", player.getTagMap());
        try {
            NBTIO.writeFile(compundTag, player.getPlayerFile());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addAttribute(Player player, String attribute, float attributeData) {//Float Tag
        if (tagExists(player, attribute)) {
            tellConsole(LoggingLevel.ERROR, "Someting tried to make multiple attributes in player file!");
            return;
        }
        FloatTag tag = new FloatTag(attribute, attributeData);
        player.addTagToMap(attribute, tag);
        CompoundTag compundTag = new CompoundTag("Player", player.getTagMap());
        try {
            NBTIO.writeFile(compundTag, player.getPlayerFile());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addAttribute(Player player, String attribute, int[] attributeData) {//Int Array Tag
        if (tagExists(player, attribute)) {
            tellConsole(LoggingLevel.ERROR, "Someting tried to make multiple attributes in player file!");
            return;
        }
        IntArrayTag tag = new IntArrayTag(attribute, attributeData);
        player.addTagToMap(attribute, tag);
        CompoundTag compundTag = new CompoundTag("Player", player.getTagMap());
        try {
            NBTIO.writeFile(compundTag, player.getPlayerFile());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addAttribute(Player player, String attribute, int attributeData) {//Int Tag
        if (tagExists(player, attribute)) {
            tellConsole(LoggingLevel.ERROR, "Someting tried to make multiple attributes in player file!");
            return;
        }
        IntTag tag = new IntTag(attribute, attributeData);
        player.addTagToMap(attribute, tag);
        CompoundTag compundTag = new CompoundTag("Player", player.getTagMap());
        try {
            NBTIO.writeFile(compundTag, player.getPlayerFile());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addAttribute(Player player, String attribute, List attributeData) {//List Tag
        if (tagExists(player, attribute)) {
            tellConsole(LoggingLevel.ERROR, "Someting tried to make multiple attributes in player file!");
            return;
        }
        ListTag tag = new ListTag(attribute, attributeData);
        player.addTagToMap(attribute, tag);
        CompoundTag compundTag = new CompoundTag("Player", player.getTagMap());
        try {
            NBTIO.writeFile(compundTag, player.getPlayerFile());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addAttribute(Player player, String attribute, short attributeData) {//Short tag
        if (tagExists(player, attribute)) {
            tellConsole(LoggingLevel.ERROR, "Someting tried to make multiple attributes in player file!");
            return;
        }
        ShortTag tag = new ShortTag(attribute, attributeData);
        player.addTagToMap(attribute, tag);
        CompoundTag compundTag = new CompoundTag("Player", player.getTagMap());
        try {
            NBTIO.writeFile(compundTag, player.getPlayerFile());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addAttribute(Player player, String attribute, String attributeData) {//String Tag
        if (tagExists(player, attribute)) {
            tellConsole(LoggingLevel.ERROR, "Someting tried to make multiple attributes in player file!");
            return;
        }
        StringTag tag = new StringTag(attribute, attributeData);
        player.addTagToMap(attribute, tag);
        CompoundTag compundTag = new CompoundTag("Player", player.getTagMap());
        try {
            NBTIO.writeFile(compundTag, player.getPlayerFile());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void deleteAttribute(Player player, String attribute) {

    }

    public void changeCompundAttribute(Player player, CompoundTag c) {
        //CompoundTag tag = new CompoundTag(attribute);
        player.addTagToMap(c.getName(), c);
        CompoundTag compundTag = new CompoundTag("Player", player.getTagMap());
        try {
            NBTIO.writeFile(compundTag, player.getPlayerFile());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeAttribute(Player player, String attribute, byte attributeData) {//Byte Tag
        ByteTag tag = new ByteTag(attribute, attributeData);
        player.addTagToMap(attribute, tag);
        CompoundTag compundTag = new CompoundTag("Player", player.getTagMap());
        try {
            NBTIO.writeFile(compundTag, player.getPlayerFile());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeAttribute(Player player, String attribute, byte[] attributeData) {//Byte Array Tag
        ByteArrayTag tag = new ByteArrayTag(attribute, attributeData);
        player.addTagToMap(attribute, tag);
        CompoundTag compundTag = new CompoundTag("Player", player.getTagMap());
        try {
            NBTIO.writeFile(compundTag, player.getPlayerFile());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void changeAttribute(Player player, String attribute, double attributeData) {//Double Tag
        DoubleTag tag = new DoubleTag(attribute, attributeData);
        player.addTagToMap(attribute, tag);
        CompoundTag compundTag = new CompoundTag("Player", player.getTagMap());
        try {
            NBTIO.writeFile(compundTag, player.getPlayerFile());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeAttribute(Player player, String attribute, float attributeData) {//Float Tag
        FloatTag tag = new FloatTag(attribute, attributeData);
        player.addTagToMap(attribute, tag);
        CompoundTag compundTag = new CompoundTag("Player", player.getTagMap());
        try {
            NBTIO.writeFile(compundTag, player.getPlayerFile());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeAttribute(Player player, String attribute, int[] attributeData) {//Int Array Tag
        IntArrayTag tag = new IntArrayTag(attribute, attributeData);
        player.addTagToMap(attribute, tag);
        CompoundTag compundTag = new CompoundTag("Player", player.getTagMap());
        try {
            NBTIO.writeFile(compundTag, player.getPlayerFile());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeAttribute(Player player, String attribute, int attributeData) {//Int Tag
        IntTag tag = new IntTag(attribute, attributeData);
        player.addTagToMap(attribute, tag);
        CompoundTag compundTag = new CompoundTag("Player", player.getTagMap());
        try {
            NBTIO.writeFile(compundTag, player.getPlayerFile());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeAttribute(Player player, String attribute, List attributeData) {//List Tag
        ListTag tag = new ListTag(attribute, attributeData);
        player.addTagToMap(attribute, tag);
        CompoundTag compundTag = new CompoundTag("Player", player.getTagMap());
        try {
            NBTIO.writeFile(compundTag, player.getPlayerFile());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeAttribute(Player player, String attribute, short attributeData) {//
        ShortTag tag = new ShortTag(attribute, attributeData);
        player.addTagToMap(attribute, tag);
        CompoundTag compundTag = new CompoundTag("Player", player.getTagMap());
        try {
            NBTIO.writeFile(compundTag, player.getPlayerFile());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeAttribute(Player player, String attribute, String attributeData) {//String Tag
        StringTag tag = new StringTag(attribute, attributeData);
        player.addTagToMap(attribute, tag);
        CompoundTag compundTag = new CompoundTag("Player", player.getTagMap());
        try {
            NBTIO.writeFile(compundTag, player.getPlayerFile());

        } catch (IOException e) {
            e.printStackTrace();

        }

    }


    public Tag getAttribute(Player p, String attribute) {
        if (!tagExists(p, attribute)) {
            tellConsole(LoggingLevel.ERROR, "Something tried to get a nonexistant attribute from player file: " + attribute);
            return null;
        }
        try {
            CompoundTag player = NBTIO.readFile(p.getPlayerFile());
            Tag tag = player.get(attribute);
            return tag;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }



    }


}