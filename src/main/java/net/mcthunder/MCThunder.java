package net.mcthunder;

import net.mcthunder.api.*;
import net.mcthunder.block.Block;
import net.mcthunder.events.listeners.PlayerChatEventListener;
import net.mcthunder.events.listeners.PlayerCommandEventListener;
import net.mcthunder.events.source.PlayerChatEventSource;
import net.mcthunder.events.source.PlayerCommandEventSource;
import net.mcthunder.handlers.PlayerProfileHandler;
import net.mcthunder.handlers.ServerChatHandler;
import net.mcthunder.handlers.ServerPlayerEntryListHandler;
import net.mcthunder.handlers.ServerTabHandler;
import net.mcthunder.material.Material;
import net.mcthunder.world.Biome;
import net.mcthunder.world.World;
import org.fusesource.jansi.AnsiConsole;
import org.reflections.Reflections;
import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.mc.protocol.ProtocolConstants;
import org.spacehq.mc.protocol.ProtocolMode;
import org.spacehq.mc.protocol.ServerLoginHandler;
import org.spacehq.mc.protocol.data.game.EntityMetadata;
import org.spacehq.mc.protocol.data.game.ItemStack;
import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.mc.protocol.data.game.values.Face;
import org.spacehq.mc.protocol.data.game.values.entity.MetadataType;
import org.spacehq.mc.protocol.data.game.values.entity.player.Animation;
import org.spacehq.mc.protocol.data.game.values.entity.player.GameMode;
import org.spacehq.mc.protocol.data.game.values.entity.player.PlayerAction;
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
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientCreativeInventoryActionPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerKeepAlivePacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.*;
import org.spacehq.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import org.spacehq.opennbt.tag.builtin.*;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.event.server.ServerAdapter;
import org.spacehq.packetlib.event.server.SessionAddedEvent;
import org.spacehq.packetlib.event.server.SessionRemovedEvent;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;
import org.spacehq.packetlib.event.session.SessionAdapter;
import org.spacehq.packetlib.packet.Packet;
import org.spacehq.packetlib.tcp.TcpSessionFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

import static net.mcthunder.api.Utils.*;

/**
 * Created by Kevin on 8/9/2014.
 */
