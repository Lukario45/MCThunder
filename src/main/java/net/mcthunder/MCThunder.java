package net.mcthunder;


import net.mcthunder.api.*;
import net.mcthunder.block.Material;
import net.mcthunder.entity.*;
import net.mcthunder.events.interfaces.PlayerAttackEntityEventListenerInterface;
import net.mcthunder.events.interfaces.PlayerCommandEventListenerInterface;
import net.mcthunder.events.interfaces.PlayerLoggingInEventListenerInterface;
import net.mcthunder.events.listeners.*;
import net.mcthunder.events.source.*;
import net.mcthunder.gui.Window;
import net.mcthunder.handlers.PlayerProfileHandler;
import net.mcthunder.handlers.ServerChatHandler;
import net.mcthunder.handlers.ServerPlayerEntryListHandler;
import net.mcthunder.handlers.ServerTabHandler;
import net.mcthunder.inventory.Inventory;
import net.mcthunder.inventory.ItemStack;
import net.mcthunder.plugin.MCThunderPlugin;
import net.mcthunder.rankmanager.RankManager;
import net.mcthunder.world.Biome;
import net.mcthunder.world.World;
import org.fusesource.jansi.AnsiConsole;
import org.reflections.Reflections;
import org.spacehq.mc.auth.GameProfile;
import org.spacehq.mc.protocol.MinecraftProtocol;
import org.spacehq.mc.protocol.ProtocolConstants;
import org.spacehq.mc.protocol.ProtocolMode;
import org.spacehq.mc.protocol.ServerLoginHandler;
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
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientConfirmTransactionPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientCreativeInventoryActionPacket;
import org.spacehq.mc.protocol.packet.ingame.client.window.ClientWindowActionPacket;
import org.spacehq.mc.protocol.packet.ingame.server.ServerKeepAlivePacket;
import org.spacehq.mc.protocol.packet.ingame.server.entity.*;
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
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static net.mcthunder.api.Utils.*;

/**
 * Created by Kevin on 8/9/2014.
 */
public class MCThunder {
    private static ArrayList<String> commandPaths;
    private static ArrayList<Bot> bots;
    private static HashMap<UUID, Player> players;
    private static HashMap<String, World> worlds;
    private static Config conf;
    private static String serverName;
    private static String HOST;
    private static int PORT;
    private static int RENDER_DISTANCE;
    private static ServerChatHandler chatHandler;
    private static ServerTabHandler tabHandler;
    private static ServerPlayerEntryListHandler entryListHandler;
    private static PlayerProfileHandler playerProfileHandler;
    private static Server server;
    private static PlayerChatEventSource playerChatEventSource;
    private static PlayerCommandEventSource playerCommandEventSource;
    private static PlayerLoggingInEventSource loggingInEventSource;
    private static MetadataChangeEventSource metadataChangeEventSource;
    private static PlayerAttackEntityEventSource playerAttackEntityEventSource;
    private static PlayerBreakBlockEventSource playerBreakBlockEventSource;
    private static PlayerPlaceBlockEventSource playerPlaceBlockEventSource;
    private static RankManager rankManager;
    private static boolean guiMODE;
    private static Window window;

