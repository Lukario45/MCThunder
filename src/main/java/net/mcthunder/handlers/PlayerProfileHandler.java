package net.mcthunder.handlers;

import net.mcthunder.MCThunder;
import net.mcthunder.api.LoggingLevel;
import net.mcthunder.api.Player;
import org.spacehq.opennbt.NBTIO;
import org.spacehq.opennbt.tag.builtin.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
                addAttribute(player, "test", "this is a test");
            } catch (IOException e) {
                e.printStackTrace();
            }
            MCThunder.broadcast("&bWelcome &6" + playerName + " &bto the server!");
        }
    }

    public void addAttribute(Player player, String attribute, byte attributeData) {//Byte Tag
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
        ByteArrayTag tag = new ByteArrayTag(attribute, attributeData);
        player.addTagToMap(attribute, tag);
        CompoundTag compundTag = new CompoundTag("Player", player.getTagMap());
        try {
            NBTIO.writeFile(compundTag, player.getPlayerFile());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addAttribute(Player player, String attribute) {//Compund Tag
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
        ListTag tag = new ListTag(attribute, attributeData);
        player.addTagToMap(attribute, tag);
        CompoundTag compundTag = new CompoundTag("Player", player.getTagMap());
        try {
            NBTIO.writeFile(compundTag, player.getPlayerFile());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addAttribute(Player player, String attribute, short attributeData) {//
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

    public void changeAttribute(Player player, String attribute, String attributeData) {

    }

    public void getAttribute(Player player, String attribute, String attributeData) {

    }

    public void addMultiAttribute(Player player, String attribute, String attributeData) {

    }
}