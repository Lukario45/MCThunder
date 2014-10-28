package net.mcthunder;

import net.mcthunder.api.*;
import net.mcthunder.events.listeners.PlayerChatEventListener;
import net.mcthunder.events.listeners.PlayerCommandEventListener;
import net.mcthunder.events.source.PlayerChatEventSource;
import net.mcthunder.events.source.PlayerCommandEventSource;
import net.mcthunder.handlers.PlayerProfileHandler;
import net.mcthunder.handlers.ServerChatHandler;
import net.mcthunder.handlers.ServerPlayerEntryListHandler;
import net.mcthunder.handlers.ServerTabHandler;
import net.mcthunder.world.Column;
import net.mcthunder.world.World;
import org.reflections.Reflections;
import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.mc.protocol.ProtocolConstants;
import org.spacehq.mc.protocol.ProtocolMode;
import org.spacehq.mc.protocol.ServerLoginHandler;
import org.spacehq.mc.protocol.data.game.*;
import org.spacehq.mc.protocol.data.game.values.entity.MetadataType;
import org.spacehq.mc.protocol.data.game.values.entity.player.Animation;
import org.spacehq.mc.protocol.data.game.values.entity.player.GameMode;
import org.spacehq.mc.protocol.data.game.values.setting.Difficulty;
import org.spacehq.mc.protocol.data.game.values.world.WorldType;
import org.spacehq.mc.protocol.data.message.TextMessage;
import org.spacehq.mc.protocol.data.status.PlayerInfo;
import org.spacehq.mc.protocol.data.status.ServerStatusInfo;
import org.spacehq.mc.protocol.data.status.VersionInfo;
import org.spacehq.mc.protocol.data.status.handler.ServerInfoBuilder;
import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientKeepAlivePacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientSettingsPacket;
import org.spacehq.mc.protocol.packet.ingame.client.ClientTabCompletePacket;
import org.spacehq.mc.protocol.packet.ingame.client.player.*;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerKeepAlivePacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.*;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.event.server.ServerAdapter;
import org.spacehq.packetlib.event.server.SessionAddedEvent;
import org.spacehq.packetlib.event.server.SessionRemovedEvent;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;
import org.spacehq.packetlib.event.session.SessionAdapter;
import org.spacehq.packetlib.packet.Packet;
import org.spacehq.packetlib.tcp.TcpSessionFactory;

import java.util.*;

import static net.mcthunder.api.Utils.*;

/**
 * Created by Kevin on 8/9/2014.
 */
public class MCThunder {
    public static HashMap<UUID, Player> playerHashMap;
    public static HashMap<String, World> worldHashMap;
    private static Config conf;
    private static String serverName;
    private static boolean SPAWN_SERVER = true;
    private static boolean VERIFY_USERS = false;
    private static String HOST;
    private static int PORT;
    private static ServerChatHandler chatHandler;
    private static ServerTabHandler tabHandler;
    private static ServerPlayerEntryListHandler entryListHandler;
    private static PlayerProfileHandler playerProfileHandler;
    private static Server server;
    private static PlayerChatEventListener defaultPlayerChatEventListener;
    private static PlayerChatEventSource playerChatEventSource;
    private static PlayerCommandEventSource playerCommandEventSource;
    private static PlayerCommandEventListener defaultPlayerCommandEventListener;

