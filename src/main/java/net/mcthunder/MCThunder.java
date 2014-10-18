package net.mcthunder;


//MCThunder Code Imports 

import net.mcthunder.apis.Command;
import net.mcthunder.apis.CommandRegistry;
import net.mcthunder.apis.Config;
import net.mcthunder.apis.Player;
import net.mcthunder.events.listeners.PlayerChatEventListener;
import net.mcthunder.events.listeners.PlayerCommandEventListener;
import net.mcthunder.events.source.PlayerChatEventSource;
import net.mcthunder.events.source.PlayerCommandEventSource;
import net.mcthunder.handlers.PlayerProfileHandler;
import net.mcthunder.handlers.ServerChatHandler;
import net.mcthunder.handlers.ServerPlayerEntryListHandler;
import net.mcthunder.handlers.ServerTabHandler;
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
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import org.spacehq.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import org.spacehq.packetlib.Server;
import org.spacehq.packetlib.Session;
import org.spacehq.packetlib.event.server.ServerAdapter;
import org.spacehq.packetlib.event.server.SessionAddedEvent;
import org.spacehq.packetlib.event.server.SessionRemovedEvent;
import org.spacehq.packetlib.event.session.PacketReceivedEvent;
import org.spacehq.packetlib.event.session.PacketSentEvent;
import org.spacehq.packetlib.event.session.SessionAdapter;
import org.spacehq.packetlib.packet.Packet;
import org.spacehq.packetlib.tcp.TcpSessionFactory;

import java.util.*;

import static net.mcthunder.apis.Utils.*;

//MCPacketLib Imports
//Java Imports
//Misc Library Imports
//Static Mist Imports

/**
 * Created by Kevin on 8/9/2014.
 */
public class MCThunder {
    public static HashMap<UUID, Player> playerHashMap;
    private static Config conf;
    private static String serverName;
    private static boolean SPAWN_SERVER = true;
    private static boolean VERIFY_USERS = false;
    private static String HOST;
    private static int PORT;
    private static MinecraftProtocol protocol;
    private static List<Session> sessionsList;
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
        tellConsole("INFO", "INTERNAL PORT " + HOST);

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

            server.setGlobalFlag(ProtocolConstants.VERIFY_USERS_KEY, VERIFY_USERS);
            server.setGlobalFlag(ProtocolConstants.SERVER_COMPRESSION_THRESHOLD, 100);
            server.setGlobalFlag(ProtocolConstants.SERVER_INFO_BUILDER_KEY, new ServerInfoBuilder() {
                @Override
                public ServerStatusInfo buildInfo(Session session) {
                    sessionsList = server.getSessions();
                    return new ServerStatusInfo(new VersionInfo(ProtocolConstants.GAME_VERSION, ProtocolConstants.PROTOCOL_VERSION), new PlayerInfo(conf.getSlots(), playerHashMap.size(), new GameProfile[0]), new TextMessage("Hello world!"), null);
                }
            });