public class MCThunder {
    private static HashMap<UUID, Player> playerHashMap;
    private static HashMap<String, World> worldHashMap;
    private static Config conf;
    private static String serverName;
    private static boolean SPAWN_SERVER = true;//What is this variable for anyways
    private static String HOST;
    private static int PORT;
    private static int RENDER_DISTANCE;
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
        AnsiConsole.systemInstall();
        conf = new Config();
        conf.loadConfig();
        //Set Server data
        serverName = conf.getServerName();
        HOST = getIP();
        PORT = conf.getPort();
        RENDER_DISTANCE = conf.getRenderDistance();
        //Done Set Server Data
        tellConsole(LoggingLevel.INFO, "Internal IP " + HOST);
        createInitialDirs();
        Biome.mapBiomes();
        Material.mapMaterials();
        Enchantment.mapEnchantments();
        PotionEffectType.mapPotionEffects();
        EntityType.mapEntityTypes();
        tellPublicIpAddress();
        //Register Default Commands
        /**
         * Based of of Alphabot/Lukabot code that was created by zack6849
         */
        Reflections.log = null;
        Reflections reflections = new Reflections("net.mcthunder.commands");
        Set<Class<? extends Command>> subTypes = reflections.getSubTypesOf(Command.class);
        int commands = 0;
        for (Class c : subTypes)
            if (CommandRegistry.getCommand(c.getSimpleName(), "net.mcthunder.commands.") != null)
                commands++;
        tellConsole(LoggingLevel.INFO, commands + " command" + (commands != 1 ? "s " : "") + "were loaded.");
        //Done
        if (SPAWN_SERVER) {
            server = new Server(HOST, PORT, MinecraftProtocol.class, new TcpSessionFactory());
            //Handlers
            chatHandler = new ServerChatHandler();
            entryListHandler = new ServerPlayerEntryListHandler();
            tabHandler = new ServerTabHandler();
            playerProfileHandler = new PlayerProfileHandler();
            //Listeners
            defaultPlayerChatEventListener = new PlayerChatEventListener();
            defaultPlayerCommandEventListener = new PlayerCommandEventListener();
            playerChatEventSource = new PlayerChatEventSource();
            playerCommandEventSource = new PlayerCommandEventSource();
            playerChatEventSource.addEventListener(defaultPlayerChatEventListener);
            playerCommandEventSource.addEventListener(defaultPlayerCommandEventListener);

            playerHashMap = new HashMap<>(conf.getSlots());
            worldHashMap = new HashMap<>();

            final World world = new World(conf.getWorldName());
            worldHashMap.put(conf.getWorldName(), world);
            world.loadWorld();

            server.setGlobalFlag(ProtocolConstants.VERIFY_USERS_KEY, conf.getOnlineMode());
            server.setGlobalFlag(ProtocolConstants.SERVER_COMPRESSION_THRESHOLD, 100);
            server.setGlobalFlag(ProtocolConstants.SERVER_INFO_BUILDER_KEY, new ServerInfoBuilder() {
                @Override
                public ServerStatusInfo buildInfo(Session session) {
                    GameProfile[] gameProfiles = new GameProfile[playerHashMap.size()];
                    int i = 0;
                    for(Player p : getPlayers()) {
                        gameProfiles[i] = p.getGameProfile();
                        i++;
                    }
                    BufferedImage icon = null;
                    try {
                        icon = ImageIO.read(new File("server-icon.png"));
                    } catch (Exception ignored) { }//When there is no icon set
                    return new ServerStatusInfo(new VersionInfo(ProtocolConstants.GAME_VERSION, ProtocolConstants.PROTOCOL_VERSION), new PlayerInfo(conf.getSlots(), playerHashMap.size(), gameProfiles), new TextMessage(conf.getServerMOTD()), icon);
                }
            });

            server.setGlobalFlag(ProtocolConstants.SERVER_LOGIN_HANDLER_KEY, new ServerLoginHandler() {
                @Override
                public void loggedIn(Session session) {
                    GameProfile profile = session.getFlag(ProtocolConstants.PROFILE_KEY);
                    if (playerHashMap.containsKey(profile.getId()))
                        getPlayer(profile.getId()).getSession().disconnect("You logged in from another location!");

                    int entityID = (int) Math.ceil(Math.random() * Integer.MAX_VALUE);
                    EntityMetadata metadata = new EntityMetadata(2, MetadataType.STRING, profile.getName());
                    Player player = new Player(session, entityID, metadata);
                    playerHashMap.put(profile.getId(), player);
                    CompoundTag c = (CompoundTag) playerProfileHandler.getAttribute(player, "SpawnPosition");
                    Location l = null;
                    if(c != null)
                        l = new Location(getWorld((String) c.get("World").getValue()), (double) c.get("X").getValue(), (double) c.get("Y").getValue(), (double) c.get("Z").getValue(), (float) c.get("Yaw").getValue(), (float) c.get("Pitch").getValue());
                    player.setLocation(l == null ? world.getSpawnLocation() : l);
                    player.sendPacket(new ServerJoinGamePacket(player.getEntityID(), player.getWorld().isHardcore(), player.getGameMode(), player.getWorld().getDimension(), player.getWorld().getDifficulty(), conf.getSlots(), player.getWorld().getWorldType(), false));
                    tellConsole(LoggingLevel.INFO, String.format("User %s is connecting from %s:%s", player.getGameProfile().getName(), session.getHost(), session.getPort()));
                    entryListHandler.addToPlayerEntryList(session, player.getGameMode());
                    //Send World Data
                    player.loadChunks(null);
                    player.sendPacket(new ServerPlayerPositionRotationPacket(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch()));
                    player.sendPacket(new ServerSpawnPositionPacket(new Position((int) player.getLocation().getX(), (int) player.getLocation().getY(), (int) player.getLocation().getZ())));
                    broadcast("&7&o" + profile.getName() + " connected");
                    playerProfileHandler.checkPlayer(player);
                    //StringTag test = (StringTag) playerProfileHandler.getAttribute(player,"test");
                    // tellConsole(LoggingLevel.DEBUG,test.getValue());


                    ServerSpawnPlayerPacket toAllPlayers = new ServerSpawnPlayerPacket(player.getEntityID(), player.getUniqueID(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch(), player.getHeldItem().getId(), player.getMetadata().getMetadataArray());
                    for (Player player1 : getPlayers()) {
                        if (!player1.getWorld().equals(player.getWorld()))
                            continue;//Also will need to check if out of range ,_,
                        ServerSpawnPlayerPacket toNewPlayer = new ServerSpawnPlayerPacket(player1.getEntityID(), player1.getGameProfile().getId(), player1.getLocation().getX(), player1.getLocation().getY(), player1.getLocation().getZ(), player1.getLocation().getYaw(), player1.getLocation().getPitch(), player1.getHeldItem().getId(), player1.getMetadata().getMetadataArray());
                        if (!player1.getUniqueID().equals(player.getUniqueID())) {
                            player1.sendPacket(toAllPlayers);
                            player.sendPacket(toNewPlayer);
                        }
                    }
                    player.toggleMoveable();
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
                                Player mover = getPlayer(event.getSession().<GameProfile>getFlag(ProtocolConstants.PROFILE_KEY).getId());
                                if (mover == null || !mover.isMoveable())
                                    return;//Also will need to cancel on their end somehow
                                for (Packet packet : createUpdatePackets(event.getSession(), pack))
                                    for (Player p : getPlayers()) {
                                        if (!p.getWorld().equals(mover.getWorld()))
                                            continue;//Also will need to check if out of range ,_,
                                        if (!p.getUniqueID().equals(mover.getUniqueID()))
                                            p.sendPacket(packet);
                                    }

                                updatePlayerPosition(event.getSession(), pack);
                            } else if (event.getPacket() instanceof ClientPlayerStatePacket) {
                                ClientPlayerStatePacket packet = event.getPacket();
                                GameProfile profile = event.getSession().getFlag(ProtocolConstants.PROFILE_KEY);
                                Player player = getPlayer(profile.getId());
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
                                Player player = getPlayer(profile.getId());
                                for (Player p : getPlayers()) {
                                    if (!p.getWorld().equals(player.getWorld()))
                                        continue;//Do not send a animation packet if they are not in same world
                                    if (!p.getUniqueID().equals(player.getUniqueID()))
                                        p.sendPacket(new ServerAnimationPacket(player.getEntityID(), Animation.SWING_ARM));
                                }
                            } else if (event.getPacket() instanceof ClientChatPacket) {
                                ClientChatPacket packet = event.getPacket();
                                GameProfile profile = event.getSession().getFlag(ProtocolConstants.PROFILE_KEY);
                                Player player = getPlayer(profile.getId());
                                if (packet.getMessage().startsWith("/")) {
                                    if (packet.getMessage().equals("/"))
                                        chatHandler.sendMessage(event.getSession(), "&cCommand does not exist!");
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
                                ClientSettingsPacket packet = event.getPacket();
                                Player player = getPlayer(event.getSession().<GameProfile>getFlag(ProtocolConstants.PROFILE_KEY).getId());
                                if (player.getView() != packet.getRenderDistance()) {
                                    player.setView(packet.getRenderDistance());
                                    //TODO: unload chunks if goes smaller load if goes higher also have it check during login what their distance is
                                }
                            } else if (event.getPacket() instanceof ClientTabCompletePacket) {
                                ClientTabCompletePacket packet = event.getPacket();
                                tabHandler.handleTabComplete(event.getSession(), packet);
                            } else if (event.getPacket() instanceof ClientCreativeInventoryActionPacket) {
                                ClientCreativeInventoryActionPacket packet = event.getPacket();
                                Player player = getPlayer(event.getSession().<GameProfile>getFlag(ProtocolConstants.PROFILE_KEY).getId());
                                ItemStack old = player.getHeldItem();
                                ItemStack i = packet.getClickedItem();
                                player.getInventory().setSlot(packet.getSlot(), i);
                                if(packet.getSlot() == player.getSlot() && !old.equals(player.getHeldItem())) {
                                    Packet pack = new ServerEntityEquipmentPacket(player.getEntityID(), packet.getSlot(), player.getHeldItem());
                                    for (Player p : getPlayers())
                                        if (p.getWorld().equals(player.getWorld()) && !player.getUniqueID().equals(p.getUniqueID()))
                                            p.sendPacket(pack);
                                }
                            } else if (event.getPacket() instanceof ClientWindowActionPacket) {
                                ClientWindowActionPacket packet = event.getPacket();
                                Player player = getPlayer(event.getSession().<GameProfile>getFlag(ProtocolConstants.PROFILE_KEY).getId());
                                //tellConsole(LoggingLevel.DEBUG, packet.getSlot() + " " + packet.getAction().name() + " " + packet.getWindowId());
                                //0 = click craft output
                                //1-4 = craft input
                                //5-8 = helm to boots
                                //9-35 = inventory
                                //36-44 = hotbar
                                //-999 = outside of inventory for dropping items
                                //TODO: logic for this as well as for when they hit like a number over the item to move the item
                            } else if (event.getPacket() instanceof ClientChangeHeldItemPacket) {
                                ClientChangeHeldItemPacket packet = event.getPacket();
                                Player player = getPlayer(event.getSession().<GameProfile>getFlag(ProtocolConstants.PROFILE_KEY).getId());
                                ItemStack old = player.getHeldItem();
                                player.setSlot(packet.getSlot() + 36);
                                if (!old.equals(player.getHeldItem())) {
                                    Packet pack = new ServerEntityEquipmentPacket(player.getEntityID(), packet.getSlot(), player.getHeldItem());
                                    for (Player p : getPlayers())
                                        if (p.getWorld().equals(player.getWorld()) && !player.getUniqueID().equals(p.getUniqueID()))
                                            p.sendPacket(pack);
                                }
                            } else if (event.getPacket() instanceof ClientPlayerPlaceBlockPacket) {
                                ClientPlayerPlaceBlockPacket packet = event.getPacket();
                                Player player = getPlayer(event.getSession().<GameProfile>getFlag(ProtocolConstants.PROFILE_KEY).getId());
                                Position position = packet.getPosition();
                                ItemStack heldItem = packet.getHeldItem();
                                if (heldItem == null || (position.getY() >> 4) < 0)
                                    return;
                                //tellConsole(LoggingLevel.DEBUG, "x " + packet.getCursorX() + " y " + packet.getCursorY() + " z " + packet.getCursorZ());
                                int type = heldItem.getId();
                                short data = (short) heldItem.getData();
                                Block b = new Block(new Location(player.getWorld(), position.getX(), position.getY(), position.getZ()));
                                if (!b.getType().isLiquid() && !b.getType().equals(Material.SNOW_LAYER) && !b.getType().isLongGrass())
                                    b = b.getRelative(Direction.fromFace(packet.getFace()));
                                if (!b.getType().equals(Material.AIR))
                                    return;
                                Material setType = Material.fromID(type);
                                if(setType.equals(Material.TORCH)) {//Still need to check if is a valid torch position
                                    if(packet.getFace().equals(Face.SOUTH))
                                        data = 1;
                                    else if(packet.getFace().equals(Face.NORTH))
                                        data = 2;
                                    else if(packet.getFace().equals(Face.WEST))
                                        data = 3;
                                    else if(packet.getFace().equals(Face.EAST))
                                        data = 4;
                                    else
                                        data = 5;
                                }
                                if (Material.fromString(setType.getName() + "_BLOCK") != null)
                                    setType = Material.fromString(setType.getName() + "_BLOCK");
                                b.setType(setType, data);
                            } else if (event.getPacket() instanceof ClientPlayerActionPacket) {
                                ClientPlayerActionPacket packet = event.getPacket();
                                Player player = getPlayer(event.getSession().<GameProfile>getFlag(ProtocolConstants.PROFILE_KEY).getId());
                                if ((packet.getAction().equals(PlayerAction.START_DIGGING) && player.getGameMode().equals(GameMode.CREATIVE)) ||
                                    (player.getGameMode().equals(GameMode.SURVIVAL) && packet.getAction().equals(PlayerAction.FINISH_DIGGING))) {
                                    Block b = new Block(new Location(player.getWorld(), packet.getPosition().getX(), packet.getPosition().getY(), packet.getPosition().getZ()));
                                    b.setType(Material.AIR);
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
                        Player player = getPlayer(profile.getId());
                        broadcast("&7&o" + profile.getName() + " disconnected");
                        entryListHandler.deleteFromPlayerEntryList(event.getSession());
                        ServerDestroyEntitiesPacket destroyEntitiesPacket = new ServerDestroyEntitiesPacket(player.getEntityID());
                        for (Player p : getPlayers())
                            p.sendPacket(destroyEntitiesPacket);
                        player.setAppended("");
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
                        CompoundTag c = new CompoundTag("SpawnPosition", map);
                        playerProfileHandler.changeCompundAttribute(player, c);
                        playerHashMap.remove(player.getUniqueID());
                    }
                }
            });

            server.bind();
            while (server.isListening()) {
                for (Player player : getPlayers()) {
                    EntityMetadata changes[] = player.getMetadata().getChanges();
                    if (changes != null) {
                        ServerEntityMetadataPacket pack = new ServerEntityMetadataPacket(player.getEntityID(), changes);
                        for (Player p : getPlayers())
                            if (!p.getUniqueID().equals(player.getUniqueID()))
                                p.sendPacket(pack);
                    }
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException ignored) { }
            }
        }
    }

    private static void updatePlayerPosition(Session session, ClientPlayerMovementPacket packet) {
        GameProfile profile = session.getFlag(ProtocolConstants.PROFILE_KEY);
        Player player = getPlayer(profile.getId());
        if (player.getLocation() == null)
            player.setLocation(getWorld(conf.getWorldName()).getSpawnLocation());

        if (packet instanceof ClientPlayerPositionPacket || packet instanceof ClientPlayerPositionRotationPacket) {
            int fromChunkX = (int)player.getLocation().getX() >> 4;
            int fromChunkZ = (int)player.getLocation().getZ() >> 4;
            int toChunkX = (int)packet.getX() >> 4;
            int toChunkZ = (int)packet.getZ() >> 4;
            player.setX(packet.getX());
            player.setY(packet.getY());
            player.setZ(packet.getZ());
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
            player.setPitch((float) packet.getPitch());
            player.setYaw((float) packet.getYaw());
        }
        player.setOnGround(packet.isOnGround());
    }

    private static List<Packet> createUpdatePackets(Session session, ClientPlayerMovementPacket packet) {
        List<Packet> packets = new ArrayList<>();
        GameProfile profile = session.getFlag(ProtocolConstants.PROFILE_KEY);
        Player player = getPlayer(profile.getId());
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
            else {//if (packet instanceof ClientPlayerPositionRotationPacket)
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
        for (Player p : getPlayers())
            if (p.getName().equalsIgnoreCase(name))
                return p;
            else if (partial == null && p.getName().toLowerCase().contains(name.toLowerCase()))
                partial = p;
        return partial;
    }

    public static Player getPlayer(UUID uuid) {
        return playerHashMap.get(uuid);
    }

    public static int maxRenderDistance() {
        return RENDER_DISTANCE;
    }

    public static int getPort() {
        return PORT;
    }

    public static String getIp() {
        return HOST;
    }

    public static String getServerName() {
        return serverName;
    }

    public static void shutdown(String args) {
        for (Player p : getPlayers())
            p.getSession().disconnect(args);
        for (World w : getWorlds())
            w.unloadWorld();
        AnsiConsole.systemUninstall();
        server.close();
    }

    public static void broadcast(String message) {
        chatHandler.sendMessage(message);
    }

    public static ServerChatHandler getChatHandler() {
        return chatHandler;
    }

    public static void shutdown() {
        shutdown("Server Closed.");
    }

    public static Collection<Player> getPlayers() {
        return playerHashMap.values();
    }

    public static World getWorld(String name) {
        return worldHashMap.get(name);
    }

    public static Collection<World> getWorlds() {
        return worldHashMap.values();
    }

    public Config getConfig() {
        return conf;
    }
}