    public static void main(String args[]) {
        AnsiConsole.systemInstall();
        commandPaths = new ArrayList();

        conf = new Config();
        conf.loadConfig();
        //Set Server data
        serverName = conf.getServerName();
        HOST = getIP();
        PORT = conf.getPort();
        RENDER_DISTANCE = conf.getRenderDistance();
        guiMODE = conf.getGuiMode();
        //Done Set Server Data
        if (guiMODE) {
            window = new Window();
            window.startGUI();
        }
        tellConsole(LoggingLevel.INFO, "Internal IP " + HOST);
        createInitialDirs();
        Biome.mapBiomes();
        Material.mapMaterials();
        Enchantment.mapEnchantments();
        PotionEffectType.mapPotionEffects();
        EntityType.mapEntityTypes();
        Achievement.mapAchievements();
        tellPublicIpAddress();
        //Register Default Commands

        /**
         * Based of of Alphabot/Lukabot code that was created by zack6849
         */


        server = new Server(HOST, PORT, MinecraftProtocol.class, new TcpSessionFactory());

        //Handlers
        chatHandler = new ServerChatHandler();
        entryListHandler = new ServerPlayerEntryListHandler();
        tabHandler = new ServerTabHandler();
        playerProfileHandler = new PlayerProfileHandler();
        //Listeners

        PlayerChatEventListener defaultPlayerChatEventListener = new PlayerChatEventListener();
        PlayerCommandEventListener defaultPlayerCommandEventListener = new PlayerCommandEventListener();
        PlayerLoggingInEventListener loggingInEventListener = new PlayerLoggingInEventListener();
        MetadataChangeEventListener metadataChangeEventListener = new MetadataChangeEventListener();
        PlayerAttackEntityEventListener playerAttackEntityEventListener = new PlayerAttackEntityEventListener();
        PlayerBreakBlockEventListener playerBreakBlockEventListener = new PlayerBreakBlockEventListener();
        PlayerPlaceBlockEventListener playerPlaceBlockEventListener = new PlayerPlaceBlockEventListener();

        playerChatEventSource = new PlayerChatEventSource();
        playerCommandEventSource = new PlayerCommandEventSource();
        loggingInEventSource = new PlayerLoggingInEventSource();
        metadataChangeEventSource = new MetadataChangeEventSource();
        playerAttackEntityEventSource = new PlayerAttackEntityEventSource();
        playerBreakBlockEventSource = new PlayerBreakBlockEventSource();
        playerPlaceBlockEventSource = new PlayerPlaceBlockEventSource();

        playerChatEventSource.addEventListener(defaultPlayerChatEventListener);
        playerCommandEventSource.addEventListener(defaultPlayerCommandEventListener);
        loggingInEventSource.addEventListener(loggingInEventListener);
        metadataChangeEventSource.addEventListener(metadataChangeEventListener);
        playerAttackEntityEventSource.addEventListener(playerAttackEntityEventListener);
        playerBreakBlockEventSource.addEventListener(playerBreakBlockEventListener);
        playerPlaceBlockEventSource.addEventListener(playerPlaceBlockEventListener);

        players = new HashMap<>(conf.getSlots());
        worlds = new HashMap<>();
        bots = new ArrayList<>();
        //Load the world files and check for subdirectories recursively if it is not a world folder
        if (!new File("worlds/" + conf.getWorldName()).exists()) {
            tellConsole(LoggingLevel.INFO, "Making world " + conf.getWorldName());
            makeDir("worlds/" + conf.getWorldName());
            makeDir("worlds/" + conf.getWorldName() + "/region");
            World.newWorld(conf.getWorldName());
        }
        File worldDir = new File("worlds");
        tellConsole(LoggingLevel.INFO, "Loading Worlds");
        File[] files = worldDir.listFiles();
        if (files != null)
            for (File f : files)
                if (f.exists())
                    loadWorld(f);

        File dir = new File("plugins");
        tellConsole(LoggingLevel.INFO, "Loading Plugins");
        ArrayList<JarFile> jarFiles = new ArrayList<>();

        for (File f : dir.listFiles()) {
            if (f.getName().endsWith(".jar")) {
                try {
                    jarFiles.add(new JarFile(f));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        for (JarFile jar : jarFiles) {
            Enumeration<JarEntry> jarEntrys = jar.entries();
            while (jarEntrys.hasMoreElements()) {
                JarEntry jarEntry = jarEntrys.nextElement();
                //tellConsole(LoggingLevel.DEBUG, jarEntry.getName());
                if (jarEntry.getName().toLowerCase().endsWith(".class")) {
                    try {
                        Class cls = Class.forName(jarEntry.getName().replace(".class", "").replace("/", "."), false, new URLClassLoader(new URL[]{new URL("jar:file:" + jar.getName() + "!/")}));
                        if (MCThunderPlugin.class.isAssignableFrom(cls)) {
                            MCThunderPlugin plugin = (MCThunderPlugin) cls.getConstructor().newInstance();
                            break;
                        }
                    } catch (Exception e) {
                        tellConsole(LoggingLevel.ERROR, "The jar file: " + jar.getName().substring(jar.getName().lastIndexOf('\\') + 1) + " is not a valid MCThunder Plugin!");
                        e.printStackTrace();
                    }
                }
            }
        }

        if (conf.getUseRankManager()) {
            rankManager = new RankManager();
            rankManager.load();
        }

        Reflections.log = null;
        int commands = 0;
        addCommandPath("net.mcthunder.commands");
        //"net.mcthunder.commands", "net.mcthunder.rankmanager.commands"
        for (String path : getCommandPaths()) {
            Reflections reflections = new Reflections(path);
            Set<Class<? extends Command>> subTypes = reflections.getSubTypesOf(Command.class);
            for (Class c : subTypes)
                if (CommandRegistry.loadCommand(c.getSimpleName(), path + ".") != null)
                    commands++;
        }
        tellConsole(LoggingLevel.INFO, commands + " command" + (commands != 1 ? "s " : "") + "were loaded.");
        //Done

        server.setGlobalFlag(ProtocolConstants.VERIFY_USERS_KEY, conf.getOnlineMode());
        server.setGlobalFlag(ProtocolConstants.SERVER_COMPRESSION_THRESHOLD, 256);//Default is 256 not 100
        server.setGlobalFlag(ProtocolConstants.SERVER_INFO_BUILDER_KEY, new ServerInfoBuilder() {
            @Override
            public ServerStatusInfo buildInfo(Session session) {
                GameProfile[] gameProfiles = new GameProfile[players.size() + bots.size()];
                int i = 0;
                for (Player p : getPlayers())
                    gameProfiles[i++] = p.getGameProfile();
                for (int j = 0; j < bots.size(); j++)
                    gameProfiles[i++] = bots.get(j).getGameProfile();
                BufferedImage icon = null;
                try {
                    icon = ImageIO.read(new File("server-icon.png"));
                } catch (Exception ignored) {
                }//When there is no icon set
                return new ServerStatusInfo(new VersionInfo(ProtocolConstants.GAME_VERSION, ProtocolConstants.PROTOCOL_VERSION), new PlayerInfo(conf.getSlots() + bots.size(), players.size() + bots.size(), gameProfiles), new TextMessage(conf.getServerMOTD()), icon);
            }
        });

        server.setGlobalFlag(ProtocolConstants.SERVER_LOGIN_HANDLER_KEY, new ServerLoginHandler() {
            @Override
            public void loggedIn(Session session) {
                try {
                    loggingInEventSource.fireEvent(session);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
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
                            Player player = getPlayer(event.getSession().<GameProfile>getFlag(ProtocolConstants.PROFILE_KEY).getId());
                            ClientPlayerMovementPacket pack = event.getPacket();
                            if (player == null || !player.isMoveable())
                                return;
                            long chunk = player.getChunk();
                            for (Packet packet : createUpdatePackets(event.getSession(), pack))
                                for (Player p : getPlayers())
                                    if (p.getWorld().equals(player.getWorld()) && p.isColumnLoaded(chunk) && !p.getUniqueID().equals(player.getUniqueID()))
                                        p.sendPacket(packet);
                            updatePlayerPosition(event.getSession(), pack);
                            for ( Entity e: player.getWorld().getEntities()){
                                if (player.isTouching(e) && e.getType().isItem()){
                                    DroppedItem i = (DroppedItem) e;
                                    if (i.getAge() >= 0) {
                                        player.getInventory().add(i.getItemStack());
                                        player.updateInventory();
                                        player.getWorld().getEntities().remove(e);
                                        for (Player p : getPlayers()) {
                                            if (player.getWorld() == player.getWorld()) {
                                                p.sendPacket(new ServerCollectItemPacket(i.getEntityID(), player.getEntityID()));
                                            }
                                        }
                                    }
                                }
                            }

                        } else if (event.getPacket() instanceof ClientPlayerStatePacket) {
                            Player player = getPlayer(event.getSession().<GameProfile>getFlag(ProtocolConstants.PROFILE_KEY).getId());
                            ClientPlayerStatePacket packet = event.getPacket();
                            switch (packet.getState()) {
                                case START_SNEAKING:
                                    player.setSneaking(true);
                                    break;
                                case STOP_SNEAKING:
                                    player.setSneaking(false);
                                    break;
                                case START_SPRINTING:
                                    player.setSprinting(true);
                                    break;
                                case STOP_SPRINTING:
                                    player.setSprinting(false);
                                    break;
                                case RIDING_JUMP:
                                case OPEN_INVENTORY:
                                case LEAVE_BED:
                                    break;
                            }
                        } else if (event.getPacket() instanceof ClientSwingArmPacket) {
                            Player player = getPlayer(event.getSession().<GameProfile>getFlag(ProtocolConstants.PROFILE_KEY).getId());
                            long chunk = player.getChunk();
                            for (Player p : getPlayers())
                                if (p.getWorld().equals(player.getWorld()) && p.isColumnLoaded(chunk) && !p.getUniqueID().equals(player.getUniqueID()))
                                    p.sendPacket(new ServerAnimationPacket(player.getEntityID(), Animation.SWING_ARM));
                        } else if (event.getPacket() instanceof ClientChatPacket) {
                            Player player = getPlayer(event.getSession().<GameProfile>getFlag(ProtocolConstants.PROFILE_KEY).getId());
                            ClientChatPacket packet = event.getPacket();
                            if (packet.getMessage().startsWith("/")) {
                                if (packet.getMessage().equals("/"))
                                    player.sendMessage("&cCommand does not exist!");
                                else
                                    try {
                                        playerCommandEventSource.fireEvent(player, packet);
                                    } catch (ClassNotFoundException e) {
                                        player.sendMessage("&cUnknown Command");
                                    }
                            } else
                                playerChatEventSource.fireEvent(player, packet);
                        } else if (event.getPacket() instanceof ClientPlayerInteractEntityPacket) {
                            Player player = getPlayer(event.getSession().<GameProfile>getFlag(ProtocolConstants.PROFILE_KEY).getId());
                            ClientPlayerInteractEntityPacket packet = event.getPacket();
                            Entity ent = player.getWorld().getEntityFromID(packet.getEntityId());
                            if (ent == null) {
                                for (Player p : MCThunder.getPlayers())
                                    if (p.getEntityID() == packet.getEntityId()) {
                                        ent = p;
                                        break;
                                    }
                                if (ent == null)
                                    for (int i = 0; i < bots.size(); i++)
                                        if (bots.get(i).getEntityID() == packet.getEntityId()) {
                                            ent = bots.get(i);
                                            break;
                                        }
                            }
                            switch (packet.getAction()) {
                                case INTERACT:
                                    //player.sendMessage("INTERACT/INTERACTAT");
                                    break;
                                case ATTACK:
                                    try {
                                        playerAttackEntityEventSource.fireEvent(player, ent);
                                    } catch (ClassNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case INTERACT_AT:
                                    //player.sendMessage("You have attacked an entity");
                                    break;
                            }

                        } else if (event.getPacket() instanceof ClientKeepAlivePacket)
                            event.getSession().send(new ServerKeepAlivePacket(event.<ClientKeepAlivePacket>getPacket().getPingId()));
                        else if (event.getPacket() instanceof ClientSettingsPacket) {
                            Player player = getPlayer(event.getSession().<GameProfile>getFlag(ProtocolConstants.PROFILE_KEY).getId());
                            ClientSettingsPacket packet = event.getPacket();
                            if (player.getView() != packet.getRenderDistance()) {
                                player.setView(packet.getRenderDistance());
                                //TODO: unload chunks if goes smaller load if goes higher also have it check during login what their distance is
                            }
                        } else if (event.getPacket() instanceof ClientTabCompletePacket)
                            tabHandler.handleTabComplete(event.getSession(), (ClientTabCompletePacket) event.getPacket());
                        else if (event.getPacket() instanceof ClientCreativeInventoryActionPacket) {
                            Player player = getPlayer(event.getSession().<GameProfile>getFlag(ProtocolConstants.PROFILE_KEY).getId());
                            ClientCreativeInventoryActionPacket packet = event.getPacket();
                            //tellConsole(LoggingLevel.DEBUG, packet.getSlot());
                            ItemStack old = player.getHeldItem();
                            player.getInventory().setSlot(packet.getSlot(), new ItemStack(packet.getClickedItem() == null ? Material.AIR :
                                    Material.fromData(packet.getClickedItem().getId(), (short) packet.getClickedItem().getData()),
                                    packet.getClickedItem() == null ? 0 : packet.getClickedItem().getAmount(), packet.getClickedItem() == null ?
                                    null : packet.getClickedItem().getNBT()));
                            if (packet.getSlot() == player.getSlot() && !old.equals(player.getHeldItem())) {
                                Packet pack = new ServerEntityEquipmentPacket(player.getEntityID(), 0, player.getHeldItem().getItemStack());
                                long chunk = player.getChunk();
                                for (Player p : getPlayers())
                                    if (p.getWorld().equals(player.getWorld()) && !player.getUniqueID().equals(p.getUniqueID()) && p.isColumnLoaded(chunk))
                                        p.sendPacket(pack);
                            }
                        } else if (event.getPacket() instanceof ClientWindowActionPacket) {
                            ClientWindowActionPacket packet = event.getPacket();
                            //tellConsole(LoggingLevel.DEBUG, packet.getSlot() + " " + packet.getAction().name() + " " + packet.getWindowId());
                            //0 = click craft output
                            //1-4 = craft input
                            //5-8 = helm to boots
                            //9-35 = inventory
                            //36-44 = hotbar
                            //-999 = outside of inventory for dropping items
                            //TODO: logic for this as well as for when they hit like a number over the item to move the item
                            //TODO: Make it actually count for updating held item
                        } else if (event.getPacket() instanceof ClientChangeHeldItemPacket) {
                            Player player = getPlayer(event.getSession().<GameProfile>getFlag(ProtocolConstants.PROFILE_KEY).getId());
                            ClientChangeHeldItemPacket packet = event.getPacket();
                            ItemStack old = player.getHeldItem();
                            player.setSlot(packet.getSlot() + 36);
                            if (!old.equals(player.getHeldItem())) {
                                Packet pack = new ServerEntityEquipmentPacket(player.getEntityID(), 0, player.getHeldItem().getItemStack());
                                long chunk = player.getChunk();
                                for (Player p : getPlayers())
                                    if (p.getWorld().equals(player.getWorld()) && !player.getUniqueID().equals(p.getUniqueID()) && p.isColumnLoaded(chunk))
                                        p.sendPacket(pack);
                            }
                        } else if (event.getPacket() instanceof ClientConfirmTransactionPacket) {
                            ClientConfirmTransactionPacket p = event.getPacket();
                            tellConsole(LoggingLevel.DEBUG, p.getAccepted());
                            tellConsole(LoggingLevel.DEBUG, p.getWindowId());
                            tellConsole(LoggingLevel.DEBUG, p.getActionId());
                        } else if (event.getPacket() instanceof ClientPlayerPlaceBlockPacket) {
                            Player player = getPlayer(event.getSession().<GameProfile>getFlag(ProtocolConstants.PROFILE_KEY).getId());
                            ClientPlayerPlaceBlockPacket packet = event.getPacket();
                            try {
                                playerPlaceBlockEventSource.fireEvent(player, packet);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }

                        } else if (event.getPacket() instanceof ClientPlayerActionPacket) {
                            Player player = getPlayer(event.getSession().<GameProfile>getFlag(ProtocolConstants.PROFILE_KEY).getId());
                            ClientPlayerActionPacket packet = event.getPacket();
                            if ((packet.getAction().equals(PlayerAction.START_DIGGING) && player.getGameMode().equals(GameMode.CREATIVE)) ||
                                    (player.getGameMode().equals(GameMode.SURVIVAL) && packet.getAction().equals(PlayerAction.FINISH_DIGGING))) {
                                try {
                                    playerBreakBlockEventSource.fireEvent(player, packet);
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }

                            } else if (packet.getAction().equals(PlayerAction.DROP_ITEM) || packet.getAction().equals(PlayerAction.DROP_ITEM_STACK)) {
                                if (player.getHeldItem().getType() != Material.AIR) {
                                    DroppedItem di = new DroppedItem(player.getLocation(), new ItemStack(player.getHeldItem().getType(), 1));
                                    di.setAge((short) -20);
                                    player.getWorld().loadEntity(di);
                                    Inventory i = player.getInventory();
                                    if (i.getItemAt(player.getSlot()).getAmount() > 1) {
                                        i.getItemAt(player.getSlot()).setAmount(player.getHeldItem().getAmount() - 1);
                                        player.setInventory(i);
                                    } else if (i.getItemAt(player.getSlot()).getAmount() == 1) {
                                        i.getItemAt(player.getSlot()).setType(Material.AIR);
                                        player.setInventory(i);
                                    }
                                }
                            }
                        } else if (event.getPacket() != null)
                            tellConsole(LoggingLevel.DEBUG, event.getPacket());
                    }
                });
            }

            @Override
            public void sessionRemoved(SessionRemovedEvent event) {
                if (((org.spacehq.mc.protocol.MinecraftProtocol) event.getSession().getPacketProtocol()).getMode() == ProtocolMode.GAME) {
                    Player player = getPlayer(event.getSession().<GameProfile>getFlag(ProtocolConstants.PROFILE_KEY).getId());
                    broadcast("&7&o" + player.getName() + " disconnected");
                    entryListHandler.removeFromList(player);
                    ServerDestroyEntitiesPacket destroyEntitiesPacket = new ServerDestroyEntitiesPacket(player.getEntityID());
                    for (Player p : getPlayers())
                        p.sendPacket(destroyEntitiesPacket);
                    player.setAppended("");
                    player.unloadChunks();
                    player.saveToFile();
                    players.remove(player.getUniqueID());
                }
            }
        });

        server.bind();
        while (server.isListening()) {
            try {
                long startTime = System.currentTimeMillis();
                //TODO: put things to happen each tick such as either physics checks ect.
                //TODO: Add a way to register events to happen after certain number of ticks will be implemented in prep for physics engine
                for (World w : getWorlds())
                    if (!w.getEntities().isEmpty())
                        try {
                            for (Entity e : w.getEntities()) {
                                if (e instanceof LivingEntity) {
                                    LivingEntity l = (LivingEntity) e;
                                    if (l instanceof Ageable) {
                                        Ageable a = (Ageable) l;
                                        a.setAge((byte) (a.getAge() + 1));
                                    }
                                    ArrayList<PotionEffectType> toRem = new ArrayList<>();
                                    for (PotionEffect p : l.getActiveEffects())
                                        if (p.getDuration() > 1)
                                            p.setDuration(p.getDuration() - 1);
                                        else
                                            toRem.add(p.getType());
                                    for (PotionEffectType t : toRem)
                                        l.removePotionEffect(t);
                                    l.ai();
                                } else if (e instanceof ExperienceOrb) {
                                    ExperienceOrb x = (ExperienceOrb) e;
                                    x.setAge((byte) (x.getAge() + 1));
                                } else if (e instanceof DroppedItem) {
                                    DroppedItem d = (DroppedItem) e;
                                    d.setAge((byte) (d.getAge() + 1));
                                } else if (e instanceof FallingSand) {
                                    FallingSand f = (FallingSand) e;
                                    f.setTime((byte) (f.getTime() + 1));
                                }
                            }
                        } catch (Exception ignored) {
                        }//Entity added at exact same time it checked for entities
                for (int i = 0; i < bots.size(); i++)
                    bots.get(i).ai();
                for (Player p : getPlayers()) {
                    ArrayList<PotionEffectType> toRem = new ArrayList<>();
                    for (PotionEffect e : p.getActiveEffects())
                        if (e.getDuration() > 1)
                            e.setDuration(e.getDuration() - 1);
                        else
                            toRem.add(e.getType());
                    for (PotionEffectType t : toRem)
                        p.removePotionEffect(t);
                }
                long time = 50 + startTime - System.currentTimeMillis();
                if (time > 0)
                    Thread.sleep(time);//Wait for next tick after everything finishes
            } catch (InterruptedException ignored) {
            }
        }
    }

    private static void updatePlayerPosition(Session session, ClientPlayerMovementPacket packet) {
        GameProfile profile = session.getFlag(ProtocolConstants.PROFILE_KEY);
        Player player = getPlayer(profile.getId());
        if (player.getLocation() == null)
            player.setLocation(getWorld(conf.getWorldName()).getSpawnLocation());

        if (packet instanceof ClientPlayerPositionPacket || packet instanceof ClientPlayerPositionRotationPacket) {
            int fromChunkX = (int) player.getLocation().getX() >> 4;
            int fromChunkZ = (int) player.getLocation().getZ() >> 4;
            int toChunkX = (int) packet.getX() >> 4;
            int toChunkZ = (int) packet.getZ() >> 4;
            player.setX(packet.getX());
            player.setY(packet.getY());
            player.setZ(packet.getZ());
            if (fromChunkX != toChunkX || fromChunkZ != toChunkZ)
                player.loadChunks(fromChunkZ - toChunkZ > 0 && fromChunkX - toChunkX < 0 ? Direction.NORTH_EAST : fromChunkZ - toChunkZ < 0 &&
                        fromChunkX - toChunkX < 0 ? Direction.SOUTH_EAST : fromChunkZ - toChunkZ < 0 && fromChunkX - toChunkX > 0 ?
                        Direction.SOUTH_WEST : fromChunkZ - toChunkZ > 0 && fromChunkX - toChunkX > 0 ? Direction.NORTH_WEST :
                        fromChunkZ - toChunkZ > 0 ? Direction.NORTH : fromChunkX - toChunkX < 0 ? Direction.EAST :
                                fromChunkZ - toChunkZ < 0 ? Direction.SOUTH : fromChunkX - toChunkX > 0 ? Direction.WEST : null);
        }

        if (packet instanceof ClientPlayerRotationPacket || packet instanceof ClientPlayerPositionRotationPacket) {
            player.setPitch((float) packet.getPitch());
            player.setYaw((float) packet.getYaw());
        }
        player.setOnGround(packet.isOnGround());
    }

    public static List<String> getCommandPaths() {
        return commandPaths;
    }

    public static void addCommandPath(String path) {
        if (!path.contains(".")) {
            tellConsole(LoggingLevel.ERROR, path + " is not a valid command path, please check your plugins!");
        } else {
            commandPaths.add(path);

        }
    }

    private static List<Packet> createUpdatePackets(Session session, ClientPlayerMovementPacket packet) {
        List<Packet> packets = new ArrayList<>();
        GameProfile profile = session.getFlag(ProtocolConstants.PROFILE_KEY);
        Player player = getPlayer(profile.getId());
        player.setOnGround(packet.isOnGround());
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
                packets.add(new ServerEntityTeleportPacket(player.getEntityID(), packet.getX(), packet.getY(), packet.getZ(), yaw, pitch, player.isOnGround()));
            else if (packet instanceof ClientPlayerPositionPacket)
                packets.add(new ServerEntityPositionPacket(player.getEntityID(), movedX, movedY, movedZ, player.isOnGround()));
            else {//ClientPlayerPositionRotationPacket
                packets.add(new ServerEntityPositionRotationPacket(player.getEntityID(), movedX, movedY, movedZ, yaw, pitch, player.isOnGround()));
                packets.add(new ServerEntityHeadLookPacket(player.getEntityID(), yaw));
            }
        } else if (packet instanceof ClientPlayerRotationPacket) {
            float yaw = (float) packet.getYaw();
            packets.add(new ServerEntityRotationPacket(player.getEntityID(), yaw, (float) packet.getPitch(), player.isOnGround()));
            packets.add(new ServerEntityHeadLookPacket(player.getEntityID(), yaw));
        }

        return packets;
    }

