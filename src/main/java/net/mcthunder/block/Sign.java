package net.mcthunder.block;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Location;
import net.mcthunder.entity.Player;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerUpdateSignPacket;

public class Sign {
    private Location location;
    private String[] lines;
    private ServerUpdateSignPacket packet;

    public Sign(Location location, String[] lines) {
        this.location = location;
        this.lines = lines;
        this.packet = new ServerUpdateSignPacket(location.getPosition(), getLines());
    }

    public void sendPacket() {
        for (Player p : MCThunder.getPlayers())
            if (p.getWorld().equals(this.location.getWorld()))
                p.sendPacket(this.packet);
    }

    public void updateSign(String[] lines) {
        this.lines = lines;
        this.packet = new ServerUpdateSignPacket(this.location.getPosition(), getLines());
        sendPacket();
    }

    public ServerUpdateSignPacket getPacket() {
        return this.packet;
    }

    public String[] getLines() {
        return this.lines;
    }
}