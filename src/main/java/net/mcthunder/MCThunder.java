package net.mcthunder;

import net.mcthunder.api.*;
import net.mcthunder.block.Block;
import net.mcthunder.entity.*;
import net.mcthunder.events.listeners.MetadataChangeEventListener;
import net.mcthunder.events.listeners.PlayerChatEventListener;
import net.mcthunder.events.listeners.PlayerCommandEventListener;
import net.mcthunder.events.listeners.PlayerLoggingInEventListener;
import net.mcthunder.events.source.MetadataChangeEventSource;
import net.mcthunder.events.source.PlayerChatEventSource;
import net.mcthunder.events.source.PlayerCommandEventSource;
import net.mcthunder.events.source.PlayerLoggingInEventSource;
import net.mcthunder.handlers.PlayerProfileHandler;
import net.mcthunder.handlers.ServerChatHandler;
import net.mcthunder.handlers.ServerPlayerEntryListHandler;
import net.mcthunder.handlers.ServerTabHandler;
import net.mcthunder.inventory.*;
import net.mcthunder.material.Material;
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
import org.spacehq.mc.protocol.data.game.Position;
import org.spacehq.mc.protocol.data.game.values.Face;
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
import java.util.*;

import static net.mcthunder.api.Utils.*;

/**
 * Created by Kevin on 8/9/2014.
 */
public class MCThunder {
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
    private static RankManager rankManager;

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
        Achievement.mapAchievements();
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
        playerChatEventSource = new PlayerChatEventSource();
        playerCommandEventSource = new PlayerCommandEventSource();
        loggingInEventSource = new PlayerLoggingInEventSource();
        metadataChangeEventSource = new MetadataChangeEventSource();
        playerChatEventSource.addEventListener(defaultPlayerChatEventListener);
        playerCommandEventSource.addEventListener(defaultPlayerCommandEventListener);
        loggingInEventSource.addEventListener(loggingInEventListener);
        metadataChangeEventSource.addEventListener(metadataChangeEventListener);
        if (conf.getUseRankManager()) {
           rankManager = new RankManager();
            rankManager.load();
        }

        players = new HashMap<>(conf.getSlots());
        worlds = new HashMap<>();
        bots = new ArrayList<>();
        //Load the world files and check for subdirectories recursively if it is not a world folder
        File dir = new File("worlds");
        File[] files = dir.listFiles();
        if (files != null)
            for (File f : files)
                if (f.exists())
                    loadWorld(f);