            server.setGlobalFlag(ProtocolConstants.SERVER_LOGIN_HANDLER_KEY, new ServerLoginHandler() {
                @Override
                public void loggedIn(Session session) {
                    GameProfile profile = session.getFlag(ProtocolConstants.PROFILE_KEY);
					if(playerHashMap.containsKey(profile.getId()))
						playerHashMap.get(profile.getId()).getSession().disconnect("You logged in from another location!");

                    int entityID = (int) Math.ceil(Math.random() * Integer.MAX_VALUE);
                    EntityMetadata metadata = new EntityMetadata(2, MetadataType.STRING, profile.getName());
                    playerHashMap.put(profile.getId(), new Player(server, session, profile, entityID, 0, metadata));

                    Player player = playerHashMap.get(profile.getId());

                    player.getSession().send(new ServerJoinGamePacket(0, false, GameMode.CREATIVE, 0, Difficulty.PEACEFUL, 10, WorldType.DEFAULT, false));
                    tellConsole("INFO", "User " + player.gameProfile().getName() + " is trying to log in!");
                    entryListHandler.addToPlayerEntryList(server, session);
                    //Send World Data
                    player.getSession().send(new ServerPlayerPositionRotationPacket(0, 24, 0, 0, 0));

                    /**byte[] light = new byte[4096];
                    Arrays.fill(light, (byte) 15);
                    Chunk[] chunks = new Chunk[16];
                    for (int i = 0; i < chunks.length; i++) {
                        NibbleArray3d blocklight = new NibbleArray3d(light);
                        NibbleArray3d skylight = new NibbleArray3d(light);
                     ShortArray3d blocks = new ShortArray3d(4098);
                        for (int cY = 0; cY < 24; cY++) {
                            for (int cZ = 0; cZ < 16; cZ++) {
                                for (int cX = 0; cX < 16; cX++) {
                                    int y = cY - i * 16;
                     if ((cY < (i + 1) * 16) && (cY > i * 16)) {
                                        if (cY < 1) {
                                            blocks.setBlock(cX, y, cZ, 7);
                                        } else if (cY < 23) {
                     blocks.setBlock(cX, y, cZ, 3 );
                     } else if (cY > 23){
                     blocks.setBlock(cX, y, cZ, 57);
                     }

                     else{
                                            blocks.setBlock(cX, y, cZ, 2);
                                        }
                     }
                                }
                            }
                        }

                        Chunk chunk = new Chunk(blocks, blocklight, skylight);
                        chunks[i] = chunk;
                    }


                     }*/
                    byte[] light = new byte[4096]; //Create a light array of bytes (actually nibbles) (should this be 2048)
                    Arrays.fill(light, (byte) 15); //fill up the light array with full light (16 at 0 indexed)
                    Chunk[] chunks = new Chunk[16];

                    for (int i = 0; i < chunks.length; i++) { //Loop through all 16 chunks (verticle fashion)
                        NibbleArray3d blocklight = new NibbleArray3d(light); //Create our blocklight array
                        NibbleArray3d skylight = new NibbleArray3d(light); //Create our skylight array
                        ShortArray3d blocks = new ShortArray3d(4096); //Array containing the blocks of THIS sub-chunk only!

                        for (int cY = 0; cY < 16; cY++) //Loop through the Y axis
                            for (int cZ = 0; cZ < 16; cZ++) //Loop through z
                                for (int cX = 0; cX < 16; cX++) { //Loop through x
                                    int y = cY + i * 16; //Get our ABSOLUTE y coordinate (used only to check our literal position)

                                    if (y == 0) //lowest point
                                        blocks.setBlock(cX, cY, cZ, 7); //Adminium
                                    else if (y <= 23) //less than 24 but above 0
                                        blocks.setBlock(cX, cY, cZ, 3); //Dirt
                                    else if (y == 24) //Exactly 24
                                        blocks.setBlock(cX, cY, cZ, 2); //Grass
                                }
                        Chunk chunk = new Chunk(blocks, blocklight, skylight);
                        chunks[i] = chunk;
                    }
                    for (int x = -5; x <= 5; x++)
                        for (int z = -5; z <= 5; z++)
                            player.getSession().send(new ServerChunkDataPacket(x, z, chunks, new byte[256]));
                    player.getSession().send(new ServerSpawnPositionPacket(new Position(0, 24, 0)));

                    player.getChatHandler().sendMessage(server, profile.getName() + " has joined " + serverName);
                    playerProfileHandler.checkPlayer(player);

                    ServerSpawnPlayerPacket toAllPlayers = new ServerSpawnPlayerPacket(player.getEntityID(), player.gameProfile().getId(), 0, 24, 0, player.getYaw(), player.getPitch(), player.getHeldItem(), player.getMetadata().getMetadataArray());
                    for (Player player1 : playerHashMap.values()) {
                        ServerSpawnPlayerPacket toNewPlayer = new ServerSpawnPlayerPacket(player1.getEntityID(), player1.gameProfile().getId(), player1.getX(), player1.getY(), player1.getZ(), player1.getYaw(), player1.getPitch(), player1.getHeldItem(), player1.getMetadata().getMetadataArray());
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
								for(Packet packet : createUpdatePackets(event.getSession(), pack))
									for(Player p : playerHashMap.values())
										if(!p.gameProfile().getName().equals(event.getSession().<GameProfile>getFlag(ProtocolConstants.PROFILE_KEY).getName()))
											p.getSession().send(packet);

								updatePlayerPosition(event.getSession(), pack);
							} else if(event.getPacket() instanceof ClientPlayerStatePacket) {
								ClientPlayerStatePacket packet = event.getPacket();
								GameProfile profile = event.getSession().getFlag(ProtocolConstants.PROFILE_KEY);
								Player player = playerHashMap.get(profile.getId());
								switch(packet.getState()) {
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
							} else if(event.getPacket() instanceof ClientSwingArmPacket) {
								GameProfile profile = event.getSession().getFlag(ProtocolConstants.PROFILE_KEY);
								Player player = playerHashMap.get(profile.getId());
								for(Player p : playerHashMap.values())
									if(!p.gameProfile().getName().equals(profile.getName()))
										p.getSession().send(new ServerAnimationPacket(player.getEntityID(), Animation.SWING_ARM));
                            } else if (event.getPacket() instanceof ClientChatPacket) {
                                ClientChatPacket packet = event.getPacket();
                                GameProfile profile = event.getSession().getFlag(ProtocolConstants.PROFILE_KEY);
                                Player player = playerHashMap.get(profile.getId());
                                if (packet.getMessage().startsWith("/")) {
                                    if (packet.getMessage().equals("/"))
                                        chatHandler.sendPrivateMessage(event.getSession(), "Command does not exist!");
                                    else
                                        try {
                                            playerCommandEventSource.fireEvent(player, packet);
                                        } catch (ClassNotFoundException e) {
                                            player.sendMessageToPlayer("Unknown Command");
                                        }
                                } else
                                    playerChatEventSource.fireEvent(player, packet);

                                //chatHandler.handleChat(server, event.getSession(), packet, sessionsList);
                            } else if (event.getPacket() instanceof ClientKeepAlivePacket)
								event.getSession().send(new ServerKeepAlivePacket(event.<ClientKeepAlivePacket>getPacket().getPingId()));
                            else if (event.getPacket() instanceof ClientSettingsPacket) {

                            } else if (event.getPacket() instanceof ClientTabCompletePacket) {
                                ClientTabCompletePacket packet = event.getPacket();
                                tabHandler.handleTabComplete(server, event.getSession(), packet);
                            } else if (event.getPacket() != null)
                                tellConsole("DEBUG", event.getPacket().toString());
                            else
                                tellConsole("DEBUG", "null packet");
                        }

                        @Override
                        public void packetSent(PacketSentEvent event) {
                            if (event.getPacket() instanceof ServerJoinGamePacket) {

                            }
                        }
                    });
                }