    public static Server getServer() {
        return server;
    }

    public static Player getPlayer(String name) {//Add a search by display name
        if (name == null)
            return null;
        name = name.trim();
        Player partial = null;
        for (Player p : getPlayers())
            if (p.getName().equalsIgnoreCase(name))
                return p;
            else if (partial == null && (p.getName().toLowerCase().contains(name.toLowerCase()) || p.getDisplayName().toLowerCase().contains(name.toLowerCase())))
                partial = p;
        return partial;
    }

    public static Player getPlayer(UUID uuid) {
        return players.get(uuid);
    }

    public static int maxRenderDistance() {
        return RENDER_DISTANCE;
    }

    public static int getPort() {
        return PORT;
    }

    public static boolean getGuiMode() {
        return guiMODE;
    }

    public static String getIp() {
        return HOST;
    }

    public static String getServerName() {
        return serverName;
    }

    public static void shutdown(String args) {
        for (Player p : getPlayers())
            p.disconnect(args);
        for (World w : getWorlds())
            w.unloadWorld();
        for (int i = 0; i < bots.size(); i++)
            bots.get(i).unload();
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
        return players.values();
    }

    public static void addPlayer(Player player) {
        players.put(player.getUniqueID(), player);
    }

    public static HashMap getPlayerHashmap() {
        return players;
    }