        server.setGlobalFlag(ProtocolConstants.VERIFY_USERS_KEY, conf.getOnlineMode());
        server.setGlobalFlag(ProtocolConstants.SERVER_COMPRESSION_THRESHOLD, 256);//Default is 256 not 100
        server.setGlobalFlag(ProtocolConstants.SERVER_INFO_BUILDER_KEY, new ServerInfoBuilder() {
            @Override
            public ServerStatusInfo buildInfo(Session session) {
                GameProfile[] gameProfiles = new GameProfile[players.size() + bots.size()];
                int i = 0;
                for (Player p : getPlayers())
                    gameProfiles[i++] = p.getGameProfile();
                for (Bot b : getBots())
                    gameProfiles[i++] = b.getGameProfile();
                BufferedImage icon = null;
                try {
                    icon = ImageIO.read(new File("server-icon.png"));
                } catch (Exception ignored) { }//When there is no icon set
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
                                case RIDING_JUMP: case OPEN_INVENTORY: case LEAVE_BED:
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
                            Position position = packet.getPosition();
                            ItemStack heldItem = packet.getHeldItem() == null ? null : new ItemStack(Material.fromData(packet.getHeldItem().getId(),
                                    (short) packet.getHeldItem().getData()), packet.getHeldItem().getAmount());
                            if ((position.getY() >> 4) < 0)
                                return;
                            Block b = new Block(new Location(player.getWorld(), position));
                            if (!player.isSneaking()) {
                                Inventory inv = b.getType().equals(Material.CHEST) || b.getType().equals(Material.TRAPPED_CHEST) ?
                                        player.getWorld().getChest(b.getLocation()).getInventory() : b.getType().equals(Material.ENDER_CHEST) ?
                                        player.getEnderChest() : b.getType().equals(Material.CRAFTING_TABLE) ? new CraftingInventory("Crafting") :
                                        b.getType().getParent().equals(Material.HOPPER) ? new HopperInventory("Hopper") :
                                        b.getType().equals(Material.BEACON) ? new BeaconInventory("Beacon") :
                                        b.getType().getParent().equals(Material.ANVIL) ? new AnvilInventory("Anvil") :
                                        b.getType().equals(Material.BREWING_STAND_BLOCK) ? new BrewingStandInventory("BrewingStand") :
                                        b.getType().equals(Material.DISPENSER) ? new DispenserInventory("Dispenser") :
                                        b.getType().equals(Material.DROPPER) ? new DropperInventory("Dropper") :
                                        b.getType().equals(Material.FURNACE) ? new FurnaceInventory("Furnace") :
                                        b.getType().equals(Material.ENCHANTING_TABLE) ? new EnchantingInventory("Enchanting") : null;
                                if (inv != null) {
                                    player.openInventory(inv);
                                    return;
                                }
                            }
                            if (heldItem == null)
                                return;
                            if (!b.getType().isLiquid() && !b.getType().equals(Material.SNOW_LAYER) && !b.getType().isLongGrass())
                                b = b.getRelative(Direction.fromFace(packet.getFace()));
                            if (!b.getType().equals(Material.AIR))
                                return;
                            Location clicked = new Location(player.getWorld(), b.getLocation().getX() + packet.getCursorX(), b.getLocation().getY() +
                                    1 - (packet.getCursorY() == 0 ? 1 : packet.getCursorY()), b.getLocation().getZ() + packet.getCursorZ());
                            Material setType = heldItem.getType();
                            if (setType == null)
                                return;
                            if (setType.getParent().equals(Material.TORCH) || setType.getParent().equals(Material.REDSTONE_TORCH))//TODO: Check if is a valid torch position
                                setType = packet.getFace().equals(Face.SOUTH) ? Material.fromString("EAST_" + setType.getParent()) :
                                        packet.getFace().equals(Face.NORTH) ? Material.fromString("WEST_" + setType.getParent()) :
                                        packet.getFace().equals(Face.WEST) ? Material.fromString("SOUTH_" + setType.getParent()) :
                                        packet.getFace().equals(Face.EAST) ? Material.fromString("NORTH_" + setType.getParent()) :
                                        Material.fromString("UP_" + setType.getParent());
                            if (setType.equals(Material.REDSTONE))
                                setType = Material.REDSTONE_WIRE;
                            if (setType.equals(Material.STRING))
                                setType = Material.TRIPWIRE;
                            if (Material.fromString(setType.getName() + "_BLOCK") != null && !setType.equals(Material.BROWN_MUSHROOM) && !setType.equals(Material.RED_MUSHROOM) &&
                                    !setType.equals(Material.MELON))
                                setType = Material.fromString(setType.getName() + "_BLOCK");
                            String name = setType.getName().replace("_UP", "");
                            if ((packet.getFace().equals(Face.BOTTOM) || packet.getFace().equals(Face.TOP)) && Material.fromString(name + "_UP") != null)
                                setType = Material.fromString(name + "_UP");
                            else if ((packet.getFace().equals(Face.NORTH) || packet.getFace().equals(Face.SOUTH)) && Material.fromString(name + "_EAST") != null)
                                setType = Material.fromString(name + "_EAST");
                            else if ((packet.getFace().equals(Face.EAST) || packet.getFace().equals(Face.WEST)) && Material.fromString(name + "_NORTH") != null)
                                setType = Material.fromString(name + "_NORTH");
                            if (setType.getName().contains("BUCKET"))
                                setType = Material.fromString(setType.getName().replace("_BUCKET", ""));
                            if (setType.getParent().equals(Material.SPAWN_EGG))
                                clicked.getWorld().loadEntity(Entity.fromType(EntityType.fromString(setType.getName().replaceFirst("SPAWN_", "")), clicked));
                            else if (setType.equals(Material.ARMOR_STAND))
                                clicked.getWorld().loadEntity(Entity.fromType(EntityType.ARMOR_STAND, clicked));
                            else
                                b.setType(setType);
                        } else if (event.getPacket() instanceof ClientPlayerActionPacket) {
                            Player player = getPlayer(event.getSession().<GameProfile>getFlag(ProtocolConstants.PROFILE_KEY).getId());
                            ClientPlayerActionPacket packet = event.getPacket();
                            if ((packet.getAction().equals(PlayerAction.START_DIGGING) && player.getGameMode().equals(GameMode.CREATIVE)) ||
                                    (player.getGameMode().equals(GameMode.SURVIVAL) && packet.getAction().equals(PlayerAction.FINISH_DIGGING))) {
                                Block b = new Block(new Location(player.getWorld(), packet.getPosition()));
                                if (player.getGameMode().equals(GameMode.SURVIVAL))
                                    player.getWorld().loadEntity(new DroppedItem(player.getLocation(), new ItemStack(b.getType(), 1)));
                                b.setType(Material.AIR);
                            } else if (packet.getAction().equals(PlayerAction.DROP_ITEM) || packet.getAction().equals(PlayerAction.DROP_ITEM_STACK)) {
                                player.getWorld().loadEntity(new DroppedItem(new Location(player.getWorld(), packet.getPosition()), player.getHeldItem()));
                                //TODO: Remove from inventory as well as only drop 1 when its not a full stack
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
                        } catch (Exception ignored) { }//Entity added at exact same time it checked for entities
                for (Bot b : getBots())
                    b.ai();
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
            } catch (InterruptedException ignored) { }
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
        for (Bot b : getBots())
            b.unload();
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

    public static RankManager getRankManager(){return rankManager;}

    public static void addLoginEventListener(net.mcthunder.interfaces.PlayerLoggingInEventListener listener) {
        loggingInEventSource.addEventListener(listener);
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