    public static void main(String args[]) {
        conf = new Config();
        conf.loadConfig();
        //Set Server data
        serverName = conf.getServerName();
        VERIFY_USERS = conf.getOnlineMode();
        HOST = getIP();
        PORT = conf.getPort();
        //Done Set Server Data
        tellConsole(LoggingLevel.INFO, "INTERNAL PORT " + HOST);
        createInitialDirs();
        tellPublicIpAddress();
        //Register Default Commands
        /**
         * Based of of Alphabot/Lukabot code that was created by zack6849
         */
        final String pkg = "net.mcthunder.commands.";
        Reflections reflections = new Reflections("net.mcthunder.commands");
        Set<Class<? extends Command>> subTypes = reflections.getSubTypesOf(Command.class);
        for (Class c : subTypes)
            CommandRegistry.register(CommandRegistry.getCommand(c.getSimpleName(), pkg));
        //Done
        if (SPAWN_SERVER) {
            server = new Server(HOST, PORT, MinecraftProtocol.class, new TcpSessionFactory());
            //Set Handlers
            chatHandler = new ServerChatHandler();
            entryListHandler = new ServerPlayerEntryListHandler();
            tabHandler = new ServerTabHandler();
            playerProfileHandler = new PlayerProfileHandler(server);
            //Done Set Handlers
            //Listeners
            defaultPlayerChatEventListener = new PlayerChatEventListener();
            defaultPlayerCommandEventListener = new PlayerCommandEventListener();
            playerChatEventSource = new PlayerChatEventSource();
            playerCommandEventSource = new PlayerCommandEventSource();
            playerChatEventSource.addEventListener(defaultPlayerChatEventListener);
            playerCommandEventSource.addEventListener(defaultPlayerCommandEventListener);
            //Done Listeners
            playerHashMap = new HashMap<UUID, Player>(conf.getSlots());
            worldHashMap = new HashMap<String, World>();

            /*File dir = new File("worlds");
            File[] files = dir.listFiles();
            for(File f : files) {
                worldHashMap.put(f.getName(), new World(f.getName()));
                World w = worldHashMap.get(f.getName());
                try {
                    w.loadWorld();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Region r = new Region(w, getLong(0, 0));
                r.loadRegion();
            }*/
            // if (!worldHashMap.containsKey("world"))
            // worldHashMap.put("world", new World("world", 0, chunks));
            //   player.setWorld(worldHashMap.get("world"));
            worldHashMap.put(conf.getWorldName(), new World(conf.getWorldName()));
            final World world = worldHashMap.get(conf.getWorldName());

                world.loadWorld();


            //world.getRegion(getLong(0,0)).loadRegion();

            //r.loadRegion();


            server.setGlobalFlag(ProtocolConstants.VERIFY_USERS_KEY, VERIFY_USERS);
            server.setGlobalFlag(ProtocolConstants.SERVER_COMPRESSION_THRESHOLD, 100);
            server.setGlobalFlag(ProtocolConstants.SERVER_INFO_BUILDER_KEY, new ServerInfoBuilder() {
                @Override
                public ServerStatusInfo buildInfo(Session session) {
                    return new ServerStatusInfo(new VersionInfo(ProtocolConstants.GAME_VERSION, ProtocolConstants.PROTOCOL_VERSION), new PlayerInfo(conf.getSlots(), playerHashMap.size(), new GameProfile[0]), new TextMessage(conf.getServerMOTD()), null);
                }
            });

            server.setGlobalFlag(ProtocolConstants.SERVER_LOGIN_HANDLER_KEY, new ServerLoginHandler() {
                @Override
                public void loggedIn(Session session) {
                    GameProfile profile = session.getFlag(ProtocolConstants.PROFILE_KEY);
                    if (playerHashMap.containsKey(profile.getId()))
                        playerHashMap.get(profile.getId()).getSession().disconnect("You logged in from another location!");

                    int entityID = (int) Math.ceil(Math.random() * Integer.MAX_VALUE);
                    EntityMetadata metadata = new EntityMetadata(2, MetadataType.STRING, profile.getName());
                    playerHashMap.put(profile.getId(), new Player(server, session, profile, entityID, 0, metadata));

                    Player player = playerHashMap.get(profile.getId());
                    //how about you don't waste time getting an exact copy of a variable you already have stored?
                    session.send(new ServerJoinGamePacket(0, false, GameMode.CREATIVE, 0, Difficulty.PEACEFUL, conf.getSlots(), WorldType.FLAT, false));
                    tellConsole(LoggingLevel.INFO, String.format("User %s is connecting from %s:%s", player.gameProfile().getName(), session.getHost(), session.getPort()));
                    entryListHandler.addToPlayerEntryList(server, session);
                    //Send World Data
                    player.setLocation(world.getSpawnLocation());
                    player.loadChunks(null);
                    player.getSession().send(new ServerPlayerPositionRotationPacket(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 0, 0));
                    player.getSession().send(new ServerSpawnPositionPacket(new Position((int)player.getLocation().getX(), (int)player.getLocation().getY(), (int)player.getLocation().getZ())));

                    player.getChatHandler().sendMessage(server, "&7&o" + profile.getName() + " connected");
                    playerProfileHandler.checkPlayer(player);

                    ServerSpawnPlayerPacket toAllPlayers = new ServerSpawnPlayerPacket(player.getEntityID(), player.gameProfile().getId(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch(), player.getHeldItem(), player.getMetadata().getMetadataArray());
                    for (Player player1 : playerHashMap.values()) {
                        ServerSpawnPlayerPacket toNewPlayer = new ServerSpawnPlayerPacket(player1.getEntityID(), player1.gameProfile().getId(), player1.getLocation().getX(), player1.getLocation().getY(), player1.getLocation().getZ(), player1.getLocation().getYaw(), player1.getLocation().getPitch(), player1.getHeldItem(), player1.getMetadata().getMetadataArray());
                        if (player1.gameProfile().getName().equals(player.gameProfile().getName())) {

                        } else {
                            player1.getSession().send(toAllPlayers);
                            player.getSession().send(toNewPlayer);
                        }
                    }

                }
            });

            server.addListener(new ServerAdapter() {
                @Override
                public void sessionAdded(SessionAddedEvent event) {
                    event.getSession().addListener(new SessionAdapter() {
                        @Override
                        public void packetReceived(PacketReceivedEvent event) {
                            if (event.getPacket() instanceof ClientPlayerMovementPacket) {
                                ClientPlayerMovementPacket pack = event.getPacket();
                                for (Packet packet : createUpdatePackets(event.getSession(), pack))
                                    for (Player p : playerHashMap.values())
                                        if (!p.gameProfile().getName().equals(event.getSession().<GameProfile>getFlag(ProtocolConstants.PROFILE_KEY).getName()))
                                            p.getSession().send(packet);

                                updatePlayerPosition(event.getSession(), pack);
                            } else if (event.getPacket() instanceof ClientPlayerStatePacket) {
                                ClientPlayerStatePacket packet = event.getPacket();
                                GameProfile profile = event.getSession().getFlag(ProtocolConstants.PROFILE_KEY);
                                Player player = playerHashMap.get(profile.getId());
                                switch (packet.getState()) {
                                    case START_SNEAKING:
                                        player.setSneaking(true);
                                        break;
                                    case STOP_SNEAKING:
                                        player.setSneaking(false);
                                        break;
                                    case LEAVE_BED:
                                        break;
                                    case START_SPRINTING:
                                        player.setSprinting(true);
                                        break;
                                    case STOP_SPRINTING:
                                        player.setSprinting(false);
                                        break;
                                    case RIDING_JUMP:
                                        break;
                                    case OPEN_INVENTORY:
                                        break;
                                }
                            } else if (event.getPacket() instanceof ClientSwingArmPacket) {
                                GameProfile profile = event.getSession().getFlag(ProtocolConstants.PROFILE_KEY);
                                Player player = playerHashMap.get(profile.getId());
                                for (Player p : playerHashMap.values())
                                    if (!p.gameProfile().getName().equals(profile.getName()))
                                        p.getSession().send(new ServerAnimationPacket(player.getEntityID(), Animation.SWING_ARM));
                            } else if (event.getPacket() instanceof ClientChatPacket) {
                                ClientChatPacket packet = event.getPacket();
                                GameProfile profile = event.getSession().getFlag(ProtocolConstants.PROFILE_KEY);
                                Player player = playerHashMap.get(profile.getId());
                                if (packet.getMessage().startsWith("/")) {
                                    if (packet.getMessage().equals("/"))
                                        chatHandler.sendPrivateMessage(event.getSession(), "&cCommand does not exist!");
                                    else
                                        try {
                                            playerCommandEventSource.fireEvent(player, packet);
                                        } catch (ClassNotFoundException e) {
                                            player.sendMessage("&cUnknown Command");
                                        }
                                } else
                                    playerChatEventSource.fireEvent(player, packet);

                            } else if (event.getPacket() instanceof ClientKeepAlivePacket)
                                event.getSession().send(new ServerKeepAlivePacket(event.<ClientKeepAlivePacket>getPacket().getPingId()));
                            else if (event.getPacket() instanceof ClientSettingsPacket) {

                            } else if (event.getPacket() instanceof ClientTabCompletePacket) {
                                ClientTabCompletePacket packet = event.getPacket();
                                tabHandler.handleTabComplete(server, event.getSession(), packet);

                            } else if (event.getPacket() instanceof ClientPlayerPlaceBlockPacket) {
                                ClientPlayerPlaceBlockPacket packet = event.getPacket();
                                Player player = playerHashMap.get(event.getSession().<GameProfile>getFlag(ProtocolConstants.PROFILE_KEY).getId());
                                Position position = packet.getPosition();
                                tellConsole(LoggingLevel.DEBUG, position.getX() + "/" + position.getY() + "/" + position.getZ());
                                ItemStack heldItem = packet.getHeldItem();
                                int heldItemId = heldItem.getId();
                                int columnX = position.getX() >> 4;
                                int columnZ = position.getZ() >> 4;
                                int chunkY = position.getY() >> 4;
                                Column column = player.getWorld().getColumn(getLong(columnX, columnZ));
                                Chunk[] chunks = column.getChunks();
                                ShortArray3d blocks = chunks[chunkY].getBlocks();
                                NibbleArray3d blockLight = chunks[chunkY].getBlockLight();
                                NibbleArray3d skyLight = chunks[chunkY].getBlockLight();
                                tellConsole(LoggingLevel.DEBUG, "X " + columnX + " Z " + columnZ + " Y " + chunkY + "Block ID " + heldItemId);
                                blocks.setBlock(position.getX(), position.getY(), position.getZ(), heldItemId << 4);
                                chunks[chunkY] = new Chunk(blocks, blockLight, skyLight);
                                Column c = new Column(getLong(columnX, columnZ), chunks);
                                player.getWorld().addColumn(c);
                                for (Player p : playerHashMap.values()) {
                                    if (p.isColumnLoaded(getLong(columnX, columnZ))) {
                                        p.addColumn(c);
                                    }
                                }

                            } else if (event.getPacket() != null)
                                tellConsole(LoggingLevel.DEBUG, event.getPacket().toString());
                        }
                    });
                }

                @Override
                public void sessionRemoved(SessionRemovedEvent event) {
                    if (((MinecraftProtocol) event.getSession().getPacketProtocol()).getMode() == ProtocolMode.GAME) {
                        GameProfile profile = event.getSession().getFlag(ProtocolConstants.PROFILE_KEY);
                        Player player = playerHashMap.get(profile.getId());
                        player.getChatHandler().sendMessage(server, "&7&o" + profile.getName() + " disconnected");
                        entryListHandler.deleteFromPlayerEntryList(server, event.getSession());
                        ServerDestroyEntitiesPacket destroyEntitiesPacket = new ServerDestroyEntitiesPacket(player.getEntityID());
                        for (Player p : playerHashMap.values())
                            p.getSession().send(destroyEntitiesPacket);
                        player.setAppended("");
                        playerHashMap.remove(player);
                    }
                }
            });

            server.bind();
            while (server.isListening()) {
                for (Player player : playerHashMap.values()) {
                    EntityMetadata changes[] = player.getMetadata().getChanges();
                    if (changes != null)
                        for (Player p : playerHashMap.values())
                            if (!p.gameProfile().getName().equals(player.gameProfile().getName()))
                                p.getSession().send(new ServerEntityMetadataPacket(player.getEntityID(), changes));
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    private static void updatePlayerPosition(Session session, ClientPlayerMovementPacket packet) {
        GameProfile profile = session.getFlag(ProtocolConstants.PROFILE_KEY);
        Player player = playerHashMap.get(profile.getId());
        if (player.getLocation() == null)
            player.setLocation(worldHashMap.get(conf.getWorldName()).getSpawnLocation());

        if (packet instanceof ClientPlayerPositionPacket || packet instanceof ClientPlayerPositionRotationPacket) {
            int fromChunkX = (int)player.getLocation().getX() / 16;
            int fromChunkZ = (int)player.getLocation().getZ() / 16;
            int toChunkX = (int)packet.getX() / 16;
            int toChunkZ = (int)packet.getZ() / 16;
            player.getLocation().setX(packet.getX());
            player.getLocation().setY(packet.getY());
            player.getLocation().setZ(packet.getZ());
            if (fromChunkX != toChunkX || fromChunkZ != toChunkZ) {
                //tellConsole(LoggingLevel.DEBUG, "Player has entered new Chunk");
                Direction dir = null;
                if(fromChunkZ - toChunkZ > 0 && fromChunkX - toChunkX < 0)
                    dir = Direction.NORTH_EAST;
                else if(fromChunkZ - toChunkZ < 0 && fromChunkX - toChunkX < 0)
                    dir = Direction.SOUTH_EAST;
                else if(fromChunkZ - toChunkZ < 0 && fromChunkX - toChunkX > 0)
                    dir = Direction.SOUTH_WEST;
                else if(fromChunkZ - toChunkZ > 0 && fromChunkX - toChunkX > 0)
                    dir = Direction.NORTH_WEST;
                else if(fromChunkZ - toChunkZ > 0)
                    dir = Direction.NORTH;
                else if(fromChunkX - toChunkX < 0)
                    dir = Direction.EAST;
                else if(fromChunkZ - toChunkZ < 0)
                    dir = Direction.SOUTH;
                else if(fromChunkX - toChunkX > 0)
                    dir = Direction.WEST;
                player.loadChunks(dir);
            }
        }

        if (packet instanceof ClientPlayerRotationPacket || packet instanceof ClientPlayerPositionRotationPacket) {
            player.getLocation().setPitch((float) packet.getPitch());
            player.getLocation().setYaw((float) packet.getYaw());
        }
        player.setOnGround(packet.isOnGround());
    }

    private static List<Packet> createUpdatePackets(Session session, ClientPlayerMovementPacket packet) {
        List<Packet> packets = new ArrayList<Packet>();
        GameProfile profile = session.getFlag(ProtocolConstants.PROFILE_KEY);
        Player player = playerHashMap.get(profile.getId());
        if (packet instanceof ClientPlayerPositionPacket || packet instanceof ClientPlayerPositionRotationPacket) {
            double movedX = packet.getX() - player.getLocation().getX();
            double movedY = packet.getY() - player.getLocation().getY();
            double movedZ = packet.getZ() - player.getLocation().getZ();
            float yaw = player.getLocation().getYaw();
            float pitch = player.getLocation().getPitch();
            if (packet instanceof ClientPlayerPositionRotationPacket) {
                yaw = (float) packet.getYaw();
                pitch = (float) packet.getPitch();
            }

            int packedX = (int) (movedX * 32);
            int packedY = (int) (movedY * 32);
            int packedZ = (int) (movedZ * 32);
            if (packedX > Byte.MAX_VALUE || packedY > Byte.MAX_VALUE || packedZ > Byte.MAX_VALUE || packedX < Byte.MIN_VALUE || packedY < Byte.MIN_VALUE || packedZ < Byte.MIN_VALUE)
                packets.add(new ServerEntityTeleportPacket(player.getEntityID(), packet.getX(), packet.getY(), packet.getZ(), yaw, pitch, packet.isOnGround()));
            else if (packet instanceof ClientPlayerPositionPacket)
                packets.add(new ServerEntityPositionPacket(player.getEntityID(), movedX, movedY, movedZ, packet.isOnGround()));
            else if (packet instanceof ClientPlayerPositionRotationPacket) {
                packets.add(new ServerEntityPositionRotationPacket(player.getEntityID(), movedX, movedY, movedZ, yaw, pitch, packet.isOnGround()));
                packets.add(new ServerEntityHeadLookPacket(player.getEntityID(), yaw));
            }
        } else if (packet instanceof ClientPlayerRotationPacket) {
            float yaw = (float) packet.getYaw();
            float pitch = (float) packet.getPitch();
            packets.add(new ServerEntityRotationPacket(player.getEntityID(), yaw, pitch, packet.isOnGround()));
            packets.add(new ServerEntityHeadLookPacket(player.getEntityID(), yaw));
        }
        return packets;
    }

    public static Server getServer() {
        return server;
    }

    public static Player getPlayer(String name) {
        Player partial = null;
        for (Player p : playerHashMap.values())
            if (p.gameProfile().getName().equalsIgnoreCase(name))
                return p;
            else if (partial == null && p.gameProfile().getName().toLowerCase().contains(name.toLowerCase()))
                partial = p;
        return partial;
    }

    public static Player getPlayer(UUID uuid) {
        return playerHashMap.get(uuid);
    }
}