    public static ServerPlayerEntryListHandler getEntryListHandler() {
        return entryListHandler;
    }

    public static void addToPlayerEntryList(Player player) {
        entryListHandler.addToList(player);
    }

    public static Collection<Bot> getBots() {
        return bots;
    }

    public static World getWorld(String name) {
        return worlds.get(name.toLowerCase());
    }

    public static Collection<World> getWorlds() {
        return worlds.values();
    }

    public static Config getConfig() {
        return conf;
    }

    public static Window getGui() {
        return window;
    }

    public static RankManager getRankManager() {
        return rankManager;
    }

    public static void addLoginEventListener(PlayerLoggingInEventListenerInterface listener) {
        loggingInEventSource.addEventListener(listener);
    }

    public static void addCommandEventListener(PlayerCommandEventListenerInterface listener) {
        playerCommandEventSource.addEventListener(listener);
    }

    public static void addPlayerEntityAttackListener(PlayerAttackEntityEventListenerInterface listener) {
        playerAttackEntityEventSource.addEventListener(listener);
    }

    public static PlayerProfileHandler getProfileHandler() {
        return playerProfileHandler;
    }

    public static void loadBot(Bot b) {
        if (bots.contains(b))
            return;
        bots.add(b);
        entryListHandler.addToList(b);
        b.load();
    }

    public static void unloadBot(Bot b) {
        if (!bots.contains(b))
            return;
        entryListHandler.removeFromList(b);
        b.unload();
        bots.remove(b);
    }

    public static MetadataChangeEventSource getMetadataChangeEventSource() {
        return metadataChangeEventSource;
    }

    public static void loadWorld(File f) {
        File level = new File(f.getPath() + "/level.dat");
        if (!level.exists()) {
            File[] files = f.listFiles();
            if (files != null)
                for (File file : files)
                    if (!file.isFile())
                        loadWorld(file);
        } else {
            worlds.put(f.getName().toLowerCase(), new World(f.getName(), f.getPath()));
            try {
                worlds.get(f.getName().toLowerCase()).loadWorld();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}