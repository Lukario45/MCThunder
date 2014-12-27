package net.mcthunder.entity;

import net.mcthunder.api.Location;
import org.spacehq.mc.protocol.data.game.values.entity.Art;
import org.spacehq.mc.protocol.data.game.values.entity.HangingDirection;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPaintingPacket;
import org.spacehq.packetlib.packet.Packet;

public class Painting extends Entity {
    private Art picture;

    public Painting(Location location) {
        super(location);
        this.type = EntityType.PAINTING;
        this.picture = Art.ALBAN;
    }

    @Override
    public Packet getPacket() {
        return new ServerSpawnPaintingPacket(this.entityID, this.picture, this.location.getPosition(), HangingDirection.EAST);
    }

    public void setPicture(Art picture) {
        this.picture = picture;
    }

    public Art getPicture() {
        return this.picture;
    }
}