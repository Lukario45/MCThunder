package net.mcthunder.entity;

import net.mcthunder.api.Location;
import net.mcthunder.material.Material;
import net.mcthunder.world.World;
import org.spacehq.opennbt.tag.builtin.ByteTag;
import org.spacehq.opennbt.tag.builtin.CompoundTag;
import org.spacehq.opennbt.tag.builtin.IntTag;
import org.spacehq.opennbt.tag.builtin.StringTag;

public class CommandBlockMinecart extends Minecart {
    private String command, lastOutput;
    private boolean trackOutput;
    private int successCount;

    public CommandBlockMinecart(Location location) {
        super(location);
        this.type = EntityType.MINECART_COMMAND_BLOCK;
        this.blockType = Material.COMMAND_BLOCK_MINECART;
        this.successCount = 0;
        this.command = "";
        this.lastOutput = "";
        this.trackOutput = true;
        this.metadata.setMetadata(20, 8978432);
    }

    public CommandBlockMinecart(World w, CompoundTag tag) {
        super(w, tag);
        StringTag command = tag.get("Command");
        this.command = command == null ? "" : command.getValue();
        IntTag successCount = tag.get("SuccessCount");
        this.successCount = successCount == null ? 0 : successCount.getValue();
        StringTag lastOutput = tag.get("LastOutput");
        this.lastOutput = lastOutput == null ? "" : lastOutput.getValue();
        ByteTag trackOutput = tag.get("TrackOutput");//1 true, 0 false
        this.trackOutput = trackOutput != null && trackOutput.getValue() == (byte) 1;
        this.blockType = Material.COMMAND_BLOCK_MINECART;
        this.metadata.setMetadata(20, 8978432);
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getCommand() {
        return this.command;
    }

    public void setLastOutput(String lastOutput) {
        this.lastOutput = lastOutput;
    }

    public String getLastOutput() {
        return this.lastOutput;
    }

    public void setTrackOutput(boolean trackOutput) {
        this.trackOutput = trackOutput;
    }

    public boolean trackOutput() {
        return this.trackOutput;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getSuccessCount() {
        return this.successCount;
    }

    public CompoundTag getNBT() {//TODO: Return the nbt
        CompoundTag nbt = super.getNBT();
        return nbt;
    }
}