
package net.mcthunder.block;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Location;
import net.mcthunder.entity.Player;
import org.spacehq.mc.protocol.data.game.world.block.UpdatedTileType;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerUpdateTileEntityPacket;
import org.spacehq.opennbt.tag.builtin.CompoundTag;

public class Sign {
    private Location location;
    private String[] lines;
    private ServerUpdateTileEntityPacket packet;

    public Sign(Location location, String[] lines) {
        this.location = location;
        this.lines = lines;
        // TODO: 1/30/2017 UPDATE SIGN TO WORK WITH NEW PACKET
        this.packet = new ServerUpdateTileEntityPacket(location.getPosition(), UpdatedTileType.SIGN, new CompoundTag(null));
    }

    public void sendPacket() {
        for (Player p : MCThunder.getPlayers())
            if (p.getWorld().equals(this.location.getWorld()))
                p.sendPacket(this.packet);
    }

    public void updateSign(String[] lines) {
        this.lines = lines;
        this.packet = new ServerUpdateTileEntityPacket(this.location.getPosition(), UpdatedTileType.SIGN, new CompoundTag(null));
        sendPacket();
    }

    public ServerUpdateTileEntityPacket getPacket() {
        return this.packet;
    }

    public String[] getLines() {
        return this.lines;
    }
}