                @Override
                public void sessionRemoved(SessionRemovedEvent event) {
                    if (((MinecraftProtocol) event.getSession().getPacketProtocol()).getMode() == ProtocolMode.GAME) {
                        GameProfile profile = event.getSession().getFlag(ProtocolConstants.PROFILE_KEY);
                        Player player = playerHashMap.get(profile.getId());
                        player.getChatHandler().sendMessage(server, profile.getName() + " has left " + conf.getServerName());
                        entryListHandler.deleteFromPlayerEntryList(server, event.getSession());
                        ServerDestroyEntitiesPacket destroyEntitiesPacket = new ServerDestroyEntitiesPacket(player.getEntityID());
                        for (Player p : playerHashMap.values())
                            p.getSession().send(destroyEntitiesPacket);
                        playerHashMap.remove(player);
                    }
                }
            });

            server.bind();
			while(server.isListening()) {
				for(Player player : playerHashMap.values()) {
					EntityMetadata changes[] = player.getMetadata().getChanges();
					if(changes != null)
						for(Player p : playerHashMap.values())
							if(!p.gameProfile().getName().equals(player.gameProfile().getName()))
								p.getSession().send(new ServerEntityMetadataPacket(player.getEntityID(), changes));
				}

				// Don't overload CPU with loops.
				try {
					Thread.sleep(20);
				} catch(InterruptedException e) { }
			}
        }
    }

    private static void updatePlayerPosition(Session session, ClientPlayerMovementPacket packet) {
        GameProfile profile = session.getFlag(ProtocolConstants.PROFILE_KEY);
        Player player = playerHashMap.get(profile.getId());
        if (packet instanceof ClientPlayerPositionPacket || packet instanceof ClientPlayerPositionRotationPacket) {
            player.setX(packet.getX());
            player.setY(packet.getY());
            player.setZ(packet.getZ());
        }

        if (packet instanceof ClientPlayerRotationPacket || packet instanceof ClientPlayerPositionRotationPacket) {
            player.setYaw(packet.getYaw());
            player.setPitch(packet.getPitch());
        }

        player.setOnGround(packet.isOnGround());
    }

    private static List<Packet> createUpdatePackets(Session session, ClientPlayerMovementPacket packet) {
        List<Packet> packets = new ArrayList<Packet>();
        GameProfile profile = session.getFlag(ProtocolConstants.PROFILE_KEY);
		Player player = playerHashMap.get(profile.getId());
		if(packet instanceof ClientPlayerPositionPacket || packet instanceof ClientPlayerPositionRotationPacket) {
            double movedX = packet.getX() - player.getX();
            double movedY = packet.getY() - player.getY();
            double movedZ = packet.getZ() - player.getZ();
            float yaw = player.getYaw();
            float pitch = player.getPitch();
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

    public Server getServer() {
        return this.server;
    }
}
