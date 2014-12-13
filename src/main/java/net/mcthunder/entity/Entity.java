package net.mcthunder.entity;

import net.mcthunder.MCThunder;
import net.mcthunder.api.Location;
import net.mcthunder.api.MetadataMap;
import net.mcthunder.world.World;
import org.spacehq.mc.protocol.data.game.values.entity.Art;
import org.spacehq.mc.protocol.data.game.values.entity.HangingDirection;
import org.spacehq.mc.protocol.data.game.values.entity.MobType;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerDestroyEntitiesPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.ServerEntityTeleportPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnExpOrbPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPaintingPacket;
import org.spacehq.packetlib.packet.Packet;

import java.util.Arrays;
import java.util.Collection;

public class Entity {
    //TODO make one for each type of entity with also their ai and spawning things
    protected final EntityType type;
    protected final int entityID;
    private String customName;
    protected Location location;
    private boolean onGround;
    private short fireTicks;
    private short airLeft;
    protected MetadataMap metadata;

    public Entity(EntityType type) {
        this(null, type);
    }

    public Entity(Location location, EntityType type) {
        this.location = location;
        this.type = type;
        this.entityID = (int) Math.ceil(Math.random() * Integer.MAX_VALUE);
        this.metadata = new MetadataMap();
    }

    public void spawn() {
        //TODO: replace this to send from world
        for (Player p : MCThunder.getPlayers())
            if (p.getWorld().equals(this.location.getWorld()))
                for (Packet packet : getPackets())
                    if (packet != null)
                        p.sendPacket(packet);
    }

    public MetadataMap getMetadata() {
        return this.metadata;
    }

    public int getEntityID() {
        return this.entityID;
    }

    public Packet getPacket() {
        if (this.type == null)
            return null;
        else if (this.type.equals(EntityType.PAINTING))
            return new ServerSpawnPaintingPacket(this.entityID, Art.ALBAN, this.location.getPosition(), HangingDirection.EAST);
        else if (this.type.equals(EntityType.XP_ORB))
            return new ServerSpawnExpOrbPacket(this.entityID, this.location.getX(), this.location.getY(), this.location.getZ(), 0);
        else if (this.type.isCreature())
            return new ServerSpawnMobPacket(this.entityID, MobType.valueOf(this.type.getName()), this.location.getX(), this.location.getY(), this.location.getZ(), this.location.getYaw(), this.location.getPitch(), 0, 0, 0, 0, getMetadata().getMetadataArray());
        return null;
    }

    public Collection<Packet> getPackets() {
        return Arrays.asList(getPacket());
    }

    public Location getLocation() {
        return this.location.clone();
    }

    public void setLocation(Location location) {
        this.location = location.clone();
    }

    public void setX(double x) {
        this.location.setX(x);
    }

    public void setY(double y) {
        this.location.setY(y);
    }

    public void setZ(double z) {
        this.location.setZ(z);
    }

    public void setYaw(float yaw) {
        this.location.setYaw(yaw);
    }

    public void setPitch(float pitch) {
        this.location.setPitch(pitch);
    }

    public World getWorld() {
        return this.location.getWorld();
    }

    public void setWorld(World w) {
        this.location.setWorld(w);
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public void teleport(Location l) {
        if (!l.getWorld().equals(getWorld())) {
            ServerDestroyEntitiesPacket destroyEntitiesPacket = new ServerDestroyEntitiesPacket(getEntityID());
            //refreshEntityID();//Should not be needed when they change world to one people have not been in
            Packet respawn = getPacket();
            if (respawn != null)
                for (Player p : MCThunder.getPlayers()) {
                    p.sendPacket(destroyEntitiesPacket);
                    if (p.getWorld().equals(l.getWorld()))//If they are in the new world
                        p.sendPacket(respawn);
                }
        } else {
            ServerEntityTeleportPacket packet = new ServerEntityTeleportPacket(getEntityID(), l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch(), this.onGround);
            for (Player p : MCThunder.getPlayers())
                if (p.getWorld().equals(l.getWorld()))
                    p.sendPacket(packet);
        }
        setLocation(l);
    }
}